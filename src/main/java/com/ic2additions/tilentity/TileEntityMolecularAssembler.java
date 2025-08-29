package com.ic2additions.tilentity;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.Recipes;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.block.invslot.InvSlotOutput;
import ic2.core.block.invslot.InvSlotProcessable;
import ic2.core.block.invslot.InvSlotProcessableGeneric;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.MinecraftForge;

import java.util.Collection;

public class TileEntityMolecularAssembler extends TileEntityInventory implements IEnergySink, IHasGui, IGuiValueProvider {

    public final InvSlotProcessable<IRecipeInput, Collection<ItemStack>, ItemStack> input;
    public final InvSlotOutput output;

    //private final IMachineRecipeManager<IRecipeInput, Collection<ItemStack>, ItemStack> recipes;

    private static final int DEFAULT_SINK_TIER = 14;
    private static final int CYCLE_TOTAL_EU     = 100_000;  // adjust as you like
    private static final byte MAX_STARVE_WAIT   = 40;

    private boolean addedToEnet;
    private double energyInThisTick;
    private double energyGivenThisTick;
    @GuiSynced
    private double lastEnergyGiven;
    @GuiSynced
    private double energyUsed;
    private byte starveWait;

    public TileEntityMolecularAssembler() {
        super();
        this.input = new InvSlotProcessableGeneric(this, "input", 1,null);
        this.output = new InvSlotOutput(this, "output", 1);
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        if (!world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToEnet = true;
        }
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        if (addedToEnet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnet = false;
        }
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        boolean shouldBeActive = this.getActive();
        final boolean hasWork = energyUsed < CYCLE_TOTAL_EU;
        shouldBeActive = hasWork || energyInThisTick > 0;
        lastEnergyGiven = energyGivenThisTick;
        energyGivenThisTick = 0;

        if (shouldBeActive && hasWork) {
            if (energyInThisTick <= 0) {
                if (starveWait < MAX_STARVE_WAIT) {
                    starveWait++;
                } else {
                    shouldBeActive = false;
                }
            } else {
                starveWait = 0;
                double need = getDemandedEnergy();
                if (energyInThisTick >= need) {
                    energyInThisTick -= need;
                    energyUsed = 0;
                } else {
                    // Partial progress
                    energyUsed += energyInThisTick;
                    energyInThisTick = 0;
                }
            }
        }
        if (this.getActive() != shouldBeActive) {
            this.setActive(shouldBeActive);
            this.world.checkLightFor(EnumSkyBlock.BLOCK, this.pos);
        }
    }


    @Override
    public ContainerBase<? extends TileEntityMolecularAssembler> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {
        // no-op
    }

    @Override
    public double getGuiValue(String key) {
        switch (key) {
            case "progress":
                return CYCLE_TOTAL_EU == 0 ? 0 : energyUsed / CYCLE_TOTAL_EU; // 0..1
            case "eut":
                return lastEnergyGiven; // last tick EU injected
            default:
                return 0;
        }
    }

    public double getEutNow() {
        return Math.round(lastEnergyGiven * 10.0) / 10.0;
    }

    public double getProgressPercent() {
        if (CYCLE_TOTAL_EU == 0) return 0;
        double percent = (energyUsed / CYCLE_TOTAL_EU) * 100.0;
        return Math.round(percent);
    }


    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return true;
    }

    @Override
    public int getSinkTier() {
        return DEFAULT_SINK_TIER;
    }

    @Override
    public double getDemandedEnergy() {
        double need = Math.max(0, CYCLE_TOTAL_EU - energyUsed);
        return need;
    }

    @Override
    public double injectEnergy(EnumFacing from, double amount, double voltage) {
        energyGivenThisTick += amount;

        double need = getDemandedEnergy();
        if (need <= 0) {
            return amount;
        }

        if (amount <= need) {
            energyInThisTick += amount;
            return 0;
        } else {
            energyInThisTick += need;
            return amount - need;
        }
    }
}
