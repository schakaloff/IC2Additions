package com.ic2additions.init;

import com.ic2additions.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;


public class IC2AdditionsCreativeTabs {
    public static final CreativeTabs tab = new CreativeTabs(Reference.MODID + ".tab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemInit.FULLERITE);
        }
    };
}
