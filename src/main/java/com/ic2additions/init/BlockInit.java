package com.ic2additions.init;
import com.ic2additions.objects.blocks.BlockMyCable;
import com.ic2additions.objects.blocks.MolecularAssemblerBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;


public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block MY_CABLE = reg(new BlockMyCable("my_cable"));
    public static final Block MOLECULAR_ASSEMBLER = reg(new MolecularAssemblerBlock("molecular_assembler"));

    private static Block reg(Block b) {BLOCKS.add(b);return b;}


}
