package com.ic2additions.objects.blocks;

import com.ic2additions.init.BlockInit;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.main.IC2Additions;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockBase extends Block {
    public BlockBase(String name, Material material) {
        super(material);
        setRegistryName(name);
        setUnlocalizedName(name);
        setHardness(3.0F);
        setResistance(5.0F);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setSoundType(SoundType.STONE);
        setHarvestLevel("pickaxe", 1);

        BlockInit.BLOCKS.add(this);
    }
}
