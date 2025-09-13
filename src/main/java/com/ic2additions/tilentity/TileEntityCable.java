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
public class TileEntityCable extends TileEntity implements IEnergyConductor {

    public static final String NBT_NAME = "name";
    public static final String NBT_LOSS = "loss";
    public static final String NBT_CAPACITY = "capacity";

    public static final String DEFAULT_NAME = "Cable";
    public static final double DEFAULT_LOSS = 0.05D;
    public static final int DEFAULT_CAPACITY = 38168;

    private boolean addedToEnergyNet = false;
    private byte connectivity = 0;

    private String displayName = DEFAULT_NAME;
    private double loss = DEFAULT_LOSS;
    private int capacity = DEFAULT_CAPACITY;

    /* ---------- setters used by block/item on placement ---------- */
    public void setDisplayName(String name) { this.displayName = name != null ? name : DEFAULT_NAME; }
    public void setLoss(double v)          { this.loss = v; }
    public void setCapacity(int v)         { this.capacity = v; }

    /* ---------- rendering/logic helpers ---------- */
    public byte getConnectivity() { return connectivity; }
    private void setConnectivity(byte value) {
        connectivity = value;
        if (world != null) {
            IBlockState s = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, s, s, 3);
            markDirty();
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
                if (ok) newConn |= mask;
            }
            mask <<= 1;
        }
        if (newConn != connectivity) setConnectivity(newConn);
    }

    /* ---------- lifecycle ---------- */
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

    @Override
    public void update() {}

    /* ---------- IEnergyConductor ---------- */
    @Override public double getConductionLoss() { return loss; }
    @Override public double getInsulationEnergyAbsorption() { return Integer.MAX_VALUE; }
    @Override public double getInsulationBreakdownEnergy() { return 9001; }
    @Override public double getConductorBreakdownEnergy() { return capacity + 1; }
    @Override public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) { return true; }
    @Override public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) { return true; }
    @Override public void removeInsulation() { }
    @Override public void removeConductor() { if (world != null && !world.isRemote) world.setBlockToAir(pos); }

    /* ---------- NBT ---------- */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey(NBT_NAME))     displayName = nbt.getString(NBT_NAME);
        if (nbt.hasKey(NBT_LOSS))     loss = nbt.getDouble(NBT_LOSS);
        if (nbt.hasKey(NBT_CAPACITY)) capacity = nbt.getInteger(NBT_CAPACITY);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString(NBT_NAME, displayName);
        nbt.setDouble(NBT_LOSS, loss);
        nbt.setInteger(NBT_CAPACITY, capacity);
        return nbt;
    }
}
