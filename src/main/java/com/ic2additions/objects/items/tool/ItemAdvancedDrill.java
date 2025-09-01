package com.ic2additions.objects.items.tool;

import ic2.core.item.tool.HarvestLevel;

public class ItemAdvancedDrill extends BaseDrill{
    public ItemAdvancedDrill(String name){
        super(
                name,
                5000,
                HarvestLevel.Diamond,
                200_000,
                3_200,
                3,
                16.0f,
                30,
                120,
                120.0D,
                2,
                true,
                true,
                true,
                1,
                120.0D
        );
    }
}
