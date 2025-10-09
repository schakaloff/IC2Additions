package com.ic2additions.recipes.breeding;

import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BreederReactorRecipesHandler {
    private BreederReactorRecipesHandler() {}

    // === Inner class for a single recipe ===
    public static final class Recipe {
        public final ItemStack input;
        public final int totalEU;
        public final int totalTime; // in ticks
        public final ItemStack output;

        public Recipe(ItemStack input, int totalEU, int totalTime, ItemStack output) {
            this.input = input.copy();
            this.totalEU = totalEU;
            this.totalTime = totalTime;
            this.output = output.copy();
        }
    }

    // === Recipe registry ===
    private static final List<Recipe> RECIPES = new ArrayList<>();

    /** Adds a new recipe */
    public static synchronized void add(ItemStack input, int totalEU, int totalTime, ItemStack output) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(output, "output");
        if (input.isEmpty() || output.isEmpty() || totalEU <= 0 || totalTime <= 0) return;
        RECIPES.add(new Recipe(input, totalEU, totalTime, output));
    }

    /** Finds a recipe that matches a given input item */
    public static synchronized Recipe find(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;
        for (Recipe r : RECIPES) {
            if (stack.getItem() == r.input.getItem() &&
                    stack.getMetadata() == r.input.getMetadata()) {
                return r;
            }
        }
        return null;
    }

    /** Number of recipes */
    public static synchronized int getRecipeCount() {
        return RECIPES.size();
    }

    /** Get all registered recipes */
    public static synchronized List<Recipe> getAllRecipes() {
        return new ArrayList<>(RECIPES);
    }
}
