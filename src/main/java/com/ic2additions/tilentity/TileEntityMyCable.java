package com.ic2additions.tilentity;

import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

public class TileEntityMyCable extends TileEntity implements IEnergyConductor {
    private boolean addedToEnergyNet = false;
    private byte connectivity = 0;
    public static final double DEFAULT_LOSS = 0.05;
    public static final int DEFAULT_CAPACITY = 10240;
    private double loss = DEFAULT_LOSS;
    private int capacity = DEFAULT_CAPACITY;


    public byte getConnectivity() { return connectivity; }
    private boolean canInteractWith(IEnergyTile other, EnumFacing side) {return true;}
    public void setLoss(double v) { this.loss = v; }
    public void setCapacity(int v) { this.capacity = v; }

    private void setConnectivity(byte value) {
        connectivity = value;
        if (world != null) {
            IBlockState s = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, s, s, 3);
            TileEntity te = world.getTileEntity(pos);
            if (te != null) te.markDirty();
        }
    }

    public void updateConnectivity() {
        if (world == null || world.isRemote) return;
        byte newConn = 0;
        int mask = 1;
        for (EnumFacing dir : EnumFacing.VALUES) {
            IEnergyTile t = EnergyNet.instance.getSubTile(world, pos.offset(dir));
            if (t != null) {
                boolean ok =
                        (t instanceof IEnergyAcceptor && ((IEnergyAcceptor) t).acceptsEnergyFrom(this, dir.getOpposite())) ||
                                (t instanceof IEnergyEmitter  && ((IEnergyEmitter)  t).emitsEnergyTo(this, dir.getOpposite()));
                if (ok && canInteractWith(t, dir)) newConn |= mask;
            }
            mask <<= 1;
        }
        if (newConn != connectivity) setConnectivity(newConn);
    }

    @Override
    public void onLoad() {
        if (!world.isRemote) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            addedToEnergyNet = true;
            updateConnectivity();
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (!world.isRemote && addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }
    }

    @Override
    public void onChunkUnload() {
        super.onChunkUnload();
        if (!world.isRemote && addedToEnergyNet) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
        }
    }

    @Override public double getConductionLoss() { return loss; }
    @Override public double getInsulationEnergyAbsorption() { return Integer.MAX_VALUE; }
    @Override public double getInsulationBreakdownEnergy() { return 9001; }
    @Override public double getConductorBreakdownEnergy() { return capacity + 1; }
    @Override public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) { return canInteractWith(emitter, side); }
    @Override public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) { return canInteractWith(receiver, side); }
    @Override public void removeInsulation() {}
    @Override public void removeConductor() { if (world != null && !world.isRemote) world.setBlockToAir(pos); }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("loss"))     loss = nbt.getDouble("loss");
        if (nbt.hasKey("capacity")) capacity = nbt.getInteger("capacity");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setDouble("loss", loss);
        nbt.setInteger("capacity", capacity);
        return nbt;
    }
}
