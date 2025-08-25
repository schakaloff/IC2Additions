package com.ic2additions.util;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UCreativeTab extends CreativeTabs {
    private Item item = null;
    private int metadata = 0;

    public UCreativeTab(String modid, String name) {
        this(new ResourceLocation(modid, name));
    }

    public UCreativeTab(ResourceLocation location) {
        super(location.toString());
    }

    public void setIcon(Block block) {
        this.setIcon(block, 0);
    }

    public void setIcon(Block block, int metadata) {
        this.setIcon(Item.getItemFromBlock(block), metadata);
    }

    public void setIcon(Item item) {
        this.setIcon(item, 0);
    }

    public void setIcon(Item item, int metadata) {
        this.item = item;
        this.metadata = metadata;
    }

    @SideOnly(value=Side.CLIENT)
    public ItemStack getTabIconItem() {
        if (this.item == null) {
            return new ItemStack(Items.ELYTRA);
        }
        return new ItemStack(this.item, 1, this.metadata);
    }

}

