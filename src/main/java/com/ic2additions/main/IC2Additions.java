package com.ic2additions.main;

import com.ic2additions.init.BlockInit;
import com.ic2additions.init.OreDict;
import com.ic2additions.init.TesRegistry;
import com.ic2additions.proxy.CommonProxy;
import com.ic2additions.recipes.molecular.MolecularAssemblerRecipes;
import com.ic2additions.recipes.Recipes;
import com.ic2additions.tilentity.*;
import com.ic2additions.util.Reference;
import com.ic2additions.world.gen.WorldOreGen;
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

        //GameRegistry.registerTileEntity(TileEntityPlasmaCable.class, new ResourceLocation(MODID, "plasma_cable"));
        GameRegistry.registerTileEntity(TileEntityEUtoRF.class, new ResourceLocation(MODID, "eu_to_rf_converter"));
        GameRegistry.registerTileEntity(TileEntityCable.class, new ResourceLocation(MODID, "cable"));
       // GameRegistry.registerTileEntity(TileEntityRFtoEU.class, new ResourceLocation(MODID, "rf_to_eu_converter"));
    }

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new WorldOreGen(), 30);
        proxy.preinit(event);
        OreDict.registerOres();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        TesRegistry.buildDummies();
        proxy.init(event);
        Recipes.addCraftingRecipes();
        Recipes.addMachineRecipe();
        Recipes.addFurnaceRecipe();
        MolecularAssemblerRecipes.init();
        BlockInit.registerOres();
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        proxy.postinit(event);
    }
}
