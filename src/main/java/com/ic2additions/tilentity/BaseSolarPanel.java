package com.ic2additions.tilentity;

import ic2.core.IC2;
import ic2.core.block.generator.tileentity.TileEntitySolarGenerator; // for getSkyLight(...)
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.network.GuiSynced;
import net.minecraft.util.math.BlockPos;

public abstract class BaseSolarPanel extends TileEntityBaseGenerator {

    protected final double euAtFullSun;      // EU/t at skyLight == 1.0
    private int tickCounter = 0;
    private static final int TICK_RATE = 128;

    @GuiSynced
    public float skyLight;                   // same field name IC2 uses

    /**
     * @param euAtFullSun EU/t at full sun
     * @param tier        IC2 source tier (1..4)
     * @param storage     internal buffer (EU)
     */
    protected BaseSolarPanel(double euAtFullSun, int tier, int storage) {
        // production param in BaseGenerator is irrelevant for solar; set 0.
        super(0.0D, tier, storage);
        this.euAtFullSun = euAtFullSun;
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        updateSunVisibility();
    }

    @Override
    public boolean gainEnergy() {
        if (++this.tickCounter % TICK_RATE == 0) {
            updateSunVisibility();
        }
        if (this.skyLight > 0.0F) {
            this.energy.addEnergy(this.euAtFullSun * (double) this.skyLight);
            return true;
        }
        return false;
    }

    @Override
    public boolean gainFuel() { return false; }

    public void updateSunVisibility() {
        this.skyLight = TileEntitySolarGenerator.getSkyLight(this.getWorld(), this.pos.up());
    }

    @Override
    public boolean needsFuel() { return false; }

    @Override
    protected boolean delayActiveUpdate() { return true; }

    // GUI boolean used by IC2 solar XML
    @Override
    public boolean getGuiState(String name) {
        if ("sunlight".equals(name)) return this.skyLight > 0.0F;
        return super.getGuiState(name);
    }
}