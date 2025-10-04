package com.ic2additions.util;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public class DeleteItems {
    public static void init(RegistryEvent.Register<Item> event){
        String[] toRemove = {
//                "quantum_helmet",
//                "quantum_chestplate",
//                "quantum_leggings",
//                "quantum_boots"
        };
        for (String id : toRemove) {
            ResourceLocation rl = new ResourceLocation("ic2", id);
            Item item = event.getRegistry().getValue(rl);
            if (item != null) {
                event.getRegistry().register(new Item().setRegistryName(rl));
            }
        }
    }
}
