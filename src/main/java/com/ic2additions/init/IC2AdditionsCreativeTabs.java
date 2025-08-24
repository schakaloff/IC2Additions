package com.ic2additions.init;

import com.ic2additions.main.IC2Additions;
import com.ic2additions.util.Reference;
import com.ic2additions.util.UCreativeTab;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.ref.TeBlock;

public class IC2AdditionsCreativeTabs {
    public static final UCreativeTab tab = new UCreativeTab(Reference.MODID, "tab");
    public static void init(){
        BlockTileEntity block = TeBlockRegistry.get(TesRegistry.IDENTITY);
        tab.setIcon(block);
    }
}
