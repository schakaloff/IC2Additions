package com.ic2additions.tilentity;

import com.ic2additions.recipes.MolecularAssemblerRecipesHandler;
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



public class TileEntityMolecularAssembler extends TileEntityInventory implements IEnergySink, IHasGui, IGuiValueProvider {
    public final InvSlot input;
    public final InvSlotOutput output;

    private MolecularAssemblerRecipesHandler.Recipe current;
    @GuiSynced private int currentTotalEu;
    private int toConsume;

    private static final int  DEFAULT_SINK_TIER = 12;
    private static final byte MAX_STARVE_WAIT   = 40;

    private boolean addedToEnet;
    private double energyInThisTick;
    private double energyGivenThisTick;

    @GuiSynced private double lastEnergyGiven;
    @GuiSynced private double energyUsed;
    private byte starveWait;

    public TileEntityMolecularAssembler() {
        super();
        this.input = new InvSlot(this, "input", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
        this.output = new InvSlotOutput(this, "output", 1);
    }

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


    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        boolean shouldBeActive = getActive();
        boolean invChanged = false;

        if (current == null) {
            ItemStack in = input.get();
            MolecularAssemblerRecipesHandler.Recipe r = MolecularAssemblerRecipesHandler.find(in);
            if (r != null && canAcceptOutput(r.output)) {
                toConsume = r.input.getCount() > 0 ? r.input.getCount() : 1;
                if (in.getCount() >= toConsume) {
                    in.shrink(toConsume);
                    if (in.getCount() <= 0) input.clear();
                    current = r;
                    currentTotalEu = r.totalEU;
                    energyUsed = 0;
                    invChanged = true;
                    shouldBeActive = true;
                }
            } else {
                shouldBeActive = false;
            }
        } else {
            shouldBeActive = true;
        }
        lastEnergyGiven = energyGivenThisTick;
        energyGivenThisTick = 0;
        if (shouldBeActive && current != null) {
            if (energyInThisTick <= 0) {
                if (starveWait < MAX_STARVE_WAIT) {
                    starveWait++;
                } else {
                    shouldBeActive = false;
                }
            } else {
                starveWait = 0;
                double need = getDemandedEnergy();
                if (need > 0 && energyInThisTick >= need) {
                    energyInThisTick -= need;
                    finishWork();
                    invChanged = true;
                } else {
                    energyUsed += energyInThisTick;
                    energyInThisTick = 0;
                }
            }
        }

        if (getActive() != shouldBeActive) {
            setActive(shouldBeActive);
            world.checkLightFor(EnumSkyBlock.BLOCK, pos);
        }
        if (invChanged) markDirty();
    }

    private boolean canAcceptOutput(ItemStack stack) {
        return !StackUtil.isEmpty(stack) && output.canAdd(stack);
    }

    private void finishWork() {
        if (current == null) return;
        output.add(current.output);
        current = null;
        currentTotalEu = 0;
        energyUsed = 0;
        toConsume = 0;
    }

    /* ---------- GUI ---------- */
    @Override
    public ContainerBase<? extends TileEntityMolecularAssembler> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override public void onGuiClosed(EntityPlayer player) {}

    @Override
    public double getGuiValue(String key) {
        switch (key) {
            case "progress":
                return (currentTotalEu <= 0) ? 0 : (energyUsed / currentTotalEu) * 100.0;
            case "progress01":
                return (currentTotalEu <= 0) ? 0 : (energyUsed / currentTotalEu);
            case "eut":
                return lastEnergyGiven;
            default:
                return 0;
        }
    }

    public double getProgress01() {
        if (currentTotalEu <= 0) return 0.0;
        double v = energyUsed / currentTotalEu;
        if (v < 0) v = 0;
        if (v > 1) v = 1;
        return v;
    }
    public double getProgressPercent() {
        return currentTotalEu <= 0 ? 0.0 : Math.round((energyUsed / currentTotalEu) * 100.0);
    }
    public double getEutNow() {
        return Math.round(lastEnergyGiven * 10.0) / 10.0;
    }
    public int getTotalEu() {
        return currentTotalEu;
    }

    @Override public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) { return true; }
    @Override public int getSinkTier() { return DEFAULT_SINK_TIER; }

    @Override
    public double getDemandedEnergy() {
        if (current == null || currentTotalEu <= 0) { lastEnergyGiven = 0; return 0; }
        return Math.max(0, currentTotalEu - energyUsed);
    }

    @Override
    public double injectEnergy(EnumFacing from, double amount, double voltage) {
        energyGivenThisTick += amount;
        double need = getDemandedEnergy();
        if (need <= 0) return amount;
        if (amount <= need) { energyInThisTick += amount; return 0; }
        energyInThisTick += need;
        return amount - need;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.energyUsed     = nbt.getDouble("energyUsed");
        this.currentTotalEu = nbt.getInteger("currentTotalEu");
        if (this.currentTotalEu > 0) {
            this.current = new MolecularAssemblerRecipesHandler.Recipe(ItemStack.EMPTY, this.currentTotalEu, ItemStack.EMPTY);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("energyUsed", this.energyUsed);
        nbt.setInteger("currentTotalEu", this.currentTotalEu);
        return nbt;
    }
}
