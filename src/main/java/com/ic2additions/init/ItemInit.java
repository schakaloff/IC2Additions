package com.ic2additions.init;

import com.ic2additions.objects.items.ItemBlockPlasmaCable;
import com.ic2additions.objects.items.ItemMusicDisc;
import com.ic2additions.objects.items.*;
import com.ic2additions.objects.items.armor.*;
import com.ic2additions.objects.items.reactor.ItemReactorCalifornium;
import com.ic2additions.objects.items.reactor.ItemReactorNeptunium;
import com.ic2additions.objects.items.reactor.ItemReactorThorium;
import com.ic2additions.objects.items.tool.*;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.List;

public class ItemInit {
    public static final List<Item> ITEMS = new ArrayList<>();
    public static final List<Item> ORDERED_ITEMS = new ArrayList<>();

    // Tools & blocks
    public static final Item PLASMA_CABLE = register(new ItemBlockCable(BlockInit.PLASMA_CABLE));
    public static final Item PHOTON_CABLE = register(new ItemBlockCable(BlockInit.PHOTON_CABLE));
    public static final Item QUANTUM_CABLE = register(new ItemBlockCable(BlockInit.QUANTUM_CABLE));
    public static final Item ARC_CABLE = register(new ItemBlockCable(BlockInit.ARC_CABLE));

    // Materials
    public static final Item SANARIUM = register(new ItemHot("sanarium"));
    public static final Item SANARIUM_SHARD = register(new ItemHot("sanarium_shard"));
    public static final Item SANARIUM_ALLOY = register(new ItemHot("sanarium_alloy"));
    public static final Item IRRADIANT_URANIUM_INGOT = register(new ItemBaseRadioactive("irradiant_uranium_ingot",30,50));
    public static final Item ENRICHTED_SANARIUM = register(new ItemRadioactiveHot("enrichted_sanarium",30,60));
    public static final Item ENRICHTED_SANARIUM_ALLOY = register(new ItemRadioactiveHot("enrichted_sanarium_alloy",30,60));

    public static final Item IRRADIANT_GLASS_PANE = register(new ItemBase("irradiant_glass_pane"));
    public static final Item IRIDIUM_IRON_PLATE = register(new ItemBase("iridium_iron_plate"));
    public static final Item REINFORCED_IRIDIUM_IRON_PLATE = register(new ItemBase("reinforced_iridium_iron_plate"));
    public static final Item IRRADIANT_REINFORCED_PLATE = register(new ItemBase("irradiant_reinforced_plate"));
    public static final Item MT_CORE = register(new ItemBase("mt_core"));
    public static final Item QUANTUM_CORE = register(new ItemBase("quantum_core"));
    public static final Item COOLING_CORE = register(new ItemBase("cooling_core"));
    public static final Item ENGINE_BOOSTER = register(new ItemBase("engine_booster"));
    public static final Item GRAVITATION_ENGINE = register(new ItemBase("gravitation_engine"));
    public static final Item MAGNETRON = register(new ItemBase("magnetron"));
    public static final Item SUPER_CONDUCTOR = register(new ItemBase("super_conductor"));
    public static final Item SUPER_CONDUCTOR_COVER = register(new ItemBase("super_conductor_cover"));

    public static final Item PHOTON = register(new ItemBase("photon"));
    public static final Item FULLERITE = register(new ItemBase("fullerite"));

    public static final Item DURITANIUM_PLATE = register(new ItemDangerousDrop("duritanium_plate",16,true,true));
    public static final Item RAW_TRITANIUM_PLATE = register(new ItemDangerousDrop("raw_tritanium_plate",16,true,true));

    public static final Item HYBRID_CIRCUIT = register(new ItemBase("hybrid_circuit"));
    public static final Item NANO_CIRCUIT = register(new ItemBase("nano_circuit"));
    public static final Item QUANTUM_CIRCUIT = register(new ItemBase("quantum_circuit"));


    public static final Item IRIDIUM_INGOT = register(new ItemBase("iridium_ingot"));
    public static final Item DURITANIUM_INGOT = register(new ItemDangerousDrop("duritanium_ingot",16,true,true));
    public static final Item RAW_TRITANIUM_INGOT = register(new ItemDangerousDrop("raw_tritanium_ingot",16,true,true));
    public static final Item TRITANIUM_INGOT = register(new ItemDangerousDrop("tritanium_ingot",16,true,true));

