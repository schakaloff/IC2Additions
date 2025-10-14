package com.ic2additions.objects.items.reactor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class ItemReactorPlutonium extends ItemReactorUranium implements IReactorComponent, ICustomDamageItem {
    public ItemReactorPlutonium(String name, int cells) {
        super((ItemName) null, cells, 55000);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(1.1F);
        return true;
    }

    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        switch (this.numberOfCells) {
            case 1: return new ItemStack(ItemInit.DEPLETED_PLUTONIUM);
            case 2: return new ItemStack(ItemInit.DEPLETED_PLUTONIUM_DUAL);
            case 4: return new ItemStack(ItemInit.DEPLETED_PLUTONIUM_QUAD);
            default: throw new RuntimeException("invalid cell count: " + this.numberOfCells);
        }
    }
}