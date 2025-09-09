package com.ic2additions.tilentity;

import com.ic2additions.util.EnergyStorageRF;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

public class TileEntityEUtoRF extends TileEntity implements ITickable, IEnergySink {

    private int euBuffer = 0;
    private final int maxEuBuffer = 10000;

    private final EnergyStorageRF rfStorage = new EnergyStorageRF(40000);

    // Conversion rate: 4 EU -> 1 RF
    private static final double CONVERSION = 0.25;

    @Override
    public void update() {
        if (!world.isRemote) {
            int rfSpace = rfStorage.getMaxEnergyStored() - rfStorage.getEnergyStored();
            int euNeeded = (int)(rfSpace / CONVERSION);

            if (euBuffer > 0 && rfSpace > 0) {
                int euToConvert = Math.min(euBuffer, euNeeded);
                int rfProduced = (int)(euToConvert * CONVERSION);

                euBuffer -= euToConvert;
                rfStorage.receiveEnergy(rfProduced, false);
            }
        }
    }

    // --- IC2 EnergySink ---
    @Override
    public double getDemandedEnergy() {
        return maxEuBuffer - euBuffer;
    }

    @Override
    public int getSinkTier() {
        return 2; // MV tier
    }

    @Override
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        int accepted = (int) Math.min(amount, maxEuBuffer - euBuffer);
        euBuffer += accepted;
        return amount - accepted;
    }

    @Override
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return true;
    }

    // --- Forge Capabilities ---
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return true;
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) return CapabilityEnergy.ENERGY.cast(rfStorage);
        return super.getCapability(capability, facing);
    }

    // --- EnergyNet registration ---
    @Override
    public void onLoad() {
        if (!world.isRemote) {
            EnergyNet.instance.addTile(this);
        }
    }

    @Override
    public void invalidate() {
        if (!world.isRemote) {
            EnergyNet.instance.removeTile(this);
        }
        super.invalidate();
    }

    // --- Getters for block display ---
    public int getEuBuffer() {
        return euBuffer;
    }

    public EnergyStorageRF getRfStorage() {
        return rfStorage;
    }
}
