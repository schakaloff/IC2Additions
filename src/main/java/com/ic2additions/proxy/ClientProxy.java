package com.ic2additions.proxy;

import com.ic2additions.event.EventHandlerConfigChange;
import com.ic2additions.util.CommonRegistry;
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
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        CommonRegistry.registerEventHandler(EventHandlerConfigChange.class);
    }

    @Override
    public void postinit(FMLPostInitializationEvent event) {
        super.postinit(event);
    }
}