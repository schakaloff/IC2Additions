package com.ic2additions.objects.items.armor;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemAdvancedEnergyPack extends ItemBaseEnergyPack{
    public ItemAdvancedEnergyPack(){
        super("advanced_pack", 3000000.0, 30000.0, 4);
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }
}
