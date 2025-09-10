package com.ic2additions.init;

import com.ic2additions.objects.ItemBlockPlasmaCable;
import com.ic2additions.objects.ItemMusicDisc;
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


    public static final Item URANIUM_233 = new ItemBaseRadioactiveItem("uranium_233", 180, 100);

    public static final Item CALIFORNIUM_252 = new ItemBaseRadioactiveItem("californium_252", 200, 90);
    public static final Item CALIFORNIUM_INGOT = new ItemBaseRadioactiveItem("californium_ingot", 50, 100);

    public static final Item THORIUM_232 = new ItemBaseRadioactiveItem("thorium_232", 30, 75);
    public static final Item THORIUM_INGOT = new ItemBaseRadioactiveItem("thorium_ingot", 30, 75);

    public static final Item NEPTUNIUM_INGOT = new ItemBaseRadioactiveItem("neptunium_ingot", 100, 70);
    public static final Item NEPTUNIUM_237 = new ItemBaseRadioactiveItem("neptunium_237", 200, 100);
    public static final Item NEPTUNIUM_239 = new ItemBaseRadioactiveItem("neptunium_239", 200, 100);



    public static final Item THORIUM_ROD_SINGLE = new ItemReactorThorium("thorium_fuel_rod", 1);
    public static final Item THORIUM_ROD_DOUBLE = new ItemReactorThorium("thorium_double_fuel_rod", 2);
    public static final Item THORIUM_ROD_QUAD = new ItemReactorThorium("thorium_quad_fuel_rod", 4);

    public static final Item DEPLETED_THORIUM = new ItemBase("depleted_thorium");
    public static final Item DEPLETED_THORIUM_DUAL = new ItemBase("depleted_dual_thorium");
    public static final Item DEPLETED_THORIUM_QUAD = new ItemBase("depleted_quad_thorium");

    public static final Item NEPTUNIUM_ROD_SINGLE = new ItemReactorNeptunium("neptunium_fuel_rod", 1);
    public static final Item NEPTUNIUM_ROD_DOUBLE = new ItemReactorNeptunium("neptunium_double_fuel_rod", 2);
    public static final Item NEPTUNIUM_ROD_QUAD = new ItemReactorNeptunium("neptunium_quad_fuel_rod", 4);

    public static final Item DEPLETED_NEPTUNIUM = new ItemBase("depleted_neptunium");
    public static final Item DEPLETED_NEPTUNIUM_DUAL = new ItemBase("depleted_dual_neptunium");
    public static final Item DEPLETED_NEPTUNIUM_QUAD = new ItemBase("depleted_quad_neptunium");

    public static final Item CALIFORNIUM_ROD_SINGLE = new ItemReactorCalifornium("californium_fuel_rod", 1);
    public static final Item CALIFORNIUM_ROD_DOUBLE = new ItemReactorCalifornium("californium_double_fuel_rod", 2);
    public static final Item CALIFORNIUM_ROD_QUAD = new ItemReactorCalifornium("californium_quad_fuel_rod", 4);

    public static final Item DEPLETED_CALIFORNIUM = new ItemBase("depleted_californium");
    public static final Item DEPLETED_CALIFORNIUM_DUAL = new ItemBase("depleted_dual_californium");
    public static final Item DEPLETED_CALIFORNIUM_QUAD = new ItemBase("depleted_quad_californium");


    //discs
    public static final Item COMMUNISM = new ItemMusicDisc("music_disc_communism", "record.communism");

    //items
    public static final Item PLASMATRON_CRYSTAL = new CrystalBase("plasmatron_crystal", 25_000_000, 4096D, 4, true);
    public static final Item AURATON_CRYSTAL = new CrystalBase("auraton_crystal", 50_000_000, 6144D, 5, true);
    public static final Item QUANTUM_CRYSTAL = new CrystalBase("quantum_crystal", 75_000_000, 8196D, 6, true);
    public static final Item PHOTON_CRYSTAL = new CrystalBase("photon_crystal", 100_000_000, 10000D, 7, true);
    //tools
    public static final Item PLASMA_CABLE = new ItemBlockPlasmaCable(BlockInit.PLASMA_CABLE);

    public static final Item ADVANCED_DRILL = new ItemAdvancedDrill("advanced_drill");
    public static final Item NANO_DRILL = new ItemNanoDrill("nano_drill");
    public static final Item QUANTUM_DRILL = new ItemQuantumDrill("quantum_drill");

    public static final Item ADVANCED_CHAINSAW = new ItemAdvancedChainsaw();
    public static final Item MULTI_TOOL = new ItemMultiTool("gravitool");
    public static final Item VAIJRA = new ItemVajra("vajra");

    public static final Item PLASMA_SABER = new ItemQuantumSaber();
    public static final Item ADVANCED_SABER = new ItemAdvancedSaber();

    //armor
    public static final Item ADVANCED_NANO_HELMET = new ItemAdvancedNanoArmour("advanced_nano_helmet",  EntityEquipmentSlot.HEAD);
    public static final Item ADVANCED_NANO_CHEST = new ItemAdvancedNanoArmour("advanced_nano_chestplate",   EntityEquipmentSlot.CHEST);
    public static final Item ADVANCED_NANO_LEGGINGS = new ItemAdvancedNanoArmour("advanced_nano_leggings",EntityEquipmentSlot.LEGS);
    public static final Item ADVANCED_NANO_BOOTS = new ItemAdvancedNanoArmour ("advanced_nano_boots",   EntityEquipmentSlot.FEET);

    public static final Item SERAPHIM_MK2_HELMET = new ItemArmorSeraphimMK2("seraphim_mk2_helmet",  EntityEquipmentSlot.HEAD);
    public static final Item SERAPHIM_MK2_CHEST = new ItemArmorSeraphimMK2("seraphim_mk2_chestplate",   EntityEquipmentSlot.CHEST);
    public static final Item SERAPHIM_MK2_LEGGINGS= new ItemArmorSeraphimMK2("seraphim_mk2_leggings",EntityEquipmentSlot.LEGS);
    public static final Item SERAPHIM_MK2_BOOTS = new ItemArmorSeraphimMK2 ("seraphim_mk2_boots",   EntityEquipmentSlot.FEET);

    public static final Item ADVANCED_ELECTRIC_JETPACK = new AdvancedElectricJetpack("advanced_electric_jetpack");

    public static final Item ADVANCED_PACK = new ItemAdvancedEnergyPack();
    public static final Item ULTIMATE_PACK = new ItemUltimateEnergyPack();
    public static final Item UNREAL_PACK = new ItemUnrealEnergyPack();


}
