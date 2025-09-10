package com.ic2additions.init;
import com.ic2additions.objects.blocks.BlockEUtoRF;
import com.ic2additions.objects.blocks.BlockPlasmaCable;
import com.ic2additions.objects.blocks.BlockRFtoEU;
import com.ic2additions.objects.blocks.MolecularAssemblerBlock;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;


public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();

    public static final Block PLASMA_CABLE = reg(new BlockPlasmaCable("plasma_cable"));
    //public static final Block MOLECULAR_ASSEMBLER = reg(new MolecularAssemblerBlock("molecular_assembler"));

    public static final Block EU_TO_RF_CONVERTER = reg(new BlockEUtoRF("eu_to_rf_converter"));
    public static final Block RF_TO_EU_CONVERTER = reg(new BlockRFtoEU("rf_to_eu_converter"));

    private static Block reg(Block b) {BLOCKS.add(b);return b;}


}
