package com.ic2additions.recipes;

import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Recipes {
    public static final ItemStack plcar = IC2Items.getItem("crafting","carbon_plate");
    public static final ItemStack coins = IC2Items.getItem("crafting","rubber");

    public static void addCraftingRecipes(){
        addShapedRecipe((plcar), "III","IPI","III", 'I', new ItemStack(Items.BOOK), 'P', coins);
    }

    private static void addShapedRecipe(ItemStack output, Object... input){
        ic2.api.recipe.Recipes.advRecipes.addRecipe(output,input);
    }

    public static void addMachineRecipe(){
        IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;
        addCompressorsRecipe(input.forStack(new ItemStack(Items.SLIME_BALL),9), new ItemStack(Block.getBlockById(165)));
        addMaceratorRecipe(input.forStack(plcar), IC2Items.getItem("dust", "coal"));
    }

    private static void addCompressorsRecipe(IRecipeInput input, ItemStack output){
        ic2.api.recipe.Recipes.compressor.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[]{output});
    }

    private static void addMaceratorRecipe(IRecipeInput input, ItemStack output){
        ic2.api.recipe.Recipes.compressor.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[]{output});
    }
}
