package com.ic2additions.objects.items.armor;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemUltimateEnergyPack extends ItemBaseEnergyPack{
    public ItemUltimateEnergyPack(){
        super("ultimate_pack", 60_000_000, 70000.0, 4);
    }
    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }
}
