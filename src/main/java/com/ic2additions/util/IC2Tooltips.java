package com.ic2additions.util;

import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class IC2Tooltips {

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            player.sendMessage(new TextComponentString("Welcome back"));
            player.sendMessage(new TextComponentString("§4New Season - New IC2 Experience"));
        }
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;

        if (stack.getItem() instanceof ItemArmorQuantumSuit) {
            int insertIndex = 1;
            event.getToolTip().add(insertIndex++, TextFormatting.BLUE + I18n.format("tooltip.ic2additions.nightvision"));
            event.getToolTip().add(insertIndex++, TextFormatting.BLUE + I18n.format("tooltip.ic2additions.jetpack"));
            event.getToolTip().add(insertIndex++, TextFormatting.BLUE + I18n.format("tooltip.ic2additions.highjump"));
            event.getToolTip().add(insertIndex++, TextFormatting.BLUE + I18n.format("tooltip.ic2additions.fastwalk"));

            event.getToolTip().add(insertIndex++, TextFormatting.GOLD + I18n.format("tooltip.ic2additions.thermohazmat_set"));
            event.getToolTip().add(insertIndex++, "  " + TextFormatting.AQUA + I18n.format("tooltip.ic2additions.haste_2"));
            event.getToolTip().add(insertIndex++, "  " + TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radiation_protection"));
            event.getToolTip().add(insertIndex, "  " + TextFormatting.RED + I18n.format("tooltip.ic2additions.fire_prox.protects_fire"));}

        if (stack.getItem() instanceof ItemArmorHazmat) {
            int insertIndex = 1;
            event.getToolTip().add(insertIndex, TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radiation_protection"));
        }
    }
}
