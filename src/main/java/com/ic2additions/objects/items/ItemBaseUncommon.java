package com.ic2additions.objects.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemBaseUncommon extends ItemBase{
    public ItemBaseUncommon(String name){
        super(name);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.UNCOMMON;
    }
}
