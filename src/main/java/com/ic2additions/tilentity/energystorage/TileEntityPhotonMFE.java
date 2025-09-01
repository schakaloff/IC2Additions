package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityPhotonMFE extends TileEntityElectricBlock {
    public TileEntityPhotonMFE(){
        super(8,10240, 400_000_000);
        this.chargeSlot.setTier(8);
        this.dischargeSlot.setTier(8);
    }
}
