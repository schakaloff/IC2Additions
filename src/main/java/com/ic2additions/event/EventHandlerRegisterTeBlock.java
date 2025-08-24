package com.ic2additions.event;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.TesRegistry;
import com.ic2additions.tilentity.TileEntityEnergyStorageBase;
import ic2.api.event.TeBlockFinalCallEvent;
import ic2.core.block.TeBlockRegistry;
import ic2.core.block.comp.Energy;
import ic2.core.util.StackUtil;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;

public class EventHandlerRegisterTeBlock {

    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void on(TeBlockFinalCallEvent event){
        TeBlockRegistry.addAll(TesRegistry.class, TesRegistry.IDENTITY);
        TeBlockRegistry.addCreativeRegisterer((list, tile, itemblock, tab)->{
            if(tab == IC2AdditionsCreativeTabs.tab || tab == CreativeTabs.SEARCH){
                tile.getAllTypes().forEach(type -> {
                    if(type.hasItem()){
                        list.add(tile.getItemStack(type));
                        if(type.getDummyTe() instanceof TileEntityEnergyStorageBase){
                            TileEntityEnergyStorageBase storagetile = (TileEntityEnergyStorageBase) type.getDummyTe();
                            ItemStack filled = tile.getItemStack(type);
                            StackUtil.getOrCreateNbtData(filled).setDouble("energy", storagetile.getComponent(Energy.class).getCapacity());
                            list.add(filled);
                        }
                    }
                });
            }
        }, TesRegistry.IDENTITY);
    }
}
