package com.ic2additions.objects.items.reactor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class ItemReactorNeptunium extends ItemReactorUranium implements IReactorComponent, ICustomDamageItem {
    public ItemReactorNeptunium(String name, int cells) {
        super((ItemName) null, cells, 15000);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }
    @Override
    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return (heat * 5) / 4; // наоборот, горячее чем расчетное
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) reactor.addOutput(1.5F);
        return true;
    }

    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (reactor.produceEnergy()) {
            int basePulses = 1 + this.numberOfCells / 2;

            for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
                if (!heatRun) {
                    for (int i = 0; i < basePulses; i++) {
                        this.acceptUraniumPulse(stack, reactor, stack, x, y, x, y, heatRun);
                    }
                } else {
                    int heat = triangularNumber(basePulses) * 5; // горячее урана
                    heat = this.getFinalHeat(stack, reactor, x, y, heat);
                    reactor.addHeat(heat);
                }
            }

            if (!heatRun) this.applyCustomDamage(stack, 1, null);
        }
    }

    @Override
    protected ItemStack getDepletedStack(ItemStack stack, IReactor reactor) {
        switch (this.numberOfCells) {
            case 1: return new ItemStack(ItemInit.DEPLETED_NEPTUNIUM);
            case 2: return new ItemStack(ItemInit.DEPLETED_NEPTUNIUM_DUAL);
            case 4: return new ItemStack(ItemInit.DEPLETED_NEPTUNIUM_QUAD);
            default: throw new RuntimeException("invalid cell count: " + this.numberOfCells);
        }
    }
}
