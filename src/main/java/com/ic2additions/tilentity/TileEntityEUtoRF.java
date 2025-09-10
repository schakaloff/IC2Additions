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
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEUtoRF extends TileEntity implements ITickable, IEnergySink {

    private int euBuffer = 0;
    private final int maxEuBuffer = 10000;
    private final EnergyStorageRF rfStorage = new EnergyStorageRF(40000);
    private static final double CONVERSION = 4; //1EU FOR 4RF

    @Override
    public void update() {
        if (!world.isRemote) {
            // Convert EU → RF
            if (euBuffer > 0 && rfStorage.getEnergyStored() < rfStorage.getMaxEnergyStored()) {
                int rfSpace = rfStorage.getMaxEnergyStored() - rfStorage.getEnergyStored();
                int euToConvert = Math.min(euBuffer, (int)(rfSpace / CONVERSION));
                int rfProduced = (int)(euToConvert * CONVERSION);

                if (rfProduced > 0) {
                    euBuffer -= euToConvert;
                    rfStorage.receiveEnergy(rfProduced, false);
                }
            }
            for (EnumFacing side : EnumFacing.values()) {
                TileEntity neighbor = world.getTileEntity(pos.offset(side));
                if (neighbor != null) {
                    IEnergyStorage neighborEnergy = neighbor.getCapability(CapabilityEnergy.ENERGY, side.getOpposite());
                    if (neighborEnergy != null) {
                        int extracted = rfStorage.extractEnergy(rfStorage.getEnergyStored(), true);
                        int accepted = neighborEnergy.receiveEnergy(extracted, false);
                        rfStorage.extractEnergy(accepted, false);
                    }
                }
            }
        }
    }

    @Override
    public double getDemandedEnergy() {
        int rfSpace = rfStorage.getMaxEnergyStored() - rfStorage.getEnergyStored();
        int euNeeded = (int) (rfSpace / CONVERSION);
        return Math.max(0, Math.min(maxEuBuffer - euBuffer, euNeeded));
    }

    @Override
    public int getSinkTier() {
        return 4; // MV tier
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
