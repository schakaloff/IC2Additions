package com.ic2additions.main;

import com.ic2additions.proxy.CommonProxy;
import com.ic2additions.recipes.MolecularAssemblerRecipes;
import com.ic2additions.recipes.Recipes;
import com.ic2additions.util.Reference;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.ic2additions.util.Reference.MODID;

@Mod(modid = MODID, version = Reference.VERSION, name = Reference.NAME, dependencies = Reference.DEPENDENCIES)
public class IC2Additions {
    @Mod.Instance
    public static IC2Additions instance;
    public static IC2Additions getInstance(){return instance;}

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preinit(FMLConstructionEvent event) {
        proxy.construct(event);
        GameRegistry.registerTileEntity(
                com.ic2additions.tilentity.TileEntityMyCable.class,
                new ResourceLocation(MODID, "my_cable")
        );
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        proxy.preinit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        Recipes.addCraftingRecipes();
        Recipes.addMachineRecipe();
        MolecularAssemblerRecipes.init();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        proxy.postinit(event);
    }
}
