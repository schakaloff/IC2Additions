package com.ic2additions.recipes.breeding;

import com.ic2additions.init.ItemInit;
import net.minecraft.item.ItemStack;

public class BreederReactorRecipes {
    private BreederReactorRecipes() {}

    public static void init(){
//        // Example: Depleted Uranium Rod -> Plutonium Rod
//        // Takes 35 minutes (35 * 60 * 20 = 42,000 ticks)
        BreederReactorRecipesHandler.add(
                new ItemStack(ItemInit.THORIUM_232), // input
                1_000_000,  // total EU
                42_000,     // time in ticks
                new ItemStack(ItemInit.THORIUM_ROD_SINGLE) // output
        );
//

    }
}
