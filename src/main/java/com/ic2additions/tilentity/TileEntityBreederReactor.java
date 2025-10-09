package com.ic2additions.tilentity;

import com.ic2additions.recipes.breeding.BreederReactorRecipesHandler;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityBreederReactor extends TileEntityInventory implements IEnergySink, IHasGui, IGuiValueProvider {

    public final InvSlot input;
    public final InvSlotOutput output;

    private static final int DEFAULT_SINK_TIER = 12;
    private static final double MAX_EU_PER_TICK = 128.0;

    private boolean addedToEnet;

    @GuiSynced private double energyBuffer;
    @GuiSynced private int progressTicks;
    @GuiSynced private int currentRecipeTotalTime;
    @GuiSynced private ItemStack currentInput = ItemStack.EMPTY;

    private BreederReactorRecipesHandler.Recipe currentRecipe;

    @GuiSynced private String currentInputName = "-";
    @GuiSynced private String currentOutputName = "-";
    @GuiSynced private int currentRecipeCostEu = 0;

    public TileEntityBreederReactor() {
        super();
        this.input = new InvSlot(this, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
        this.output = new InvSlotOutput(this, "output", 1);
    }

    // === EnergyNet registration ===
    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent((IEnergyTile) this));
            addedToEnet = true;
        }
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        if (addedToEnet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent((IEnergyTile) this));
            addedToEnet = false;
        }
    }

    // === Server tick ===
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        boolean active = getActive();
        boolean dirty = false;

        if (currentRecipe == null) {
            ItemStack in = input.get();
            BreederReactorRecipesHandler.Recipe recipe = BreederReactorRecipesHandler.find(in);

            if (recipe != null && canAcceptOutput(recipe.output)) {
                // Only start when enough EU is stored
                if (energyBuffer >= recipe.totalEU) {
                    in.shrink(1);
                    if (in.getCount() <= 0) input.clear();

                    energyBuffer -= recipe.totalEU;

                    currentRecipe = recipe;
                    currentInput = recipe.input.copy();
                    progressTicks = 0;
                    currentRecipeTotalTime = recipe.totalTime;

                    currentInputName = recipe.input.getDisplayName();
                    currentOutputName = recipe.output.getDisplayName();
                    currentRecipeCostEu = recipe.totalEU;

                    active = true;
                    dirty = true;
                }
            } else {
                currentInputName = "-";
                currentOutputName = "-";
                currentRecipeCostEu = 0;
                currentRecipeTotalTime = 0;
                active = false;
            }
        }

        // Handle active recipe progress
        if (currentRecipe != null) {
            progressTicks++;
            active = true;
            dirty = true;

            if (progressTicks >= currentRecipe.totalTime) {
                finishRecipe();
                active = false;
                dirty = true;
            }
        }

        if (getActive() != active) {
            setActive(active);
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }

        if (dirty) markDirty();
    }

    // === Energy sink behavior ===
    @Override
    public double injectEnergy(EnumFacing from, double amount, double voltage) {
        energyBuffer += amount;
        return 0;
    }

    @Override
    public double getDemandedEnergy() {
        ItemStack in = input.get();
        BreederReactorRecipesHandler.Recipe recipe = BreederReactorRecipesHandler.find(in);
        if (recipe == null) return 0;
        if (canAcceptOutput(recipe.output) && currentRecipe == null) {
            return Math.max(0, recipe.totalEU - energyBuffer);
        }
        return 0;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) { return true; }
    @Override
    public int getSinkTier() { return DEFAULT_SINK_TIER; }

    // === Recipe completion ===
    private void finishRecipe() {
        if (currentRecipe == null) return;
        if (canAcceptOutput(currentRecipe.output)) {
            output.add(currentRecipe.output);
        }
        currentRecipe = null;
        currentInput = ItemStack.EMPTY;
        progressTicks = 0;
        currentRecipeTotalTime = 0;

        currentInputName = "-";
        currentOutputName = "-";
        currentRecipeCostEu = 0;
    }

    private boolean canAcceptOutput(ItemStack stack) {
        return !StackUtil.isEmpty(stack) && output.canAdd(stack);
    }

    // === GUI ===
    @Override
    public ContainerBase<? extends TileEntityBreederReactor> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}

    // === Value provider ===
    @Override
    public double getGuiValue(String key) {
        switch (key) {
            case "progress01":
            case "time01":
                return currentRecipeTotalTime <= 0 ? 0 : (double) progressTicks / currentRecipeTotalTime;
            case "progress":
            case "time":
                return currentRecipeTotalTime <= 0 ? 0 : Math.round(((double) progressTicks / currentRecipeTotalTime) * 100.0);
            case "eut":
                return energyBuffer;
            default:
                return 0;
        }
    }

    // === Getters used by %base ===
    public double getEutNow() { return energyBuffer; }

    public double getProgressPercent() {
        if (currentRecipeTotalTime <= 0) return 0;
        return Math.round(((double) progressTicks / currentRecipeTotalTime) * 100.0);
    }

    public double getTimeProgressPercent() {
        if (currentRecipeTotalTime <= 0) return 0;
        return Math.round(((double) progressTicks / currentRecipeTotalTime) * 100.0);
    }

    // This is what %base.getTime()% calls
    public double getTime() {
        return getTimeProgressPercent();
    }

    public String getCurrentInputName() { return currentInputName; }
    public String getCurrentOutputName() { return currentOutputName; }
    public int getRecipeCostEu() { return currentRecipeCostEu; }

    // === Save/load ===
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        energyBuffer = nbt.getDouble("energyBuffer");
        progressTicks = nbt.getInteger("progressTicks");
        currentRecipeTotalTime = nbt.getInteger("currentRecipeTotalTime");
        currentInput = new ItemStack(nbt.getCompoundTag("currentInput"));
        currentInputName = nbt.getString("currentInputName");
        currentOutputName = nbt.getString("currentOutputName");
        currentRecipeCostEu = nbt.getInteger("currentRecipeCostEu");

        if (!currentInput.isEmpty()) {
            currentRecipe = BreederReactorRecipesHandler.find(currentInput);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energyBuffer", energyBuffer);
        nbt.setInteger("progressTicks", progressTicks);
        nbt.setInteger("currentRecipeTotalTime", currentRecipeTotalTime);
        NBTTagCompound inputTag = new NBTTagCompound();
        currentInput.writeToNBT(inputTag);
        nbt.setTag("currentInput", inputTag);
        nbt.setString("currentInputName", currentInputName);
        nbt.setString("currentOutputName", currentOutputName);
        nbt.setInteger("currentRecipeCostEu", currentRecipeCostEu);
        return nbt;
    }
}
