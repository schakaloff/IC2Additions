package com.ic2additions.objects.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBaseEpic extends ItemBase{
    public ItemBaseEpic(String name){
        super(name);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.EPIC;
    }
}