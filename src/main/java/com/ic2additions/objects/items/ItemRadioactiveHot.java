package com.ic2additions.objects.items;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import com.ic2additions.interfaces.IHot;
import ic2.core.IC2Potion;
import ic2.core.item.ItemNuclearResource;
import ic2.core.item.armor.ItemArmorHazmat;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.item.type.IRadioactiveItemType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class ItemRadioactiveHot extends Item implements IRadioactiveItemType, IHot {
    private final int radiationDuration;
    private final int radiationAmplifier;
    public ItemRadioactiveHot(String name, int radiationDuration, int radiationAmplifier) {
        setRegistryName(name);
        setUnlocalizedName(name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(64);
        this.radiationDuration = radiationDuration;
        this.radiationAmplifier = radiationAmplifier;
        ItemInit.ITEMS.add(this);
    }

    @Override public int getRadiationDuration()  { return radiationDuration; }
    @Override public int getRadiationAmplifier() { return radiationAmplifier; }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean isCurrentItem) {
        if (world.isRemote || !(entity instanceof EntityLivingBase)) return;
        EntityLivingBase living = (EntityLivingBase) entity;

        if (!ItemArmorHazmat.hasCompleteHazmat(living)) {
            IC2Potion.radiation.applyTo(living, radiationDuration * 20, radiationAmplifier);
        }
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null || player.world.isRemote) return;
        if (player.isCreative() || player.isSpectator()) return;
        for (ItemStack s : player.inventory.mainInventory) {
            if (s.isEmpty()) continue;
            if (s.getItem() instanceof IHot) {
                player.setFire(2);
                return;
            }
        }
    }
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.getItem() instanceof IHot) {
            String line = TextFormatting.GOLD + I18n.format("tooltip.ic2additions.hot");
            if (!event.getToolTip().contains(line)) event.getToolTip().add(line);
        }
        if (stack.getItem() instanceof IRadioactiveItemType
                || stack.getItem() instanceof ItemNuclearResource
                || stack.getItem() instanceof ItemReactorUranium) {
            String line = TextFormatting.DARK_GREEN + I18n.format("tooltip.ic2additions.radioactive");
            if (!event.getToolTip().contains(line)) event.getToolTip().add(line);
        }
    }
}
