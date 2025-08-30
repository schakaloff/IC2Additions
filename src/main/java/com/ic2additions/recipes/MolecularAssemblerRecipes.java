package com.ic2additions.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MolecularAssemblerRecipes {
    private MolecularAssemblerRecipes() {}

    public static void init() {
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.GOLD_INGOT, 1), 250_000, new ItemStack(Items.DIAMOND, 1));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.REDSTONE, 1), 80_000, new ItemStack(Items.GLOWSTONE_DUST, 1));
    }
}