    public static final Item NETHERSTAR_URANIUM = register(new ItemBaseRadioactive("netherstar_uranium",200,75));
    public static final Item NEPTUNIUM_INGOT = register(new ItemBaseRadioactive("neptunium_ingot", 100, 70));
    public static final Item THORIUM_INGOT = register(new ItemBaseRadioactive("thorium_ingot", 30, 75));
    public static final Item CALIFORNIUM_INGOT = register(new ItemBaseRadioactive("californium_ingot", 50, 100));

    public static final Item URANIUM_233 = register(new ItemBaseRadioactive("uranium_233", 180, 100));
    public static final Item CALIFORNIUM_252 = register(new ItemBaseRadioactive("californium_252", 200, 90));
    public static final Item THORIUM_232 = register(new ItemBaseRadioactive("thorium_232", 30, 75));
    public static final Item NEPTUNIUM_237 = register(new ItemBaseRadioactive("neptunium_237", 200, 100));
    public static final Item NEPTUNIUM_239 = register(new ItemBaseRadioactive("neptunium_239", 200, 100));

    // Reactor rods
    public static final Item THORIUM_ROD_SINGLE = register(new ItemReactorThorium("thorium_fuel_rod", 1));
    public static final Item THORIUM_ROD_DOUBLE = register(new ItemReactorThorium("thorium_double_fuel_rod", 2));
    public static final Item THORIUM_ROD_QUAD = register(new ItemReactorThorium("thorium_quad_fuel_rod", 4));

    public static final Item DEPLETED_THORIUM = register(new ItemBase("depleted_thorium"));
    public static final Item DEPLETED_THORIUM_DUAL = register(new ItemBase("depleted_dual_thorium"));
    public static final Item DEPLETED_THORIUM_QUAD = register(new ItemBase("depleted_quad_thorium"));

    public static final Item NEPTUNIUM_ROD_SINGLE = register(new ItemReactorNeptunium("neptunium_fuel_rod", 1));
    public static final Item NEPTUNIUM_ROD_DOUBLE = register(new ItemReactorNeptunium("neptunium_double_fuel_rod", 2));
    public static final Item NEPTUNIUM_ROD_QUAD = register(new ItemReactorNeptunium("neptunium_quad_fuel_rod", 4));

    public static final Item DEPLETED_NEPTUNIUM = register(new ItemBase("depleted_neptunium"));
    public static final Item DEPLETED_NEPTUNIUM_DUAL = register(new ItemBase("depleted_dual_neptunium"));
    public static final Item DEPLETED_NEPTUNIUM_QUAD = register(new ItemBase("depleted_quad_neptunium"));

    public static final Item CALIFORNIUM_ROD_SINGLE = register(new ItemReactorCalifornium("californium_fuel_rod", 1));
    public static final Item CALIFORNIUM_ROD_DOUBLE = register(new ItemReactorCalifornium("californium_double_fuel_rod", 2));
    public static final Item CALIFORNIUM_ROD_QUAD = register(new ItemReactorCalifornium("californium_quad_fuel_rod", 4));

    public static final Item DEPLETED_CALIFORNIUM = register(new ItemBase("depleted_californium"));
    public static final Item DEPLETED_CALIFORNIUM_DUAL = register(new ItemBase("depleted_dual_californium"));
    public static final Item DEPLETED_CALIFORNIUM_QUAD = register(new ItemBase("depleted_quad_californium"));

    // Music discs
    public static final Item COMMUNISM = register(new ItemMusicDisc("music_disc_communism", "record.communism"));

    // Crystals
    public static final Item PLASMATRON_CRYSTAL = register(new CrystalBase("plasmatron_crystal", 25_000_000, 4096D, 4, true));
    public static final Item AURATON_CRYSTAL = register(new CrystalBase("auraton_crystal", 50_000_000, 6144D, 5, true));
    public static final Item QUANTUM_CRYSTAL = register(new CrystalBase("quantum_crystal", 75_000_000, 8196D, 6, true));
    public static final Item PHOTON_CRYSTAL = register(new CrystalBase("photon_crystal", 100_000_000, 10000D, 7, true));

