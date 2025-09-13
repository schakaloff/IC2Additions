package com.ic2additions.objects.items;

import com.ic2additions.interfaces.IHot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@Mod.EventBusSubscriber
public class ItemHot extends ItemBase implements IHot {
    public ItemHot(String name) {
        super(name);
    }


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        EntityPlayer player = event.player;
        if (player == null) return;
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.isEmpty()) continue;
            if (stack.getItem() instanceof IHot) {
                ((IHot) stack.getItem()).onCarriedTick(player, stack);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.isEmpty()) return;
        if (stack.getItem() instanceof IHot) {
            ((IHot) stack.getItem()).addHotTooltip(
                    stack,
                    event.getEntityPlayer() != null ? event.getEntityPlayer().world : null,
                    event.getToolTip(),
                    event.getFlags()
            );
        }
    }
}
