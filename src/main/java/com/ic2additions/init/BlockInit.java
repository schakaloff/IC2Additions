package com.ic2additions.init;
import com.ic2additions.objects.blocks.BlockMyCable;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;


public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();
    public static final Block MY_CABLE = reg(new BlockMyCable("my_cable"));
    private static Block reg(Block b) {BLOCKS.add(b);return b;}
}
