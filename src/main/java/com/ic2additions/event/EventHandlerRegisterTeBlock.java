package com.ic2additions.event;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.TesRegistry;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Energy;
import ic2.core.util.StackUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerRegisterTeBlock {
    @SubscribeEvent
    public static void on(TeBlockFinalCallEvent event) {
        TeBlockRegistry.addAll(TesRegistry.class, TesRegistry.IDENTITY);
        TesRegistry.buildDummies();
        BlockTileEntity block = TeBlockRegistry.get(TesRegistry.IDENTITY);
        TeBlockRegistry.addCreativeRegisterer((list, tile, itemblock, tab) -> {
            if (tab == IC2AdditionsCreativeTabs.tab || tab == CreativeTabs.SEARCH) {
                tile.getAllTypes().forEach(type -> {
                    if (!type.hasItem()) return;
                    list.add(tile.getItemStack(type));
                    TileEntityBlock dummy = type.getDummyTe();
                    if (dummy != null) {
                        Energy energy = dummy.getComponent(Energy.class);
                        if (energy != null) {
                            ItemStack filled = tile.getItemStack(type);
                            StackUtil.getOrCreateNbtData(filled).setDouble("energy", energy.getCapacity());
                            list.add(filled);
                        }
                    }
                });
            }
        }, TesRegistry.IDENTITY);
    }
}
