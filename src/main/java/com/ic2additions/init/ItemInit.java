package com.ic2additions.init;

import com.ic2additions.objects.items.CobaltCrystal;
import com.ic2additions.objects.items.ItemArmorBetterNano;
import com.ic2additions.objects.items.ItemBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();
    public static final Item COBALT_INGOT = new ItemBase("cobalt_ingot");
    public static final Item COBALT_CRYSTAL = new CobaltCrystal("cobalt_crystal");

    public static final Item BETTER_NANO_HELMET  =
            new ItemArmorBetterNano("better_nano_helmet",  EntityEquipmentSlot.HEAD);
    public static final Item BETTER_NANO_CHEST   =
            new ItemArmorBetterNano("better_nano_chestplate",   EntityEquipmentSlot.CHEST);
    public static final Item BETTER_NANO_LEGGINGS=
            new ItemArmorBetterNano("better_nano_leggings",EntityEquipmentSlot.LEGS);
    public static final Item BETTER_NANO_BOOTS   =
            new ItemArmorBetterNano("better_nano_boots",   EntityEquipmentSlot.FEET);
}
