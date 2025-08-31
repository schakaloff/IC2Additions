package com.ic2additions.tilentity;

import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.network.GuiSynced;

public class BaseSolarPanel extends TileEntityBaseGenerator {

    private final double euAtFullSun;
    private int tickCounter = 0;

    @GuiSynced
    public float skyLight;

    public BaseSolarPanel(double euAtFullSun, int tier, int storage) {
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
        if (++this.tickCounter % 128 == 0) {
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

    @Override
    public boolean getGuiState(String name) {
        if ("sunlight".equals(name)) return this.skyLight > 0.0F;
        return super.getGuiState(name);
    }

    public double getEutNow() {
        double raw = this.euAtFullSun * this.skyLight;
        return Math.round(raw * 10.0) / 10.0;
    }
}