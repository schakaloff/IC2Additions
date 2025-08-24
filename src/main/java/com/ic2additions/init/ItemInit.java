package com.ic2additions.init;

import com.ic2additions.objects.items.CobaltCrystal;
import com.ic2additions.objects.items.ItemBase;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Item COBALT_INGOT = new ItemBase("cobalt_ingot");
    public static final Item COBALT_CRYSTAL = new CobaltCrystal("cobalt_crystal");
}
