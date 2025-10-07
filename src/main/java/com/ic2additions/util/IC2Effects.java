package com.ic2additions.util;

import com.ic2additions.interfaces.IHot;
import com.ic2additions.objects.items.armor.ItemAdvancedQuantumArmor;
import com.ic2additions.objects.items.armor.ItemArmorSeraphimMK2;
import com.ic2additions.objects.items.armor.ItemThermohazmatArmor;
import ic2.api.item.IC2Items;
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
    public static final ItemStack depleted_uranium = IC2Items.getItem("nuclear", "depleted_uranium");
    public static final ItemStack depleted_dual_uranium = IC2Items.getItem("nuclear", "depleted_dual_uranium");
    public static final ItemStack depleted_quad_uranium = IC2Items.getItem("nuclear", "depleted_quad_uranium");

    public static final ItemStack depleted_mox = IC2Items.getItem("nuclear", "depleted_mox");
    public static final ItemStack depleted_dual_mox = IC2Items.getItem("nuclear", "depleted_dual_mox");
    public static final ItemStack depleted_quad_mox = IC2Items.getItem("nuclear", "depleted_quad_mox");

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote) return;

        boolean hasDepletedUranium = false;

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.isEmpty()) continue;

            if (ItemStack.areItemsEqual(stack, depleted_uranium) ||
                    ItemStack.areItemsEqual(stack, depleted_mox) ||
                    ItemStack.areItemsEqual(stack, depleted_dual_mox) ||
                    ItemStack.areItemsEqual(stack, depleted_quad_mox) ||
                    ItemStack.areItemsEqual(stack, depleted_dual_uranium) ||
                    ItemStack.areItemsEqual(stack, depleted_quad_uranium)) {
                hasDepletedUranium = true;
                break;
            }
            if (stack.getItem() instanceof IHot) {
                ((IHot) stack.getItem()).onCarriedTick(player, stack);
            }
        }
        if (hasDepletedUranium) {
            player.setFire(2);
        }
    }

    public static boolean hasFullSet(EntityLivingBase e) {
        if (e == null) return false;
        int count = 0;
        for (ItemStack s : e.getArmorInventoryList()) {
            if (!s.isEmpty() && s.getItem() instanceof ItemThermohazmatArmor) count++;
        }
        return count == 4;
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (!hasFullSet(event.getEntityLiving())) return;
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.isBurning()) {
            entity.extinguish();
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!hasFullSet(event.getEntityLiving())) return;
        DamageSource src = event.getSource();
        if (src.isFireDamage() || src == DamageSource.HOT_FLOOR || src == DamageSource.LAVA) {
            event.setCanceled(true);
            return;
        }
        if (src == DamageSource.FALL) {
            event.setCanceled(true);
            return;
        }
    }

}
