package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityMFSUTWO extends TileEntityElectricBlock {
    public TileEntityMFSUTWO(){
        super(5,8192, 400_000_000);
        this.chargeSlot.setTier(5);
        this.dischargeSlot.setTier(5);
    }
}
