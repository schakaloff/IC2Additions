package com.ic2additions.util;

import ic2.api.item.IC2Items;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.reactor.ItemReactorMOX;
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

        if (stack.getItem() instanceof ItemArmorHazmat) {
            int insertIndex = 1;
            event.getToolTip().add(insertIndex, TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radiation_protection"));
        }

        if(stack.getItem() instanceof ItemArmorNanoSuit){
            int insertIndex=1;
            event.getToolTip().add(insertIndex++, TextFormatting.BLUE + I18n.format("tooltip.ic2additions.nightvision"));
        }

        String name = stack.getUnlocalizedName();
        if (name.contains("depleted_uranium") || name.contains("depleted_dual_uranium")  || name.contains("depleted_quad_uranium") ||
                name.contains("depleted_mox") || name.contains("depleted_dual_mox") || name.contains("depleted_quad_mox")) {
            String localized = net.minecraft.client.resources.I18n.format("tooltip.ic2additions.hot");
            event.getToolTip().add(TextFormatting.GOLD + localized);
        }

    }
}
