package com.ic2additions.objects.items.armor;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemUnrealEnergyPack extends ItemBaseEnergyPack{
    public ItemUnrealEnergyPack(){
        super("unreal_pack", 350_000_000, 200_000, 5);
    }
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }
}
