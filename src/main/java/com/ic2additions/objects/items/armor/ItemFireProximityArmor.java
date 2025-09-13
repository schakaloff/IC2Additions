package com.ic2additions.objects.items.armor;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.main.IC2Additions;
import com.ic2additions.util.Reference;
import ic2.api.item.IHazmatLike;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemFireProximityArmor extends ItemArmor {
    public static final ArmorMaterial FIRE_PROX_MAT = EnumHelper.addArmorMaterial(
            "FIRE_PROXIMITY",
            "ic2additions:fire_proximity",
            5,
            new int[]{1, 2, 3, 1},
            12,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F
    );
    public ItemFireProximityArmor(String name, EntityEquipmentSlot slot) {
        super(FIRE_PROX_MAT, 0, slot);
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return "ic2additions:textures/armor/fire_proximity_" + (slot == EntityEquipmentSlot.LEGS ? "2" : "1") + ".png";
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

    public static boolean hasFullSet(EntityLivingBase e) {
        if (e == null) return false;
        int count = 0;
        for (ItemStack s : e.getArmorInventoryList()) {
            if (!s.isEmpty() && s.getItem() instanceof ItemFireProximityArmor) count++;
        }
        return count == 4;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.ic2additions.firefull_set"));
        tooltip.add("  " + TextFormatting.RED + I18n.format("tooltip.ic2additions.fire_prox.protects_fire"));
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (hasFullSet(event.getEntityLiving()) && event.getEntityLiving().isBurning()) {
            event.getEntityLiving().extinguish();
        }
    }
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!hasFullSet(event.getEntityLiving())) return;
        DamageSource src = event.getSource();
        if (src.isFireDamage() || src == DamageSource.HOT_FLOOR || src == DamageSource.LAVA) {
            event.setCanceled(true);
        }
    }
}
