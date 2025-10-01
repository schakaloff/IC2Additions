package com.ic2additions.recipes;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MolecularAssemblerRecipesHandler {
    private MolecularAssemblerRecipesHandler() {}

    public static final class Recipe {
        public final ItemStack input;
        public final int totalEU;
        public final ItemStack output;

        public Recipe(ItemStack input, int totalEU, ItemStack output) {
            this.input   = input.copy();
            this.totalEU = totalEU;
            this.output  = output.copy();
        }
    }

    public static final List<Recipe> RECIPES = new ArrayList<>();

    public static synchronized void add(ItemStack input, int totalEU, ItemStack output) {
        Objects.requireNonNull(input,  "input");
        Objects.requireNonNull(output, "output");
        if (input.isEmpty() || output.isEmpty() || totalEU <= 0) return;
        RECIPES.add(new Recipe(input, totalEU, output));
    }
    public static synchronized Recipe find(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        for (Recipe r : RECIPES) {
            if (stack.getItem() == r.input.getItem()
                    && stack.getMetadata() == r.input.getMetadata()) {
                return r;
            }
        }
        return null;
    }
    public static synchronized int getRecipeCount() {
        return RECIPES.size();
    }
    public static synchronized List<Recipe> getAllRecipes() {
        return new ArrayList<>(RECIPES);
    }
}
