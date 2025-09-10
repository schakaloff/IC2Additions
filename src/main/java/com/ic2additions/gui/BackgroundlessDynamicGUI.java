package com.ic2additions.gui;

import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser.GuiNode;
import ic2.core.gui.dynamic.GuiParser;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import ic2.core.ContainerBase;
import ic2.core.gui.GuiElement;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser.GuiNode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public class BackgroundlessDynamicGUI<T extends ContainerBase<? extends IInventory>> extends DynamicGui<T> {

    public static <T extends IInventory> BackgroundlessDynamicGUI<ContainerBase<T>> create(T base, EntityPlayer player, GuiNode guiNode) {
        DynamicContainer container = DynamicContainer.create(base, player, guiNode);
        return new BackgroundlessDynamicGUI<>(player, container, guiNode);
    }

    protected BackgroundlessDynamicGUI(EntityPlayer player, T container, GuiNode guiNode) {
        super(player, container, guiNode);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mouseX -= this.guiLeft;
        mouseY -= this.guiTop;
        GlStateManager.color(1F, 1F, 1F, 1F);

        for (GuiElement<?> element : this.elements) {
            if (!element.isEnabled()) continue;
            drawElement(element, mouseX, mouseY);
        }
    }

    protected void drawElement(GuiElement<?> element, int mouseX, int mouseY) {
        element.drawBackground(mouseX, mouseY);
    }

    public int getLeft() {
        return this.guiLeft;
    }

    public int getTop() {
        return this.guiTop;
    }
}
