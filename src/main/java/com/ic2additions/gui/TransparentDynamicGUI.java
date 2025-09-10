package com.ic2additions.gui;

import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.Image;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class TransparentDynamicGUI<T extends ContainerBase<? extends IInventory>> extends BackgroundlessDynamicGUI<T> {

    public static <T extends IInventory> TransparentDynamicGUI<ContainerBase<T>> create(T base, EntityPlayer player, GuiParser.GuiNode guiNode) {
        DynamicContainer container = DynamicContainer.create(base, player, guiNode);
        return new TransparentDynamicGUI<>(player, container, guiNode);
    }

    protected TransparentDynamicGUI(EntityPlayer player, T container, GuiParser.GuiNode guiNode) {
        super(player, container, guiNode);
    }

    @Override
    protected void drawElement(GuiElement<?> element, int mouseX, int mouseY) {
        boolean isImage = element instanceof Image;
        if (isImage) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
            );
        }

        super.drawElement(element, mouseX, mouseY);

        if (isImage) {
            GlStateManager.disableBlend();
        }
    }
}
