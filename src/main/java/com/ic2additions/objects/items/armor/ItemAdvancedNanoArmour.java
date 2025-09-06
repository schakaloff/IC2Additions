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

public class ItemAdvancedNanoArmour extends ItemArmorElectric {
    public ItemAdvancedNanoArmour(String name, EntityEquipmentSlot slot){
        super(null, "advanced_nano_suit", slot, 3_000_000, 4096D, 4);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return "ic2additions:textures/armor/advanced_nano_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }
    @Override
    public double getDamageAbsorptionRatio() {
        return 0.70;
    }

    @Override
    public int getEnergyPerDamage() {
        return 20000;
    }

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
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
