package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityQuantumMFE extends TileEntityElectricBlock {
    public TileEntityQuantumMFE(){
        super(7,131072, 1_350_000_000);
        this.chargeSlot.setTier(7);
        this.dischargeSlot.setTier(7);
    }
}
