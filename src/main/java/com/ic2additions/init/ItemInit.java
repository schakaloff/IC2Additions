package com.ic2additions.init;

import com.ic2additions.objects.ItemBlockMyCable;
import com.ic2additions.objects.ItemMusicDisc;
import com.ic2additions.objects.items.*;
import com.ic2additions.objects.items.armor.ItemAdvancedEnergyPack;
import com.ic2additions.objects.items.armor.ItemArmorBetterNano;
import com.ic2additions.objects.items.tool.ItemAdvancedDrill;
import com.ic2additions.objects.items.tool.ItemNanoDrill;
import com.ic2additions.objects.items.tool.ItemPlasmaSaber;
import com.ic2additions.objects.items.tool.ItemQuantumDrill;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final Item SANARIUM = new ItemBase("sanarium");
    public static final Item SANARIUM_SHARD = new ItemBase("sanarium_shard");
    public static final Item SANARIUM_ALLOY = new ItemBase("sanarium_alloy");
    public static final Item IRRADIANT_URANIUM_INGOT = new ItemBase("irradiant_uranium_ingot");
    public static final Item ENRICHTED_SANARIUM = new ItemBase("enrichted_sanarium");
    public static final Item ENRICHTED_SANARIUM_ALLOY = new ItemBase("enrichted_sanarium_alloy");
    public static final Item IRRADIANT_GLASS_PANE = new ItemBase("irradiant_glass_pane");
    public static final Item IRIDIUM_IRON_PLATE = new ItemBase("iridium_iron_plate");
    public static final Item REINFORCED_IRIDIUM_IRON_PLATE = new ItemBase("reinforced_iridium_iron_plate");
    public static final Item IRRADIANT_REINFORCED_PLATE = new ItemBase("irradiant_reinforced_plate");
    public static final Item IRIDIUM_INGOT = new ItemBase("iridium_ingot");
    public static final Item MT_CORE = new ItemBase("mt_core");
    public static final Item QUANTUM_CORE = new ItemBase("quantum_core");

    public static final Item PHOTON = new ItemBase("photon");
    public static final Item FULLERITE = new ItemBase("fullerite");
    public static final Item HYBRID_CIRCUIT = new ItemBase("hybrid_circuit");
    public static final Item NANO_CIRCUIT = new ItemBase("nano_circuit");
    public static final Item QUANTUM_CIRCUIT = new ItemBase("quantum_circuit");


    public static final Item COOLING_CORE = new ItemBase("cooling_core");
    public static final Item ENGINE_BOOSTER = new ItemBase("engine_booster");
    public static final Item GRAVITATION_ENGINE = new ItemBase("gravitation_engine");
    public static final Item MAGNETRON = new ItemBase("magnetron");
    public static final Item SUPER_CONDUCTOR = new ItemBase("super_conductor");
    public static final Item SUPER_CONDUCTOR_COVER = new ItemBase("super_conductor_cover");

    //discs
    public static final Item COMMUNISM = new ItemMusicDisc("music_disc_communism", "record.communism");

    //items
    public static final Item PLASMATRON_CRYSTAL = new CrystalBase("plasmatron_crystal", 25_000_000, 4096D, 4, true);
    public static final Item AURATON_CRYSTAL = new CrystalBase("auraton_crystal", 50_000_000, 6144D, 5, true);
    public static final Item QUANTUM_CRYSTAL = new CrystalBase("quantum_crystal", 75_000_000, 8196D, 6, true);
    public static final Item PHOTON_CRYSTAL = new CrystalBase("photon_crystal", 100_000_000, 10000D, 7, true);
    //tools
    public static final Item MY_CABLE_ITEM = new ItemBlockMyCable(BlockInit.MY_CABLE);
    public static final Item ADVANCED_DRILL = new ItemAdvancedDrill("advanced_drill");
    public static final Item NANO_DRILL = new ItemNanoDrill("nano_drill");
    public static final Item QUANTUM_DRILL = new ItemQuantumDrill("quantum_drill");
    //armor
    public static final Item BETTER_NANO_HELMET = new ItemArmorBetterNano("better_nano_helmet",  EntityEquipmentSlot.HEAD);
    public static final Item BETTER_NANO_CHEST = new ItemArmorBetterNano("better_nano_chestplate",   EntityEquipmentSlot.CHEST);
    public static final Item BETTER_NANO_LEGGINGS= new ItemArmorBetterNano("better_nano_leggings",EntityEquipmentSlot.LEGS);
    public static final Item BETTER_NANO_BOOTS = new ItemArmorBetterNano("better_nano_boots",   EntityEquipmentSlot.FEET);

    public static final Item ADVANCED_PACK = new ItemAdvancedEnergyPack();
    public static final Item PLASMA_SABER = new ItemPlasmaSaber();
}
