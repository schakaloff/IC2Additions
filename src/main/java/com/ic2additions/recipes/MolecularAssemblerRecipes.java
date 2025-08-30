package com.ic2additions.recipes;

import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MolecularAssemblerRecipes {
    private MolecularAssemblerRecipes() {}

    public static void init() {
        //MolecularAssemblerRecipesHandler.add(new ItemStack(Items.GOLD_INGOT, 1), 250_000, new ItemStack(Items.DIAMOND, 1));
        MolecularAssemblerRecipesHandler.add(IC2Items.getItem("crafting", "industrial_diamond"), 1_000_000, new ItemStack(Items.DIAMOND, 1));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.IRON_INGOT, 1), 9_000_000, IC2Items.getItem("misc_resource", "iridium_ore"));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.SKULL, 1), 250_000_000, new ItemStack(Items.NETHER_STAR, 1));
    }
}
