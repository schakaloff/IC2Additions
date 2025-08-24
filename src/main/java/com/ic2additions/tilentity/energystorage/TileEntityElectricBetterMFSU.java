package com.ic2additions.tilentity.energystorage;
import com.ic2additions.config.CommonConfig;
import com.ic2additions.config.CommonConfig.EnergyStorage;
import com.ic2additions.tilentity.TileEntityEnergyStorageBase;

public class TileEntityElectricBetterMFSU extends TileEntityEnergyStorageBase {
    public TileEntityElectricBetterMFSU() {
        super(CommonConfig.energystorage.bettermfsu);
    }
}
