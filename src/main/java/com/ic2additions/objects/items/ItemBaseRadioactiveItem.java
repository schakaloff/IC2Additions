package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.main.IC2Additions;
import ic2.core.IC2Potion;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.type.IRadioactiveItemType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBaseRadioactiveItem extends Item implements IRadioactiveItemType {
    private final int radiationDuration;
    private final int radiationAmplifier;

    public ItemBaseRadioactiveItem(String name, int radiationDuration, int radiationAmplifier){
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(64);

        this.radiationDuration = radiationDuration;
        this.radiationAmplifier = radiationAmplifier;

        ItemInit.ITEMS.add(this);
    }
    @Override
    public int getRadiationDuration() {
        return radiationDuration;
    }

    @Override
    public int getRadiationAmplifier() {
        return radiationAmplifier;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isCurrentItem) {
        if (!(entity instanceof EntityLivingBase)) return;
        EntityLivingBase e = (EntityLivingBase) entity;
        if (!ItemArmorHazmat.hasCompleteHazmat(e)) {
            int ticks = radiationDuration * 20;
            IC2Potion.radiation.applyTo(e, ticks, radiationAmplifier);
        }
    }
}
