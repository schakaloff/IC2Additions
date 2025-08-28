package com.ic2additions.tilentity;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.gui.dynamic.DynamicContainer;
import ic2.core.gui.dynamic.DynamicGui;
import ic2.core.gui.dynamic.GuiParser;
import ic2.core.gui.dynamic.IGuiValueProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;

public class TileEntityMolecularAssembler extends TileEntityElectricMachine implements IHasGui, IGuiValueProvider {
    public TileEntityMolecularAssembler() {
        super(100000,12);
    }

    @Override
    public ContainerBase<? extends TileEntityMolecularAssembler> getGuiContainer(EntityPlayer player) {
        return DynamicContainer.create(this, player, GuiParser.parse(this.teBlock));
    }

    @Override
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return DynamicGui.create(this, entityPlayer, GuiParser.parse(this.teBlock));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {

    }

    @Override
    public double getGuiValue(String s) {
        return 0;
    }
}
