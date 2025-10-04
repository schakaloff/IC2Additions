package com.ic2additions.recipes;

import com.ic2additions.init.ItemInit;
import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;


public class MolecularAssemblerRecipes {
    private MolecularAssemblerRecipes() {
    }

    public static void init() {
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.REDSTONE, 1), 2000, new ItemStack(Items.GLOWSTONE_DUST, 1));

        MolecularAssemblerRecipesHandler.add(IC2Items.getItem("crafting", "industrial_diamond"), 1_000_000, IC2Items.getItem("crafting", "industrial_diamond"));

        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.IRON_INGOT, 1), 9_000_000, IC2Items.getItem("misc_resource", "iridium_ore"));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.SKULL, 1), 250_000_000, new ItemStack(Items.NETHER_STAR, 1));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Items.GLOWSTONE_DUST, 1), 1_000_000, new ItemStack(ItemInit.SANARIUM_SHARD));
        MolecularAssemblerRecipesHandler.add(new ItemStack(Blocks.GLOWSTONE, 1), 9_000_000, new ItemStack(ItemInit.SANARIUM));

        MolecularAssemblerRecipesHandler.add(new ItemStack(ItemInit.SANARIUM, 1), 20_000_000, new ItemStack(ItemInit.PHOTON));
        MolecularAssemblerRecipesHandler.add(IC2Items.getItem("misc_resource", "iridium_ore"), 35_000_000, new ItemStack(ItemInit.FULLERITE));



    }
}
