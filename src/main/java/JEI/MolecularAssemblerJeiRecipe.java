package JEI;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.Collections;

public class MolecularAssemblerJeiRecipe implements IRecipeWrapper {

    private final ItemStack input;
    private final ItemStack output;
    private final int totalEU;

    public MolecularAssemblerJeiRecipe(ItemStack input, ItemStack output, int totalEU) {
        this.input = input.copy();
        this.output = output.copy();
        this.totalEU = totalEU;
    }

    public int getTotalEU() {
        return totalEU;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, Collections.singletonList(input));
        ingredients.setOutputs(VanillaTypes.ITEM, Collections.singletonList(output));
    }
}
