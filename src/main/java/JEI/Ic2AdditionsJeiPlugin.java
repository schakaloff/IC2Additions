package JEI;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import java.util.List;

@JEIPlugin
public class Ic2AdditionsJeiPlugin implements IModPlugin {

    public static final String UID_MOLECULAR_ASSEMBLER = "ic2additions:molecular_assembler";

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        //registry.addRecipeCategories(new MolecularAssemblerCategory(guiHelper, UID_MOLECULAR_ASSEMBLER));
    }


    @Override
    public void register(IModRegistry registry) {
        // Build wrappers from your handler’s recipes
        List<MolecularAssemblerJeiRecipe> recipes = MolecularAssemblerRecipeMaker.getRecipes();
        registry.addRecipes(recipes, UID_MOLECULAR_ASSEMBLER);

        // Optional: show your machine as a catalyst so clicking it opens the category.
        // Replace the stack below with your actual Assembler block/item stack.
        ItemStack assemblerStack = ItemStack.EMPTY; // TODO: put your Molecular Assembler ItemStack here
        if (!assemblerStack.isEmpty()) {
            registry.addRecipeCatalyst(assemblerStack, UID_MOLECULAR_ASSEMBLER);
        }

        // No GUI click area registered since your GUI is a dynamic IC2 one without a stable class.
        // If you have a concrete Gui class later, call:
        // registry.addRecipeClickArea(YourGuiClass.class, x, y, w, h, UID_MOLECULAR_ASSEMBLER);
    }
}
