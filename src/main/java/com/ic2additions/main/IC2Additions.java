package com.ic2additions.main;

import com.ic2additions.proxy.CommonProxy;
import com.ic2additions.util.Reference;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

@Mod(modid = Reference.MODID, version = Reference.VERSION, name = Reference.NAME)
public class IC2Additions {
    @Mod.Instance
    public static IC2Additions instance;
    public static IC2Additions getInstance(){return instance;}

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLConstructionEvent event) {
        proxy.construct(event);
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        proxy.preinit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        proxy.postinit(event);
    }
}
