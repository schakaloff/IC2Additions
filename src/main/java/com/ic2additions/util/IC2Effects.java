package com.ic2additions.util;

import com.ic2additions.objects.items.armor.ItemAdvancedQuantumArmor;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.List;

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

    private static final List<String> HOT_ITEMS = Arrays.asList(
            "depleted_uranium",
            "depleted_dual_uranium",
            "depleted_quad_uranium",
            "depleted_mox",
            "depleted_dual_mox",
            "depleted_quad_mox"
    );

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote) return; // only server side

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.isEmpty()) continue;

            String name = stack.getItem().getRegistryName().toString();
            if (HOT_ITEMS.stream().anyMatch(name::contains)) {
                if (!player.isBurning()) {
                    player.setFire(5); // set fire for 5 seconds
                }
                break; // only need to set fire once per tick
            }
        }
    }

}
