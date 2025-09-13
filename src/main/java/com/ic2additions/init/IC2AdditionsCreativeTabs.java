package com.ic2additions.init;

import com.ic2additions.util.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.Comparator;

import static com.ic2additions.init.ItemInit.ORDERED_ITEMS;


public class IC2AdditionsCreativeTabs {
    public static final CreativeTabs tab = new CreativeTabs(Reference.MODID + ".tab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(ItemInit.FULLERITE);
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);

            items.sort(Comparator.comparingInt(stack ->
                    ORDERED_ITEMS.indexOf(stack.getItem())
            ));
        }
    };
}
