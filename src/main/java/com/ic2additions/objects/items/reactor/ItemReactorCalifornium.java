package com.ic2additions.objects.items.reactor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ICustomDamageItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.ref.ItemName;
import net.minecraft.item.ItemStack;

public class ItemReactorCalifornium extends ItemReactorUranium implements IReactorComponent, ICustomDamageItem {
    public ItemReactorCalifornium(String name, int cells) {
        super((ItemName) null, cells, 5000);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }
    @Override
    protected int getFinalHeat(ItemStack stack, IReactor reactor, int x, int y, int heat) {
        return heat * 2; // экстремальное топливо, удвоенное тепло
    }

    @Override
    public boolean acceptUraniumPulse(ItemStack stack, IReactor reactor, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) {
            // How hot is the reactor? (0.0 = cold, 1.0 = max heat)
            float heatFactor = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
            // Californium is way stronger than MOX:
            // Instead of 4 * heatFactor + 1, scale more aggressively
            float output = 10.0F * heatFactor + 2.0F;
            reactor.addOutput(output);
        }
        return true;
    }

    @Override
    public void processChamber(ItemStack stack, IReactor reactor, int x, int y, boolean heatRun) {
        if (reactor.produceEnergy()) {
            int basePulses = 3 + this.numberOfCells; // экстремальное количество импульсов

            for (int iteration = 0; iteration < this.numberOfCells; iteration++) {
                if (!heatRun) {
                    for (int i = 0; i < basePulses; i++) {
                        this.acceptUraniumPulse(stack, reactor, stack, x, y, x, y, heatRun);
                    }
                } else {
                    int heat = triangularNumber(basePulses) * 12; // экстремально горячее
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
            case 1: return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM);
            case 2: return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_DUAL);
            case 4: return new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_QUAD);
            default: throw new RuntimeException("invalid cell count: " + this.numberOfCells);
        }
    }
}
