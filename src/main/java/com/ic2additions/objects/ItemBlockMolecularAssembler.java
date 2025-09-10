package com.ic2additions.objects;

import com.ic2additions.init.ItemInit;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockMolecularAssembler extends ItemBlock {
    public ItemBlockMolecularAssembler(Block block){
        super(block);
        setRegistryName(block.getRegistryName()); // 🔴 This is required
    }
}
