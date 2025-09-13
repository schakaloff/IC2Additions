package com.ic2additions.init;
import com.ic2additions.objects.blocks.*;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;


public class BlockInit {
    public static final List<Block> BLOCKS = new ArrayList<>();
    //public static final Block PLASMA_CABLE = reg(new BlockPlasmaCable("plasma_cable"));

    public static final Block EU_TO_RF_CONVERTER = reg(new BlockEUtoRF("eu_to_rf_converter"));

    public static final Block PLASMA_CABLE = reg(new BlockCable("plasma_cable", "Plasma Cable", 0.03, 38168));
    public static final Block QUANTUM_CABLE = reg(new BlockCable("quantum_cable", "Quantum Cable", 0.02, 131072));
    public static final Block PHOTON_CABLE = reg(new BlockCable("photon_cable", "Photon Cable", 0.01, 524288));
    public static final Block ARC_CABLE = reg(new BlockCable("arc_cable", "Arc Cable", 0.0, 2097152));

    private static Block reg(Block b) {BLOCKS.add(b);return b;}
}
