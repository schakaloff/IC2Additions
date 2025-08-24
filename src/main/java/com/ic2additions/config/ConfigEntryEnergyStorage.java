package com.ic2additions.config;
import net.minecraftforge.common.config.Config.Name;
public class ConfigEntryEnergyStorage {
    @Name("Energy Capacity")
    public double capacity;

    @Name("Energy Output")
    public double output;

    @Name("Energy Tier")
    public int outputTier;

    public ConfigEntryEnergyStorage(double capacity, double output, int outputTier) {
        this.capacity = capacity;
        this.output = output;
        this.outputTier = outputTier;
    }
}
