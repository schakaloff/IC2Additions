package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityPhotonMFE extends TileEntityElectricBlock {
    public TileEntityPhotonMFE(){
        super(8,131072,  2_147_000_000);
        this.chargeSlot.setTier(8);
        this.dischargeSlot.setTier(8);
    }
}
