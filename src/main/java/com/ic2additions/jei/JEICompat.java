package com.ic2additions.jei;

import com.ic2additions.gui.TransparentDynamicGUI;
import com.ic2additions.init.TesRegistry;
import com.ic2additions.recipes.molecular.MolecularAssemblerRecipesHandler;
import mezz.jei.api.JEIPlugin;
import ic2.api.recipe.IRecipeInput;
import ic2.core.block.ITeBlock;
import ic2.core.init.Localization;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeWrapper;
import ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

@JEIPlugin
public final class JEICompat implements IModPlugin {
    public void register(IModRegistry registry) {
        registry.addRecipeClickArea(TransparentDynamicGUI.class, 23, 48, 10, 15, new String[]{TesRegistry.molecular_assembler.getName()});
        this.addMachineRecipes(registry, (IORecipeCategory)new MolecularAssemblerCategory(registry.getJeiHelpers().getGuiHelper()), (IRecipeWrapperGenerator)MolecularAssemblerRecipeWrapper.RECIPE_WRAPPER);
    }

    private <T> void addMachineRecipes(IModRegistry registry, IORecipeCategory<T> category, IRecipeWrapperGenerator<T> wrappergen) {
        registry.addRecipeCategories(new IRecipeCategory[]{category});
        registry.addRecipes((Collection)wrappergen.getRecipeList(category), category.getUid());
        registry.addRecipeCatalyst((Object)category.getBlockStack(), new String[]{category.getUid()});
    }

    protected static class MolecularAssemblerRecipeWrapper
            extends IORecipeWrapper {
        public static final IRecipeWrapperGenerator<Object> RECIPE_WRAPPER = new IRecipeWrapperGenerator<Object>(){

            public List<IRecipeWrapper> getRecipeList(IORecipeCategory<Object> category) {
                ArrayList<IRecipeWrapper> recipes = new ArrayList<IRecipeWrapper>();
                for (MolecularAssemblerRecipesHandler.Recipe r : MolecularAssemblerRecipesHandler.getAllRecipes()) {
                    recipes.add((IRecipeWrapper)new MolecularAssemblerRecipeWrapper(r, category));
                }
                return recipes;
            }
        };
        protected final String input;
        protected final String output;
        protected final String totalEU;

        MolecularAssemblerRecipeWrapper(MolecularAssemblerRecipesHandler.Recipe r, IORecipeCategory<?> category) {
            super(wrap(r.input), Collections.singletonList(r.output), category);
            String inputText;
            IRecipeInput input = wrap(r.input);
            if (!input.getInputs().isEmpty()) {
                inputText = ((ItemStack)input.getInputs().get(0)).getDisplayName();
            } else if (input instanceof RecipeInputOreDict) {
                inputText = ((RecipeInputOreDict)input).input;
            } else {
                inputText = "Empty " + input.getClass().getSimpleName();
            }
            this.input = Localization.translate((String)"gui.ic2additions.inputMA") + ' ' + inputText;
            this.output = Localization.translate((String)"gui.ic2additions.outputItemMA") + ' ' + r.output.getDisplayName();
            this.totalEU = String.format("%s %,d %s", Localization.translate((String)"gui.ic2additions.costMA"), r.totalEU, Localization.translate((String)"ic2.generic.text.EU"));
        }

        private static IRecipeInput wrap(final ItemStack stack) {
            return new IRecipeInput() {
                public boolean matches(ItemStack other) {
                    if (other == null) return false;
                    return ItemStack.areItemsEqual(stack, other) && stack.getMetadata() == other.getMetadata();
                }
                public int getAmount() {
                    return stack.getCount();
                }
                public List<ItemStack> getInputs() {
                    return java.util.Collections.singletonList(stack);
                }
            };
        }

        public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int space = 5;
            int x = 5;
            minecraft.fontRenderer.drawSplitString(this.input,  42, x, recipeWidth - 42, 0xFFFFFF);
            x += minecraft.fontRenderer.getWordWrappedHeight(this.input,  recipeWidth - 42) + 5;
            minecraft.fontRenderer.drawSplitString(this.output, 42, x, recipeWidth - 42, 0xFFFFFF);
            x += minecraft.fontRenderer.getWordWrappedHeight(this.output, recipeWidth - 42) + 5;
            minecraft.fontRenderer.drawString(this.totalEU, 42, x, 0xFFFFFF);
        }
    }

    protected static class MolecularAssemblerCategory
            extends CustomHeightDynamicCategory<Object> {
        public MolecularAssemblerCategory(IGuiHelper guiHelper) {
            super(TesRegistry.molecular_assembler, new Object(), guiHelper, 63);
        }

        protected int getProcessSpeed(String name) {
            if ("progress".equals(name)) {
                return 50;
            }
            return super.getProcessSpeed(name);
        }
    }

    public static class CustomHeightDynamicCategory<T>
            extends DynamicCategory<T> {
        protected final int height;

        public CustomHeightDynamicCategory(ITeBlock block, T recipeManager, IGuiHelper guiHelper, int height) {
            super(block, recipeManager, guiHelper);
            this.height = height;
        }

        public int getHeight() {
            return this.height;
        }
    }
}

