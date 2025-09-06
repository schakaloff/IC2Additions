package com.ic2additions.proxy;

import com.ic2additions.init.ItemInit;
import com.ic2additions.util.ActivatableItem;
import com.ic2additions.util.IC2AdditionsKeys;
import com.ic2additions.util.JetpackOverlay;
import com.ic2additions.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.*;

public class ClientProxy extends CommonProxy
{
    @Override
    public void construct(FMLConstructionEvent event) {
        super.construct(event);
    }

    @Override
    public void preinit(FMLPreInitializationEvent event) {
        super.preinit(event);

        for (Item item : ItemInit.ITEMS) {
            if (item instanceof ActivatableItem) {
                item.addPropertyOverride(new ResourceLocation(Reference.MODID, "active"), (stack, world, entity) -> ((ActivatableItem) item).isActive(stack) ? 1F : 0F);
            }
        }
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        IC2AdditionsKeys.register();
        new JetpackOverlay();
    }

    @Override
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }
    @Override
    public void registerItemRenderer(Item item, int meta, String id)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
    }

}