package JEI;

import com.ic2additions.recipes.MolecularAssemblerRecipesHandler;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class MolecularAssemblerRecipeMaker {

    private MolecularAssemblerRecipeMaker() {}

    public static List<MolecularAssemblerJeiRecipe> getRecipes() {
        List<MolecularAssemblerJeiRecipe> out = new ArrayList<>();
        for (MolecularAssemblerRecipesHandler.Recipe r : MolecularAssemblerRecipesHandler.getAllRecipes()) {
            ItemStack in = r.input;
            ItemStack outStack = r.output;
            int eu = r.totalEU;
            if (!in.isEmpty() && !outStack.isEmpty() && eu > 0) {
                out.add(new MolecularAssemblerJeiRecipe(in, outStack, eu));
            }
        }
        return out;
    }
}
