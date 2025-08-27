package com.ic2additions.tilentity;

import ic2.core.block.generator.tileentity.TileEntitySolarGenerator;
public class TileEntitySolarPanel extends TileEntitySolarGenerator {
    protected final double euAtFullSun;
    private int tickCounter = 0;
    private static final int TICK_RATE = 128;

    public TileEntitySolarPanel(double euAtFullSun){
        super();
        this.euAtFullSun = euAtFullSun;
    }
    @Override
    public boolean gainEnergy() {
        if (++this.tickCounter % TICK_RATE == 0) {
            this.updateSunVisibility();
        }
        if (this.skyLight > 0.0F) {
            this.energy.addEnergy(this.euAtFullSun * (double) this.skyLight);
            return true;
        }
        return false;
    }

    @Override
    public void updateSunVisibility() {
        this.skyLight = TileEntitySolarGenerator.getSkyLight(this.getWorld(), this.pos.up());
    }
}
