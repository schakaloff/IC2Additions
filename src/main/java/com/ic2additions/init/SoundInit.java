package com.ic2additions.init;

import com.ic2additions.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public final class SoundInit {
    public static SoundEvent COMMUNISM;
    public static SoundEvent MOLECULAR_PUNCH;
    public static SoundEvent BREEDING_REACTOR;

    private SoundInit() {}

    private static SoundEvent create(String path) {
        ResourceLocation rl = new ResourceLocation(Reference.MODID, path);
        SoundEvent ev = new SoundEvent(rl);
        ev.setRegistryName(rl);
        return ev;
    }

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> e) {
        IForgeRegistry<SoundEvent> r = e.getRegistry();
        r.register(COMMUNISM = create("record.communism"));
        r.register(MOLECULAR_PUNCH = create("molecular.punch"));
//        r.register(BREEDING_REACTOR = create("breeding.reactor"));
    }
}
