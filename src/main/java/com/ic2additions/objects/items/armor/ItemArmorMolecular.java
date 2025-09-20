package com.ic2additions.objects.items.armor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.Reference;
import ic2.api.item.ElectricItem;
import ic2.api.item.HudMode;
import ic2.api.item.IHazmatLike;
import ic2.api.item.IItemHudProvider;
import ic2.core.item.armor.ItemArmorElectric;
import ic2.core.item.armor.jetpack.IJetpack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorMolecular extends ItemArmorElectric implements IJetpack, IItemHudProvider, IHazmatLike {

    public ItemArmorMolecular(String name, EntityEquipmentSlot slot){
        super(null, "molecular", slot, 500_000_000, 131072D, 7);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }

    //armor behavior
    @Override
    public double getDamageAbsorptionRatio() {
        return 1.5;
    }

    @Override
    public int getEnergyPerDamage() {
        return 30000;
    }

    //jetpack behavior
    @Override
    public boolean drainEnergy(ItemStack itemStack, int i) {
        return false;
    }

    @Override
    public float getPower(ItemStack itemStack) {
        return 0;
    }

    @Override
    public float getDropPercentage(ItemStack itemStack) {
        return 0;
    }

    @Override
    public double getChargeLevel(ItemStack itemStack) {
        return 0;
    }

    @Override
    public boolean isJetpackActive(ItemStack itemStack) {
        return false;
    }

    @Override
    public float getHoverMultiplier(ItemStack itemStack, boolean b) {
        return 0;
    }

    @Override
    public float getWorldHeightDivisor(ItemStack itemStack) {
        return 0;
    }

    //hud mode
    @Override
    public boolean doesProvideHUD(ItemStack stack) {
        return false;
    }

    @Override
    public HudMode getHudMode(ItemStack stack) {
        return null;
    }

    //effects protection
    @Override
    public boolean addsProtection(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return ElectricItem.manager.getCharge(stack) > 0.0D;
    }

    @Override
    public boolean fullyProtects(EntityLivingBase entity, EntityEquipmentSlot slot, ItemStack stack) {
        return false;
    }

    //registry
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

    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    //textures
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return "ic2additions:textures/armor/molecular_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }
}
