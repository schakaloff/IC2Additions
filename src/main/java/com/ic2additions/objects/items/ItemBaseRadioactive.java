package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.core.IC2Potion;
import ic2.core.item.ItemNuclearResource;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.item.type.IRadioactiveItemType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber
public class ItemBaseRadioactive extends Item implements IRadioactiveItemType {
    private final int radiationDuration;
    private final int radiationAmplifier;

    public ItemBaseRadioactive(String name, int radiationDuration, int radiationAmplifier) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(64);

        this.radiationDuration = radiationDuration;
        this.radiationAmplifier = radiationAmplifier;

        ItemInit.ITEMS.add(this);
    }

    @Override
    public int getRadiationDuration() {
        return radiationDuration;
    }

    @Override
    public int getRadiationAmplifier() {
        return radiationAmplifier;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isCurrentItem) {
        if (!(entity instanceof EntityLivingBase)) return;
        EntityLivingBase e = (EntityLivingBase) entity;
        if (!ItemArmorHazmat.hasCompleteHazmat(e)) {
            int ticks = radiationDuration * 20;
            IC2Potion.radiation.applyTo(e, ticks, radiationAmplifier);
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.getItem() instanceof IRadioactiveItemType
                || stack.getItem() instanceof ItemNuclearResource
                || stack.getItem() instanceof ItemReactorUranium) {
            String line = TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radioactive");
            List<String> tips = event.getToolTip();
            if (!tips.contains(line)) {
                tips.add(line);
            }
        }
    }
}
