package com.ic2additions.proxy;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.TesRegistry;
import com.ic2additions.main.IC2Additions;
import com.ic2additions.util.CommonRegistry;
import net.minecraftforge.fml.common.event.*;
import com.ic2additions.event.EventHandlerRegisterTeBlock;

public class CommonProxy {
    public void construct(FMLConstructionEvent event) {
        CommonRegistry.registerEventHandler(EventHandlerRegisterTeBlock.class);
    }

    public void preinit(FMLPreInitializationEvent event) {

    }

    public void init(FMLInitializationEvent event) {
        TesRegistry.buildDummies();
        IC2AdditionsCreativeTabs.init();
    }

    public void postinit(FMLPostInitializationEvent event) {
    }
}