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
import ic2.core.util.StackUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityBreederReactor extends TileEntityInventory implements IEnergySink, IHasGui, IGuiValueProvider {

    // === Inventory ===
    public final InvSlot input;
    public final InvSlotOutput output;

    // === Energy/Recipe variables ===
    private static final int DEFAULT_SINK_TIER = 12;

    private boolean addedToEnet;
    private double energyBuffer; // energy received but not yet used
    private double energyUsed;   // total EU used for current recipe
    private int progressTicks;   // elapsed ticks for current recipe

    private BreederReactorRecipesHandler.Recipe currentRecipe;
    private ItemStack currentInput = ItemStack.EMPTY;

    // === Constructor ===
    public TileEntityBreederReactor() {
        super();
        this.input = new InvSlot(this, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
        this.output = new InvSlotOutput(this, "output", 1);
    }

    // === EnergyNet Registration ===
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

    // === Main Update Logic ===
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        boolean active = getActive();
        boolean dirty = false;

        // If no recipe is active, try to start one
        if (currentRecipe == null) {
            ItemStack in = input.get();
            BreederReactorRecipesHandler.Recipe recipe = BreederReactorRecipesHandler.find(in);

            if (recipe != null && canAcceptOutput(recipe.output)) {
                currentRecipe = recipe;
                currentInput = in.copy();
                in.shrink(1);
                if (in.getCount() <= 0) input.clear();

                energyUsed = 0;
                progressTicks = 0;
                active = true;
                dirty = true;
            } else {
                active = false;
            }
        }

        // If we have a running recipe, consume energy & time
        if (currentRecipe != null) {
            double neededEnergy = currentRecipe.totalEU - energyUsed;

            // consume a portion of energy each tick (simulate slow charging)
            double perTickUsage = Math.min(energyBuffer, Math.min(neededEnergy, 128)); // max 128 EU/tick
            energyBuffer -= perTickUsage;
            energyUsed += perTickUsage;

            // Only advance time if energy is being consumed
            if (perTickUsage > 0) {
                progressTicks++;
            }

            // Check for completion
            if (energyUsed >= currentRecipe.totalEU && progressTicks >= currentRecipe.totalTime) {
                finishRecipe();
                dirty = true;
                active = false;
            }
        }

        // Update block active state
        if (getActive() != active) {
            setActive(active);
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }

        if (dirty) markDirty();
    }

    // === Recipe Completion ===
    private void finishRecipe() {
        if (currentRecipe == null) return;
        if (canAcceptOutput(currentRecipe.output)) {
            output.add(currentRecipe.output);
        }
        currentRecipe = null;
        currentInput = ItemStack.EMPTY;
        energyUsed = 0;
        progressTicks = 0;
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
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public double getGuiValue(String key) {
        switch (key) {
            case "progress01": return getProgress01();
            case "progress": return getProgressPercent();
            case "time01": return getTimeProgress01();
            case "time": return getTimeProgressPercent();
            default: return 0;
        }
    }

    // === Progress getters ===
    public double getProgress01() {
        if (currentRecipe == null || currentRecipe.totalEU <= 0) return 0;
        return Math.min(1.0, energyUsed / currentRecipe.totalEU);
    }

    public double getProgressPercent() {
        return Math.round(getProgress01() * 100.0);
    }

    public double getTimeProgress01() {
        if (currentRecipe == null || currentRecipe.totalTime <= 0) return 0;
        return Math.min(1.0, (double) progressTicks / currentRecipe.totalTime);
    }

    public double getTimeProgressPercent() {
        return Math.round(getTimeProgress01() * 100.0);
    }

    // === Energy Sink ===
    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) { return true; }

    @Override
    public int getSinkTier() { return DEFAULT_SINK_TIER; }

    @Override
    public double getDemandedEnergy() {
        if (currentRecipe == null) return 0;
        return Math.max(0, currentRecipe.totalEU - energyUsed);
    }

    @Override
    public double injectEnergy(EnumFacing from, double amount, double voltage) {
        double need = getDemandedEnergy();
        if (need <= 0) return amount;
        if (amount <= need) {
            energyBuffer += amount;
            return 0;
        } else {
            energyBuffer += need;
            return amount - need;
        }
    }

    // === NBT ===
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        energyBuffer = nbt.getDouble("energyBuffer");
        energyUsed = nbt.getDouble("energyUsed");
        progressTicks = nbt.getInteger("progressTicks");

        if (nbt.hasKey("currentInput")) {
            currentInput = new ItemStack(nbt.getCompoundTag("currentInput"));
            BreederReactorRecipesHandler.Recipe r = BreederReactorRecipesHandler.find(currentInput);
            if (r != null) currentRecipe = r;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energyBuffer", energyBuffer);
        nbt.setDouble("energyUsed", energyUsed);
        nbt.setInteger("progressTicks", progressTicks);
        if (!currentInput.isEmpty()) {
            NBTTagCompound itemTag = new NBTTagCompound();
            currentInput.writeToNBT(itemTag);
            nbt.setTag("currentInput", itemTag);
        }
        return nbt;
    }
}
