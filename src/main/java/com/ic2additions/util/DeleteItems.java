package com.ic2additions.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class DeleteItems {

    private static final String[] TO_REMOVE = {
            "quantum_helmet",
            "quantum_chestplate",
            "quantum_leggings",
            "quantum_boots"
    };

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        for (String id : TO_REMOVE) {
            ResourceLocation rl = new ResourceLocation("ic2", id);
            Item item = event.getRegistry().getValue(rl);
            if (item != null) {
                event.getRegistry().register(new Item().setRegistryName(rl));
            }
        }
    }

    @SubscribeEvent
    public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistryModifiable<IRecipe> registry = (IForgeRegistryModifiable<IRecipe>) event.getRegistry();
        List<ResourceLocation> toRemove = new ArrayList<>();
        for (IRecipe recipe : event.getRegistry().getValuesCollection()) {
            ItemStack output = recipe.getRecipeOutput();
            if (!output.isEmpty() && output.getItem().getRegistryName() != null) {
                String outputName = output.getItem().getRegistryName().toString();
                for (String id : TO_REMOVE) {
                    if (outputName.equals("ic2:" + id)) {
                        toRemove.add(recipe.getRegistryName());
                        break;
                    }
                }
            }
        }
        for (ResourceLocation rl : toRemove) {
            registry.remove(rl);
        }
    }
}
