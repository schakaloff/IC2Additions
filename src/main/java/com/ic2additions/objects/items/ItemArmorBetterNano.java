package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.util.Reference;
import ic2.core.item.armor.ItemArmorElectric;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemArmorBetterNano extends ItemArmorElectric {
    public ItemArmorBetterNano(String name, EntityEquipmentSlot slot){
        super(null, "better_nano", slot, 5_000_000, 4096D, 4);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack stack){
        if (!world.isRemote && isWearingFullBetterNano(player)) {
            player.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20, 1, true, false));
            player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20, 1, true, false));
        }
    }

    private static boolean isWearing(EntityPlayer player, Item item, EntityEquipmentSlot slot) {
        ItemStack stack = player.getItemStackFromSlot(slot);
        return stack != null && stack.getItem() == item;
    }

    public static boolean isWearingFullBetterNano(EntityPlayer player) {
        return isWearing(player, ItemInit.BETTER_NANO_HELMET, EntityEquipmentSlot.HEAD)
                && isWearing(player, ItemInit.BETTER_NANO_CHEST, EntityEquipmentSlot.CHEST)
                && isWearing(player, ItemInit.BETTER_NANO_LEGGINGS, EntityEquipmentSlot.LEGS)
                && isWearing(player, ItemInit.BETTER_NANO_BOOTS, EntityEquipmentSlot.FEET);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
        return "ic2additions:textures/armor/better_nano_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
    }

    @Override
    public double getDamageAbsorptionRatio() {
        return 0.95;
    }

    @Override
    public int getEnergyPerDamage() {
        return 2500;
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
