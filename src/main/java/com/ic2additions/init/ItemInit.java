package com.ic2additions.init;

import com.ic2additions.objects.items.*;
import com.ic2additions.objects.items.armor.ItemArmorBetterNano;
import com.ic2additions.objects.items.tool.ItemAdvancedDrill;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();
    //ingots
    public static final Item COBALT_INGOT = new ItemBase("cobalt_ingot");
    //items
    public static final Item PLASMATRON_CRYSTAL = new CrystalBase("plasmatron_crystal", 25_000_000, 4096D, 4, true);
    public static final Item AURATON_CRYSTAL = new CrystalBase("auraton_crystal", 50_000_000, 6144D, 5, true);
    public static final Item QUANTUM_CRYSTAL = new CrystalBase("quantum_crystal", 75_000_000, 8196D, 6, true);
    public static final Item PHOTON_CRYSTAL = new CrystalBase("photon_crystal", 100_000_000, 10000D, 7, true);
    //tools
    public static final Item ADVANCED_DRILL = new ItemAdvancedDrill("advanced_drill");
    //armor
    public static final Item BETTER_NANO_HELMET = new ItemArmorBetterNano("better_nano_helmet",  EntityEquipmentSlot.HEAD);
    public static final Item BETTER_NANO_CHEST = new ItemArmorBetterNano("better_nano_chestplate",   EntityEquipmentSlot.CHEST);
    public static final Item BETTER_NANO_LEGGINGS= new ItemArmorBetterNano("better_nano_leggings",EntityEquipmentSlot.LEGS);
    public static final Item BETTER_NANO_BOOTS = new ItemArmorBetterNano("better_nano_boots",   EntityEquipmentSlot.FEET);
}
