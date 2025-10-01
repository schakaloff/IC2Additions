package JEI;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class MolecularAssemblerCategory implements IRecipeCategory<MolecularAssemblerJeiRecipe> {

    private final String uid;
    private final String title;
    private final IDrawable background;
    private final IDrawable icon;

    public MolecularAssemblerCategory(IGuiHelper guiHelper, String uid) {
        this.uid = uid;
        this.title = I18n.format("ic2additions.jei.molecular_assembler"); // add lang key if you care about words
        this.background = guiHelper.createBlankDrawable(150, 60);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(net.minecraft.init.Blocks.BEACON)); // TODO replace with your assembler’s ItemStack
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getModName() {
        return "";
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MolecularAssemblerJeiRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();

        // Slots
        // input @ (20, 21)
        // output @ (110, 21)
        stacks.init(0, true, 20, 21);
        stacks.init(1, false, 110, 21);

        stacks.set(ingredients);
    }

    @Override
    public void drawExtras(Minecraft mc) {
        // no static extras
    }

    public void draw(MolecularAssemblerJeiRecipe recipe, Minecraft minecraft, int mouseX, int mouseY) {
        String eu = String.format("%,d EU", recipe.getTotalEU());
        minecraft.fontRenderer.drawString(I18n.format("ic2additions.jei.energy"), 20, 6, 0x404040);
        minecraft.fontRenderer.drawString(eu, 20, 46, 0x404040);
    }
}
