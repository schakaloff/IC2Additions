package com.ic2additions.tilentity;

import com.ic2additions.util.EnergyStorageRF;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySource;
import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import ic2.core.network.GuiSynced;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileEntityRFtoEU extends TileEntityInventory
        implements IEnergySource, IHasGui, IGuiValueProvider {

    private final EnergyStorageRF rfStorage = new EnergyStorageRF(40000); // RF buffer
    private int euBuffer = 0;
    private final int maxEuBuffer = 10000;

    private static final double CONVERSION = 4.0; // 4 RF = 1 EU

    @GuiSynced
    private int selectedTier = 1; // default LV

    // --- Tick update: convert RF -> EU into buffer ---
    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();

        int rfAvailable = rfStorage.getEnergyStored();
        if (rfAvailable > 0 && euBuffer < maxEuBuffer) {
            int maxEuThisTick = tierToEU(selectedTier);
            int euSpace = Math.min(maxEuBuffer - euBuffer, maxEuThisTick);

            int rfToExtract = Math.min(rfAvailable, (int) (euSpace * CONVERSION));
            int euProduced = (int) (rfToExtract / CONVERSION);

            rfStorage.extractEnergy(rfToExtract, false);
            euBuffer += euProduced;
        }
    }

    // --- IC2 EnergyNet hooks ---
    @Override
    public double getOfferedEnergy() {
        return Math.min(euBuffer, tierToEU(selectedTier));
    }

    @Override
    public void drawEnergy(double amount) {
        euBuffer -= amount;
        if (euBuffer < 0) euBuffer = 0;
    }

    @Override
    public int getSourceTier() {
        return selectedTier;
    }

    @Override
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return true; // can output on any side
    }

    // --- Tier logic ---
    private int tierToEU(int tier) {
        switch (tier) {
            case 1: return 32;   // LV
            case 2: return 128;  // MV
            case 3: return 512;  // HV
            case 4: return 2048; // EV
            case 5: return 8192; // IV
            default: return 32;
        }
    }

    public void nextTier() {
        selectedTier++;
        if (selectedTier > 5) selectedTier = 1;
        markDirty();
    }

    public void prevTier() {
        selectedTier--;
        if (selectedTier < 1) selectedTier = 5;
        markDirty();
    }

    public int getSelectedTier() {
        return selectedTier;
    }

    public int getEuBuffer() {
        return euBuffer;
    }

    public EnergyStorageRF getRfStorage() {
        return rfStorage;
    }

    // --- GUI support ---
    @Override
    public ContainerBase<? extends TileEntityRFtoEU> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return DynamicGui.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) { }

    @Override
    public double getGuiValue(String key) {
        switch (key) {
            case "tier": return selectedTier;
            case "euBuffer": return euBuffer;
            case "rfBuffer": return rfStorage.getEnergyStored();
            default: return 0;
        }
    }

    // --- NBT persistence ---
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        this.selectedTier = nbt.getInteger("selectedTier");
        this.euBuffer = nbt.getInteger("euBuffer");
        rfStorage.receiveEnergy(nbt.getInteger("rfEnergy"), false);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("selectedTier", selectedTier);
        nbt.setInteger("euBuffer", euBuffer);
        nbt.setInteger("rfEnergy", rfStorage.getEnergyStored());
        return nbt;
    }

    // --- Capabilities (for RF input) ---
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
    protected void onLoaded() {
        super.onLoaded();
        if (!world.isRemote) {
            EnergyNet.instance.addTile(this);
        }
    }

    @Override
    protected void onUnloaded() {
        super.onUnloaded();
        if (!world.isRemote) {
            EnergyNet.instance.removeTile(this);
        }
    }
}
