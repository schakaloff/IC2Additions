package com.ic2additions.objects.items.tool;

import ic2.core.item.tool.HarvestLevel;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemNanoDrill extends BaseDrill {
    public ItemNanoDrill(String name){
        super(
                name,
                5000,
                HarvestLevel.Iridium,
                400_000,
                6_400,
                4,
                20.0f,
                45,
                100,
                160.0D,
                3,
                true,
                true,
                true, 1, 120.0D,
                true, 2, 160.0D,
                false, 0, 0.0D,
                true, 6, 6, 150.0D,
                false, 0, 0, 0.0D
        );
    }
    @Override
    public EnumRarity getRarity(ItemStack stack){
        return EnumRarity.RARE;
    }
}
