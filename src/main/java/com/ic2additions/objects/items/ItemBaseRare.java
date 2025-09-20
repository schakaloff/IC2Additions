package com.ic2additions.objects.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBaseRare extends ItemBase{
    public ItemBaseRare(String name){
        super(name);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }
}