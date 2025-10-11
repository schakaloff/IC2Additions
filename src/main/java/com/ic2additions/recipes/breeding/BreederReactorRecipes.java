package com.ic2additions.recipes.breeding;

import com.ic2additions.init.ItemInit;
import net.minecraft.item.ItemStack;

public class BreederReactorRecipes {
    private BreederReactorRecipes() {}

    public static void init(){
        // Takes 35 minutes (35 * 60 * 20 = 42,000 ticks)
        BreederReactorRecipesHandler.add(new ItemStack(ItemInit.PLUTONIUM_ROD), 1_000_000, 36_000, new ItemStack(ItemInit.NEPTUNIUM_ROD));
        BreederReactorRecipesHandler.add(new ItemStack(ItemInit.PLUTONIUM_DUAL), 1_000_000, 48_000, new ItemStack(ItemInit.NEPTUNIUM_DUAL_ROD));
        BreederReactorRecipesHandler.add(new ItemStack(ItemInit.PLUTONIUM_QUAD), 1_000_000, 60_000, new ItemStack(ItemInit.NEPTUNIUM_QUAD_ROD));


    }
}