    //public static final Item PLASMA_CABLE = register(new ItemBlockPlasmaCable(BlockInit.PLASMA_CABLE));

    public static final Item ADVANCED_DRILL = register(new ItemAdvancedDrill("advanced_drill"));
    public static final Item NANO_DRILL = register(new ItemNanoDrill("nano_drill"));
    public static final Item QUANTUM_DRILL = register(new ItemQuantumDrill("quantum_drill"));

    public static final Item ADVANCED_CHAINSAW = register(new ItemAdvancedChainsaw());
    public static final Item MULTI_TOOL = register(new ItemMultiTool("gravitool"));
    public static final Item VAIJRA = register(new ItemVajra("vajra"));

    public static final Item PLASMA_SABER = register(new ItemQuantumSaber());
    public static final Item ADVANCED_SABER = register(new ItemAdvancedSaber());

    // Armor
    public static final Item FIRE_PROX_HELMET = register(new ItemFireProximityArmor("fire_proximity_helmet", EntityEquipmentSlot.HEAD));
    public static final Item FIRE_PROX_CHEST = register(new ItemFireProximityArmor("fire_proximity_chest", EntityEquipmentSlot.CHEST));
    public static final Item FIRE_PROX_LEGS = register(new ItemFireProximityArmor("fire_proximity_leggings", EntityEquipmentSlot.LEGS));
    public static final Item FIRE_PROX_BOOTS = register(new ItemFireProximityArmor("fire_proximity_boots", EntityEquipmentSlot.FEET));

    public static final Item THERMO_HAZ_HELMET = register(new ItemThermohazmatArmor("thermo_hazmat_helmet", EntityEquipmentSlot.HEAD));
    public static final Item THERMO_HAZ_CHEST = register(new ItemThermohazmatArmor("thermo_hazmat_chest", EntityEquipmentSlot.CHEST));
    public static final Item THERMO_HAZ_LEGS = register(new ItemThermohazmatArmor("thermo_hazmat_leggings", EntityEquipmentSlot.LEGS));
    public static final Item THERMO_HAZ_BOOTS = register(new ItemThermohazmatArmor("thermo_hazmat_boots", EntityEquipmentSlot.FEET));

    public static final Item ADVANCED_NANO_HELMET = register(new ItemAdvancedNanoArmour("advanced_nano_helmet", EntityEquipmentSlot.HEAD));
    public static final Item ADVANCED_NANO_CHEST = register(new ItemAdvancedNanoArmour("advanced_nano_chestplate", EntityEquipmentSlot.CHEST));
    public static final Item ADVANCED_NANO_LEGGINGS = register(new ItemAdvancedNanoArmour("advanced_nano_leggings", EntityEquipmentSlot.LEGS));
    public static final Item ADVANCED_NANO_BOOTS = register(new ItemAdvancedNanoArmour("advanced_nano_boots", EntityEquipmentSlot.FEET));

    public static final Item SERAPHIM_MK2_HELMET = register(new ItemArmorSeraphimMK2("seraphim_mk2_helmet", EntityEquipmentSlot.HEAD));
    public static final Item SERAPHIM_MK2_CHEST = register(new ItemArmorSeraphimMK2("seraphim_mk2_chestplate", EntityEquipmentSlot.CHEST));
    public static final Item SERAPHIM_MK2_LEGGINGS = register(new ItemArmorSeraphimMK2("seraphim_mk2_leggings", EntityEquipmentSlot.LEGS));
    public static final Item SERAPHIM_MK2_BOOTS = register(new ItemArmorSeraphimMK2("seraphim_mk2_boots", EntityEquipmentSlot.FEET));

    // Jetpacks & packs
    public static final Item ADVANCED_ELECTRIC_JETPACK = register(new AdvancedElectricJetpack("advanced_electric_jetpack"));
    public static final Item ADVANCED_PACK = register(new ItemAdvancedEnergyPack());
    public static final Item ULTIMATE_PACK = register(new ItemUltimateEnergyPack());
    public static final Item UNREAL_PACK = register(new ItemUnrealEnergyPack());

    // Register helper
    private static <T extends Item> T register(T item) {
        ORDERED_ITEMS.add(item);
        return item;
    }
}
