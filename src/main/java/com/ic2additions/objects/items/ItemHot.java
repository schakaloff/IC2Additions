package com.ic2additions.objects.items;

import com.ic2additions.interfaces.IHot;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.List;


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

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (this instanceof IHot) {
            ((IHot) this).addHotTooltip(stack, worldIn, tooltip, flagIn);
        }
    }
}
