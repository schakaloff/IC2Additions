package com.ic2additions.init;

import com.ic2additions.main.IC2Additions;
import com.ic2additions.util.Reference;
import com.ic2additions.util.UCreativeTab;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import ic2.core.ref.TeBlock;

public class IC2AdditionsCreativeTabs {
    public static final UCreativeTab tab = new UCreativeTab(Reference.MODID, "tab");

    // Called from the TeBlock event when the block exists
    public static void setIcon(BlockTileEntity block) {
        tab.setIcon(block);
    }
}
