package com.ic2additions.objects.items.armor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.Reference;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBaseEnergyPack extends ItemArmorElectric {
    protected final String name;
    protected ItemBaseEnergyPack(String name, double maxCharge, double transferLimit, int tier) {
        super(null, name, EntityEquipmentSlot.CHEST, maxCharge, transferLimit, tier);
        this.name = name;
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }
    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        return true;
    }
    @Override
    public double getDamageAbsorptionRatio() {
        return 0.0D;
    }

    @Override
    public int getEnergyPerDamage() {
        return 0;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return Reference.MODID + ":textures/armor/" + name + ".png";
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.UNCOMMON;
    }

    @Override
    public String getUnlocalizedName() {
        return "item." + Reference.MODID + "." + getRegistryName().getResourcePath();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedNameInefficiently(ItemStack stack) {
        return getUnlocalizedName();
    }
}
