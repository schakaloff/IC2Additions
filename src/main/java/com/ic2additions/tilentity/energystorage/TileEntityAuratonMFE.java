package com.ic2additions.tilentity.energystorage;

import ic2.core.block.wiring.TileEntityElectricBlock;

public class TileEntityAuratonMFE extends TileEntityElectricBlock {
    public TileEntityAuratonMFE(){
        super(6,32768, 650_000_000);
        this.chargeSlot.setTier(5);
        this.dischargeSlot.setTier(5);
    }
}