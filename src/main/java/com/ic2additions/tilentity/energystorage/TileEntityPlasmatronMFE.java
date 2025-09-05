package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityPlasmatronMFE extends TileEntityElectricBlock {
    public TileEntityPlasmatronMFE(){
        super(5,8192, 150_000_000);
        this.chargeSlot.setTier(5);
        this.dischargeSlot.setTier(5);
    }
}
