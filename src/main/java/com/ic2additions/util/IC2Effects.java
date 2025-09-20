package com.ic2additions.util;

import com.ic2additions.objects.items.armor.ItemAdvancedQuantumArmor;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IC2Effects {
    public static boolean hasFullQuantumSet(EntityLivingBase e) {
        if (e == null) return false;
        int count = 0;
        for (ItemStack s : e.getArmorInventoryList()) {
            if (!s.isEmpty() && s.getItem() instanceof ItemArmorQuantumSuit) count++;
        }
        return count == 4;
    }

    @SubscribeEvent
    public static void wearingQuant(LivingEvent.LivingUpdateEvent event) {
        if (!hasFullQuantumSet(event.getEntityLiving())) return;
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.isBurning()) {
            entity.extinguish();
        }
        //entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, 20, 1, true, false));
        entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20, 0, true, false));
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!hasFullQuantumSet(event.getEntityLiving())) return;
        DamageSource src = event.getSource();
        if (src.isFireDamage() || src == DamageSource.HOT_FLOOR || src == DamageSource.LAVA) {
            event.setCanceled(true);
        }
    }
}
