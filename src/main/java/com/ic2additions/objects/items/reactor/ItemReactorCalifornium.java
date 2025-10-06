package com.ic2additions.objects.items.reactor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.item.reactor.ItemReactorMOX;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class ItemReactorCalifornium extends ItemReactorMOX implements IReactorComponent, ICustomDamageItem {
    public ItemReactorCalifornium(String name, int cells) {
        super((ItemName) null, cells);

        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) {
            float breedereffectiveness = (float)reactor.getHeat() / (float)reactor.getMaxHeat();
            float ReaktorOutput = 8.0F * breedereffectiveness + 2.0F;
            reactor.addOutput(ReaktorOutput);
        }

        return true;
    }
    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        switch (this.numberOfCells) {
            case 1:
                return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM);
            case 2:
                return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_DUAL);
            case 4:
                return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_QUAD);
            default:
                throw new RuntimeException("invalid cell count: " + this.numberOfCells);
        }
    }
}
