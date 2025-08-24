package com.ic2additions.config;
import net.minecraftforge.common.config.Config;
import static com.ic2additions.util.Reference.MODID;

@Config(modid = MODID, category = "")

public class CommonConfig {
    @Config.RequiresMcRestart
    @Config.Comment("Energy storage config")

    public static EnergyStorage energystorage = new EnergyStorage();
    public static class EnergyStorage{
        public ConfigEntryEnergyStorage bettermfsu = new ConfigEntryEnergyStorage(36e7, 18_432, 4);
    }
}

