package com.ic2additions.event;

import com.ic2additions.util.Reference;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerConfigChange {
    @SubscribeEvent
    public static void on(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Reference.MODID)) {
            ConfigManager.sync(Reference.MODID, Type.INSTANCE);
        }
    }
}
