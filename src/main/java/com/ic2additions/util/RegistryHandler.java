package com.ic2additions.util;
import com.ic2additions.init.BlockInit;
import com.ic2additions.init.ItemInit;
import com.ic2additions.main.IC2Additions;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
        for (Block block : BlockInit.BLOCKS) {
            ItemBlock itemBlock = new ItemBlock(block);
            itemBlock.setRegistryName(block.getRegistryName()); // IMPORTANT
            event.getRegistry().register(itemBlock);
        }
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event){
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event)
    {
        for(Item item : ItemInit.ITEMS)
        {
            IC2Additions.proxy.registerItemRenderer(item, 0, "inventory");
        }

        for(Block block : BlockInit.BLOCKS){
            IC2Additions.proxy.registerItemRenderer(Item.getItemFromBlock(block), 0, "inventory");
        }
    }
}
