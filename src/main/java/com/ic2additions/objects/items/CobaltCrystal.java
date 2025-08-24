package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

public class CobaltCrystal extends ItemBase implements IElectricItem {
    private static final double CAPACITY = 2_000_000;
    private static final double TRANSFER = 2048D;
    private static final int TIER = 3;

    public CobaltCrystal(String name){
        super(name);
        setMaxStackSize(1);
        setNoRepair();
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
    }
    @Override public int getTier(ItemStack stack){ return TIER; }
    @Override public double getMaxCharge(ItemStack stack){ return CAPACITY; }
    @Override public double getTransferLimit(ItemStack stack){ return TRANSFER; }
    @Override public boolean canProvideEnergy(ItemStack stack){ return true; }
    @Override public boolean showDurabilityBar(ItemStack stack){ return true; }
    @Override public double getDurabilityForDisplay(ItemStack stack) {
        double charge = ElectricItem.manager.getCharge(stack);
        return 1.0 - (charge / CAPACITY);
    }
}
