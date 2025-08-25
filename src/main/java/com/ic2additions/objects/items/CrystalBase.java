package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CrystalBase extends ItemBase implements IElectricItem {
    private final double capacity;
    private final double transfer;
    private final int tier;
    private final boolean provideEnergy;

    public CrystalBase(String name, double capacity, double transfer, int tier, boolean provideEnergy) {
        super(name);
        this.capacity = capacity;
        this.transfer = transfer;
        this.tier = tier;
        this.provideEnergy = provideEnergy;

        setMaxStackSize(1);
        setNoRepair();
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
    }

    @Override public boolean canProvideEnergy(ItemStack stack) { return provideEnergy; }
    @Override public double getMaxCharge(ItemStack stack)      { return capacity; }
    @Override public int    getTier(ItemStack stack)           { return tier; }
    @Override public double getTransferLimit(ItemStack stack)  { return transfer; }
    @Override public boolean showDurabilityBar(ItemStack stack) { return true; }
    @Override public double getDurabilityForDisplay(ItemStack stack) {
        double charge = ElectricItem.manager.getCharge(stack);
        return 1.0 - (charge / capacity);
    }
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this));
        ItemStack full = new ItemStack(this);
        ElectricItem.manager.charge(full, capacity, Integer.MAX_VALUE, true, false);
        items.add(full);
    }
}