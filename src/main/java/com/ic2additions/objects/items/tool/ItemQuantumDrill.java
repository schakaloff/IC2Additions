package com.ic2additions.objects.items.tool;

import ic2.core.item.tool.HarvestLevel;

public class ItemQuantumDrill extends BaseDrill {
    public ItemQuantumDrill(String name){
        super(
                name,
                5000,
                HarvestLevel.Iridium,
                600_000,
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
                true, 3, 200.0D,
                true, 6, 6, 150.0D,
                true, 13, 13, 200.0D
        );
    }
}
