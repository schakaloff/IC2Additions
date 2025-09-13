package com.ic2additions.interfaces;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IHot {
    default void onCarriedTick(EntityPlayer player, ItemStack stack){
        player.setFire(2);
    }
    default void addHotTooltip(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
        tooltip.add(TextFormatting.GOLD + I18n.format("tooltip.ic2additions.hot"));
    }
}
