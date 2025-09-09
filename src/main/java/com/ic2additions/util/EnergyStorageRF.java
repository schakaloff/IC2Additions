package com.ic2additions.util;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageRF extends EnergyStorage {
    public EnergyStorageRF(int capacity) {
        super(capacity);
    }

    @Override
    public boolean canExtract() {
        return true;
    }

    @Override
    public boolean canReceive() {
        return false; // only EU->RF conversion adds energy
    }
}