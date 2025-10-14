package com.ic2additions.jei;
import com.ic2additions.gui.TransparentDynamicGUI;
import com.ic2additions.init.TesRegistry;
import com.ic2additions.recipes.breeding.BreederReactorRecipesHandler;

import ic2.api.recipe.IRecipeInput;
import ic2.core.block.ITeBlock;
import ic2.core.init.Localization;
import ic2.core.recipe.RecipeInputOreDict;
import ic2.jeiIntegration.recipe.machine.DynamicCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeCategory;
import ic2.jeiIntegration.recipe.machine.IORecipeWrapper;
import ic2.jeiIntegration.recipe.machine.IRecipeWrapperGenerator;

import mezz.jei.api.JEIPlugin;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JEIPlugin
public final class JeiBreederReactor implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeClickArea(TransparentDynamicGUI.class, 16, 35, 16, 16, new String[]{ TesRegistry.breeding_reactor.getName() });

        BreederReactorCategory category =
                new BreederReactorCategory(registry.getJeiHelpers().getGuiHelper());

        addMachineRecipes(registry, category, BreederReactorRecipeWrapper.RECIPE_WRAPPER);
    }

    private <T> void addMachineRecipes(IModRegistry registry,
                                       IORecipeCategory<T> category,
                                       IRecipeWrapperGenerator<T> wrappergen) {
        registry.addRecipeCategories(new IRecipeCategory[]{ category });
        registry.addRecipes((Collection) wrappergen.getRecipeList(category), category.getUid());
        registry.addRecipeCatalyst(category.getBlockStack(), new String[]{ category.getUid() });
    }

    protected static final class BreederReactorRecipeWrapper extends IORecipeWrapper {
        public static final IRecipeWrapperGenerator<Object> RECIPE_WRAPPER = new IRecipeWrapperGenerator<Object>() {
            @Override
            public List<IRecipeWrapper> getRecipeList(IORecipeCategory<Object> category) {
                List<IRecipeWrapper> recipes = new ArrayList<>();
                for (BreederReactorRecipesHandler.Recipe r : BreederReactorRecipesHandler.getAllRecipes()) {
                    recipes.add(new BreederReactorRecipeWrapper(r, category));
                }
                return recipes;
            }
        };

        private final String inputText;
        private final String outputText;
        private final String totalEuText;
        private final String timeText;

        BreederReactorRecipeWrapper(BreederReactorRecipesHandler.Recipe r, IORecipeCategory<?> category) {
            super(wrap(r.input), Collections.singletonList(r.output), category);

            String resolvedInput;
            IRecipeInput wrapped = wrap(r.input);
            if (!wrapped.getInputs().isEmpty()) {
                resolvedInput = wrapped.getInputs().get(0).getDisplayName();
            } else if (wrapped instanceof RecipeInputOreDict) {
                resolvedInput = ((RecipeInputOreDict) wrapped).input;
            } else {
                resolvedInput = "Empty " + wrapped.getClass().getSimpleName();
            }

            int seconds = Math.max(0, r.totalTime / 20);
            String mmss = String.format("%d:%02d", seconds / 60, seconds % 60);

            this.inputText   = Localization.translate("gui.ic2additions.inputBR")      + ' ' + resolvedInput;
            this.outputText  = Localization.translate("gui.ic2additions.outputItemBR") + ' ' + r.output.getDisplayName();
            this.totalEuText = String.format("%s %,d %s", Localization.translate("gui.ic2additions.costBR"), r.totalEU, Localization.translate("ic2.generic.text.EU"));
            this.timeText    = Localization.translate("gui.ic2additions.countdownBR")  + ' ' + mmss;
        }

        private static IRecipeInput wrap(final ItemStack stack) {
            return new IRecipeInput() {
                @Override
                public boolean matches(ItemStack other) {
                    if (other == null) return false;
                    return ItemStack.areItemsEqual(stack, other) && stack.getMetadata() == other.getMetadata();
                }

                @Override
                public int getAmount() {
                    return stack.getCount();
                }

                @Override
                public List<ItemStack> getInputs() {
                    return java.util.Collections.singletonList(stack);
                }
            };
        }

        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            int x = 5;
            mc.fontRenderer.drawSplitString(this.inputText,  42, x, recipeWidth - 42, 0xFFFFFF);
            x += mc.fontRenderer.getWordWrappedHeight(this.inputText,  recipeWidth - 42) + 5;

            mc.fontRenderer.drawSplitString(this.outputText, 42, x, recipeWidth - 42, 0xFFFFFF);
            x += mc.fontRenderer.getWordWrappedHeight(this.outputText, recipeWidth - 42) + 5;

            mc.fontRenderer.drawString(this.totalEuText, 42, x, 0xFFFFFF);
            x += 10;

            mc.fontRenderer.drawString(this.timeText, 42, x, 0xFFFFFF);
        }
    }

    protected static final class BreederReactorCategory extends CustomHeightDynamicCategory<Object> {
        BreederReactorCategory(IGuiHelper guiHelper) {
            // Height 63 like your assembler category; tweak if your JEI gui art needs more space.
            super(TesRegistry.breeding_reactor, new Object(), guiHelper, 63);
        }

        @Override
        protected int getProcessSpeed(String name) {
            if ("progress".equals(name)) return 50;
            return super.getProcessSpeed(name);
        }
    }

    public static class CustomHeightDynamicCategory<T> extends DynamicCategory<T> {
        protected final int height;

        public CustomHeightDynamicCategory(ITeBlock block, T recipeManager, IGuiHelper guiHelper, int height) {
            super(block, recipeManager, guiHelper);
            this.height = height;
        }

        @Override
        public int getHeight() {
            return this.height;
        }
    }
}
