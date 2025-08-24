package com.ic2additions.util;


import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonRegistry {
    public static void registerEventHandler(Object object) {
        MinecraftForge.EVENT_BUS.register(object);
    }

    public static void unregisterEventHandler(Object object) {
        MinecraftForge.EVENT_BUS.unregister(object);
    }

    public static void registerEventHandler(Object ... objects) {
        for (Object object : objects) {
            MinecraftForge.EVENT_BUS.register(object);
        }
    }

    public static void unregisterEventHandler(Object ... objects) {
        for (Object object : objects) {
            MinecraftForge.EVENT_BUS.unregister(object);
        }
    }

    public static void registerGuiHandler(Object mod, IGuiHandler handler) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler);
    }

    public static void registerSmelting(ItemStack input, ItemStack output, float xp) {
        GameRegistry.addSmelting((ItemStack)input, (ItemStack)output, (float)xp);
    }

    public static void registerWorldGeneration(IWorldGenerator generator, int weight) {
        GameRegistry.registerWorldGenerator((IWorldGenerator)generator, (int)weight);
    }
}

