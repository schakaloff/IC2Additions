package com.ic2additions.objects.items.reactor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class ItemReactorThorium extends ItemReactorUranium implements IReactorComponent, ICustomDamageItem {
    public ItemReactorThorium(String name, int cells) {
        super((ItemName) null, cells, 30000);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    // Run cooler than uranium: uranium uses triangular*4 then calls this.
    // Scale down final heat to ~75% of uranium’s.
    @Override
    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        // 3/4 of uranium’s heat
        return (heat * 3) / 4;
    }

    // Slight EU buff per pulse
    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(1.2F); // uranium is 1.0F
        return true;
    }

    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        switch (this.numberOfCells) {
            case 1: return new ItemStack(ItemInit.DEPLETED_THORIUM);
            case 2: return new ItemStack(ItemInit.DEPLETED_THORIUM_DUAL);
            case 4: return new ItemStack(ItemInit.DEPLETED_THORIUM_QUAD);
            default: throw new RuntimeException("invalid cell count: " + this.numberOfCells);
        }
    }
}