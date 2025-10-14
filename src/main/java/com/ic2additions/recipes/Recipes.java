package com.ic2additions.recipes;

import com.ic2additions.init.BlockInit;
import com.ic2additions.init.ItemInit;
import com.ic2additions.init.TesRegistry;
import ic2.api.item.IC2Items;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.IRecipeInputFactory;
import ic2.core.block.BlockTileEntity;
import ic2.core.block.TeBlockRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class Recipes {
    public static final ItemStack plcar = IC2Items.getItem("crafting","carbon_plate");
    public static final ItemStack composite = IC2Items.getItem("crafting","alloy");
    public static final ItemStack solar_panel = IC2Items.getItem("te","solar_generator");
    public static final ItemStack advanced_circuit = IC2Items.getItem("crafting","advanced_circuit");
    public static final ItemStack iron_plate = IC2Items.getItem("plate","iron");
    public static final ItemStack dense_lapis = IC2Items.getItem("plate","dense_lapis");
    public static final ItemStack dense_steel = IC2Items.getItem("plate","dense_steel");
    public static final ItemStack lead_plate = IC2Items.getItem("plate","lead");
    public static final ItemStack dense_obsidian = IC2Items.getItem("plate","dense_obsidian");
    public static final ItemStack iridium_plate = IC2Items.getItem("crafting","iridium");
    public static final ItemStack small_uranium_238 = IC2Items.getItem("nuclear", "small_uranium_238");
    public static final ItemStack small_uranium_2381 = IC2Items.getItem("nuclear", "small_uranium_238");
    public static final ItemStack uranium_238 = IC2Items.getItem("nuclear", "uranium_238");
    public static final ItemStack uranium = IC2Items.getItem("purified", "uranium");
    public static final ItemStack reinforced_glass = IC2Items.getItem("glass", "reinforced");
    public static final ItemStack iridium_ore = IC2Items.getItem("misc_resource", "iridium_ore");
    public static final ItemStack advanced_machine = IC2Items.getItem("resource", "advanced_machine");
    public static final ItemStack coal_chunk = IC2Items.getItem("crafting", "coal_chunk");
    public static final ItemStack fuel_rod = IC2Items.getItem("crafting", "fuel_rod");
    public static final ItemStack coil = IC2Items.getItem("crafting", "coil");
    public static final ItemStack carbon_plate = IC2Items.getItem("crafting", "carbon_plate");
    public static final ItemStack ev_transformer = IC2Items.getItem("te", "ev_transformer");
    public static final ItemStack hv_transformer = IC2Items.getItem("te", "hv_transformer");
    public static final ItemStack small_lead = IC2Items.getItem("dust", "small_lead");

    public static final ItemStack thorium232x4 = new ItemStack(ItemInit.THORIUM_232,4);
    public static final ItemStack thorium232x6 = new ItemStack(ItemInit.THORIUM_232,6);
    public static final ItemStack uranium233x2 = new ItemStack(ItemInit.URANIUM_233,2);
    public static final ItemStack uranium233x3 = new ItemStack(ItemInit.URANIUM_233,3);
    public static final ItemStack uranium233x6 = new ItemStack(ItemInit.URANIUM_233,4);

    public static final ItemStack neptunium237 = new ItemStack(ItemInit.NEPTUNIUM_237);
    public static final ItemStack neptunium237x2 = new ItemStack(ItemInit.NEPTUNIUM_237,2);
    public static final ItemStack neptunium237x3 = new ItemStack(ItemInit.NEPTUNIUM_237,3);

    public static final ItemStack neptunium239 = new ItemStack(ItemInit.NEPTUNIUM_239);
    public static final ItemStack neptunium239x2 = new ItemStack(ItemInit.NEPTUNIUM_239,2);
    public static final ItemStack neptunium239x3 = new ItemStack(ItemInit.NEPTUNIUM_239,3);

    public static final ItemStack stone = IC2Items.getItem("dust", "stone");
    public static final ItemStack iron_dust = IC2Items.getItem("dust", "iron");
    public static final ItemStack iron_dustx2 = IC2Items.getItem("dust", "iron");
    public static final ItemStack iron_dustx4 = IC2Items.getItem("dust", "iron");
    public static final ItemStack plutonium = IC2Items.getItem("nuclear", "plutonium");
    public static final ItemStack plutoniumx2 = IC2Items.getItem("nuclear", "plutonium");
    public static final ItemStack plutoniumx3 = IC2Items.getItem("nuclear", "plutonium");
    public static final ItemStack plutoniumx4 = IC2Items.getItem("nuclear", "plutonium");
    public static final ItemStack small_plutonium = IC2Items.getItem("nuclear", "small_plutonium");
    public static final ItemStack small_plutoniumx2 = IC2Items.getItem("nuclear", "small_plutonium");
    public static final ItemStack small_plutoniumx3 = IC2Items.getItem("nuclear", "small_plutonium");
    public static final ItemStack small_plutoniumx4 = IC2Items.getItem("nuclear", "small_plutonium");
    public static final ItemStack small_uranium_235 = IC2Items.getItem("nuclear", "small_uranium_235");
    public static final ItemStack uranium_235 = IC2Items.getItem("nuclear", "uranium_235");
    public static final ItemStack mox = IC2Items.getItem("nuclear", "mox");
    public static final ItemStack reactor_chamber = IC2Items.getItem("te", "reactor_chamber");
    public static final ItemStack neutron_reflector = IC2Items.getItem("thick_neutron_reflector");
    public static final ItemStack advanced_pack = IC2Items.getItem("energy_pack");
    public static final ItemStack jetpack_electric = IC2Items.getItem("jetpack_electric");
    public static final ItemStack energy_crystal = IC2Items.getItem("energy_crystal");
    public static final ItemStack lapotron_crystal = IC2Items.getItem("lapotron_crystal");
    public static final ItemStack overclocker = IC2Items.getItem("upgrade", "overclocker");
    public static final ItemStack diamond_drill = IC2Items.getItem("diamond_drill");
    public static final ItemStack hex_heat_storage = IC2Items.getItem("hex_heat_storage");
    public static final ItemStack advanced_heat_exchanger = IC2Items.getItem("advanced_heat_exchanger");
    public static final ItemStack advanced_heat_vent = IC2Items.getItem("advanced_heat_vent");
    public static final ItemStack heat_plating = IC2Items.getItem("heat_plating");
    public static final ItemStack electric_hoe = IC2Items.getItem("electric_hoe");
    public static final ItemStack electric_wrench = IC2Items.getItem("electric_wrench");
    public static final ItemStack electric_treetap = IC2Items.getItem("electric_treetap");
    public static final ItemStack iridium_reflector = IC2Items.getItem("iridium_reflector");
    public static final ItemStack hazmat_helmet = IC2Items.getItem("hazmat_helmet");
    public static final ItemStack hazmat_chestplate = IC2Items.getItem("hazmat_chestplate");
    public static final ItemStack hazmat_leggings = IC2Items.getItem("hazmat_leggings");
    public static final ItemStack rubber_boots = IC2Items.getItem("rubber_boots");
    public static final ItemStack glass_fibre_cable = IC2Items.getItem("cable", "type:glass,insulation:0");
    public static final ItemStack ins_hv_cable = IC2Items.getItem("cable", "type:iron,insulation:3");
    public static final ItemStack sulfur = IC2Items.getItem("dust", "sulfur");
    public static final ItemStack lithium = IC2Items.getItem("dust", "lithium");
    public static final ItemStack coal_fuel = IC2Items.getItem("dust", "coal_fuel");
    public static final ItemStack rubber = IC2Items.getItem("crafting", "rubber");
    public static final ItemStack jetpack_attachment_plate = IC2Items.getItem("crafting", "jetpack_attachment_plate");
    public static final ItemStack nano_helmet = IC2Items.getItem("nano_helmet");
    public static final ItemStack nano_chestplate = IC2Items.getItem("nano_chestplate");
    public static final ItemStack nano_leggings = IC2Items.getItem("nano_leggings");
    public static final ItemStack nano_boots = IC2Items.getItem("nano_boots");
    public static final ItemStack nano_saber = IC2Items.getItem("nano_saber");
    public static final ItemStack copper_plate = IC2Items.getItem("plate", "copper");
    public static final ItemStack mfsu = IC2Items.getItem("te", "mfsu");
    public static BlockTileEntity block = TeBlockRegistry.get(TesRegistry.IDENTITY);


    public static void addCraftingRecipes(){
        small_uranium_2381.setCount(9);
        small_plutoniumx2.setCount(2);
        small_plutoniumx3.setCount(3);
        small_plutoniumx4.setCount(4);

        plutoniumx2.setCount(2);
        plutoniumx3.setCount(3);
        plutoniumx4.setCount(4);

        iron_dustx2.setCount(2);
        iron_dustx4.setCount(4);

        addShapedRecipe(new ItemStack(ItemInit.IRIDIUM_IRON_PLATE), "PPP", "PIP", "PPP", 'P', iron_plate, 'I', iridium_ore);
        addShapedRecipe(new ItemStack(ItemInit.REINFORCED_IRIDIUM_IRON_PLATE), "III", "IPI", "III", 'I', composite, 'P', ItemInit.IRIDIUM_IRON_PLATE);
        addShapedRecipe(new ItemStack(ItemInit.IRRADIANT_REINFORCED_PLATE), "RSR", "DPD", "RSR", 'R', Items.REDSTONE, 'S', ItemInit.SANARIUM_SHARD, 'D', Items.DIAMOND, 'P', ItemInit.REINFORCED_IRIDIUM_IRON_PLATE);
        addShapedRecipe(new ItemStack(ItemInit.IRRADIANT_GLASS_PANE), "III", "UGU", "III", 'I', reinforced_glass, 'U', ItemInit.IRRADIANT_URANIUM_INGOT, 'G', Items.GLOWSTONE_DUST);
        addShapedRecipe(new ItemStack(ItemInit.MT_CORE), "PRP", "P P", "PRP", 'P', ItemInit.IRRADIANT_GLASS_PANE, 'R', neutron_reflector);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_CORE), "SNS", "NCN", "SNS", 'S', ItemInit.ENRICHTED_SANARIUM_ALLOY, 'N', Items.NETHER_STAR, 'C', ItemInit.QUANTUM_CIRCUIT);
        addShapedRecipe(new ItemStack(ItemInit.SANARIUM), "III","III","III", 'I', ItemInit.SANARIUM_SHARD);
        addShapedRecipe(new ItemStack(ItemInit.SANARIUM_ALLOY), "III", "ISI", "III", 'I', iridium_plate, 'S', ItemInit.SANARIUM);
        addShapedRecipe(new ItemStack(ItemInit.IRRADIANT_PURIFIED_URANIUM), "GUG", "UIU", "GUG", 'G', Items.GLOWSTONE_DUST, 'U', small_uranium_238, 'I', uranium);
        addShapedRecipe(new ItemStack(ItemInit.ENRICHTED_SANARIUM), "III", "ISI", "III", 'I', ItemInit.IRRADIANT_URANIUM_INGOT, 'S', ItemInit.SANARIUM);
        addShapedRecipe(new ItemStack(ItemInit.ENRICHTED_SANARIUM_ALLOY), " E ", "ESE", " E ",'E', ItemInit.ENRICHTED_SANARIUM, 'S', ItemInit.SANARIUM_ALLOY);
        addShapedRecipe(new ItemStack(ItemInit.COOLING_CORE), "HAH", "PIP", "HAH", 'H', hex_heat_storage, 'A', advanced_heat_exchanger, 'P', heat_plating, 'I', iridium_plate);
        addShapedRecipe(new ItemStack(ItemInit.NETHERSTAR_URANIUM), "III", "IUI", "III", 'I', Items.NETHER_STAR, 'U', ItemInit.IRRADIANT_URANIUM_INGOT);
        addShapedRecipe(uranium_238, "III","III","III", 'I', small_uranium_238);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_COIL), "ICI", "CLC", "ICI", 'I', ins_hv_cable, 'C', coil, 'L', dense_lapis );
        addShapedRecipe(new ItemStack(ItemInit.NANO_COIL), "RAR", "ACA", "RAR", 'R', ItemInit.REINFORCED_IRIDIUM_IRON_PLATE, 'A', ItemInit.ADVANCED_COIL, 'C', energy_crystal);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_COIL), "ICI", "CRC", "ICI", 'I', iridium_plate, 'C', ItemInit.NANO_COIL, 'R', ItemInit.IRRADIANT_REINFORCED_PLATE);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_ELECTRIC_MOTOR), " S ", "CDC", " S ", 'S', dense_steel, 'C', ItemInit.ADVANCED_COIL, 'D', Items.DIAMOND);
        addShapedRecipe(new ItemStack(ItemInit.NANO_ELECTRIC_MOTOR), " C ", "NBN", " C ", 'C', carbon_plate, 'N', ItemInit.NANO_COIL, 'B', advanced_machine);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_ELECTRIC_MOTOR), " S ", "CIC", " S ", 'S', ItemInit.SANARIUM, 'C', ItemInit.QUANTUM_COIL, 'I', ItemInit.IRRADIANT_GLASS_PANE);

        addShapedRecipe(new ItemStack(ItemInit.HYBRID_CIRCUIT), "SES", "LCL", "HLH", 'S', sulfur, 'E', ItemInit.ADVANCED_ELECTRIC_MOTOR, 'L', lithium, 'C', advanced_circuit, 'H', coal_fuel);
        addShapedRecipe(new ItemStack(ItemInit.NANO_CIRCUIT), "CEC", "RHR", "CRC", 'C', carbon_plate, 'E', ItemInit.NANO_ELECTRIC_MOTOR, 'R', Items.REDSTONE, 'H', ItemInit.HYBRID_CIRCUIT);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_CIRCUIT), "AEA", "INI", "AIA", 'A', advanced_heat_exchanger, 'E', ItemInit.QUANTUM_ELECTRIC_MOTOR, 'I', iridium_reflector, 'N', ItemInit.NANO_CIRCUIT);

        addShapedRecipe(new ItemStack(ItemInit.GRAVITATION_ENGINE), "HPH", "CTC", "HPH", 'H', ItemInit.HYBRID_CIRCUIT, 'P', ItemInit.SUPER_CONDUCTOR, 'C', ItemInit.COOLING_CORE, 'T', hv_transformer);
        addShapedRecipe(new ItemStack(ItemInit.ENGINE_BOOSTER), "GCG", "AUA", "CTC", 'G', Items.GLOWSTONE_DUST, 'C', composite, 'A', advanced_circuit, 'U', overclocker, 'T', advanced_heat_vent);
        addShapedRecipe(new ItemStack(ItemInit.SUPER_CONDUCTOR_COVER, 3), "CIC", "PPP", "CIC", 'C', composite, 'I', iridium_plate, 'P', carbon_plate);
        addShapedRecipe(new ItemStack(ItemInit.SUPER_CONDUCTOR, 2), "III", "SGS", "III", 'I', ItemInit.SUPER_CONDUCTOR_COVER, 'S', glass_fibre_cable, 'G', Items.GOLD_INGOT);
        addShapedRecipe(new ItemStack(ItemInit.MAGNETRON), "IMI", "MPM", "IMI", 'I', iron_plate, 'M', copper_plate, 'P', ItemInit.SUPER_CONDUCTOR);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_PACK), " P ", " C ", " E ", 'P', advanced_pack, 'C', advanced_circuit, 'E', energy_crystal);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_ELECTRIC_JETPACK), " E ", "BPB", " C ", 'E', jetpack_electric, 'B', ItemInit.ENGINE_BOOSTER, 'P', ItemInit.ADVANCED_PACK, 'C', ItemInit.HYBRID_CIRCUIT);
        addShapedRecipe(new ItemStack(ItemInit.ULTIMATE_PACK), "CIC", "CPC", "CSC", 'C', lapotron_crystal, 'I', iridium_plate, 'P', ItemInit.ADVANCED_ELECTRIC_JETPACK, 'S', ItemInit.SUPER_CONDUCTOR);
        addShapedRecipe(new ItemStack(ItemInit.UNREAL_PACK), "F F", "APA", "CAC", 'A', ItemInit.AURATON_CRYSTAL, 'P', ItemInit.ULTIMATE_PACK, 'C', ItemInit.QUANTUM_CIRCUIT, 'F', ItemInit.PHOTON);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_DRILL), "   ", "ODO", "COC", 'O', overclocker, 'D', diamond_drill, 'C', ItemInit.HYBRID_CIRCUIT);
        addShapedRecipe(new ItemStack(ItemInit.NANO_DRILL), "   ", "IDI", "CIC", 'I', carbon_plate, 'D', ItemInit.ADVANCED_DRILL, 'C', ItemInit.NANO_CIRCUIT);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_DRILL), " Q ", "PDP", "CPC", 'P', iridium_plate, 'D', ItemInit.NANO_DRILL, 'C', ItemInit.QUANTUM_CIRCUIT, 'Q', ItemInit.QUANTUM_CORE);
        addShapedRecipe(new ItemStack(ItemInit.MULTI_TOOL), "PHP", "CKC", "WAT", 'P', carbon_plate, 'H', electric_hoe, 'C', composite, 'K', energy_crystal, 'W', electric_wrench, 'T', electric_treetap, 'A', advanced_circuit);
        addShapedRecipe(new ItemStack(ItemInit.VAIJRA), "SCS", "PVP", "KLK", 'C', energy_crystal, 'P', ItemInit.IRRADIANT_URANIUM_INGOT, 'V', ItemInit.VAJRA_CORE, 'K', composite, 'L', lapotron_crystal, 'S', ItemInit.SANARIUM_SHARD);
        addShapedRecipe(new ItemStack(ItemInit.VAJRA_CORE), "SMS", "INI", "PTP", 'S', ItemInit.SANARIUM, 'M', ItemInit.MAGNETRON, 'I', iridium_plate, 'N', Items.NETHER_STAR, 'P', ItemInit.SUPER_CONDUCTOR, 'T', hv_transformer);

        addShapedRecipe(new ItemStack(ItemInit.PLASMATRON_CRYSTAL), "ICI", "ILI", "ICI", 'I', ItemInit.IRRADIANT_URANIUM_INGOT, 'C', ItemInit.HYBRID_CIRCUIT, 'L', lapotron_crystal);
        addShapedRecipe(new ItemStack(ItemInit.AURATON_CRYSTAL), "ACA", "SPS", "ACA", 'A', ItemInit.SANARIUM_ALLOY, 'C', ItemInit.NANO_CIRCUIT, 'S', ItemInit.SANARIUM, 'P', ItemInit.PLASMATRON_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_CRYSTAL), "ECE", "IAI", "ECE", 'E', ItemInit.ENRICHTED_SANARIUM_ALLOY, 'C', ItemInit.QUANTUM_CIRCUIT, 'I', ItemInit.IRRADIANT_REINFORCED_PLATE, 'A', ItemInit.AURATON_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.PHOTON_CRYSTAL), "DPD", "PQP", "DPD", 'D', ItemInit.DURITANIUM_PLATE, 'P', ItemInit.PHOTON, 'Q', ItemInit.QUANTUM_CRYSTAL);

        addShapedRecipe(block.getItemStack(TesRegistry.molecular_assembler), "MTM", "CKC", "MTM", 'M', advanced_machine, 'T', ev_transformer, 'C', ItemInit.HYBRID_CIRCUIT, 'K', ItemInit.MT_CORE);
        addShapedRecipe(block.getItemStack(TesRegistry.better_solar_panel), "III", "CSC", "HPH", 'I', new ItemStack(ItemInit.IRRADIANT_GLASS_PANE), 'C', composite, 'S', solar_panel, 'H', advanced_circuit, 'P', ItemInit.IRRADIANT_REINFORCED_PLATE);
        addShapedRecipe(block.getItemStack(TesRegistry.hybrid_solar_panel), "PLP", "ISI","HEH", 'P', plcar, 'L', Blocks.LAPIS_BLOCK, 'I', iridium_plate, 'S', block.getItemStack(TesRegistry.better_solar_panel), 'H', ItemInit.HYBRID_CIRCUIT, 'E', ItemInit.ENRICHTED_SANARIUM);
        addShapedRecipe(block.getItemStack(TesRegistry.ultimate_solar_panel), "NLN", "GPG", "SGS", 'N', ItemInit.NANO_CIRCUIT, 'L', Blocks.REDSTONE_BLOCK, 'G', coal_chunk, 'P', block.getItemStack(TesRegistry.hybrid_solar_panel),'S', ItemInit.ENRICHTED_SANARIUM_ALLOY);
        addShapedRecipe(block.getItemStack(TesRegistry.quantum_solar_panel), "III", "ICI","III", 'I', block.getItemStack(TesRegistry.ultimate_solar_panel), 'C', ItemInit.QUANTUM_CORE);

        addShapedRecipe(block.getItemStack(TesRegistry.plasmatronmfe),"PCP", "IMI", "PHP", 'P', ItemInit.PLASMATRON_CRYSTAL, 'C', glass_fibre_cable, 'I', iridium_plate, 'M', mfsu, 'H', ItemInit.HYBRID_CIRCUIT);
        addShapedRecipe(block.getItemStack(TesRegistry.auratonmfe), "SCS", "NMN", "SCS", 'S', ItemInit.SANARIUM_ALLOY, 'C', ItemInit.AURATON_CRYSTAL, 'N', ItemInit.NANO_CIRCUIT, 'M', block.getItemStack(TesRegistry.plasmatronmfe));
        addShapedRecipe(block.getItemStack(TesRegistry.quantummfe), "QIQ", "CMC", "QIQ", 'Q', ItemInit.QUANTUM_CORE, 'I', ItemInit.NETHERSTAR_URANIUM, 'C', ItemInit.QUANTUM_CRYSTAL, 'M', block.getItemStack(TesRegistry.auratonmfe));
        addShapedRecipe(block.getItemStack(TesRegistry.photonmfe), "CTC", "TMT", "CTC", 'C', ItemInit.PHOTON_CRYSTAL, 'T', ItemInit.TRITANIUM_INGOT, 'M', block.getItemStack(TesRegistry.quantummfe));

        addShapedRecipe(new ItemStack(ItemInit.DURITANIUM_INGOT), "IPI", "PFP", "IPI", 'I', new ItemStack(ItemInit.IRRADIANT_REINFORCED_PLATE), 'P', ItemInit.PHOTON, 'F', ItemInit.FULLERITE);
        addShapedRecipe(new ItemStack(ItemInit.RAW_TRITANIUM_INGOT), "FDF", "DFD", "FDF", 'F', ItemInit.FULLERITE, 'D', ItemInit.DURITANIUM_PLATE);
        addShapedRecipe(new ItemStack(ItemInit.TRITANIUM_INGOT), "III", "IFI", "III", 'I', ItemInit.RAW_TRITANIUM_PLATE, 'F', ItemInit.FULLERITE);
        addShapedRecipe(new ItemStack(ItemInit.THERMOSIL_PLATE), "PIP", "IPI", "PIP", 'P', ItemInit.TRITANIUM_PLATE, 'I', ItemInit.TRITANIUM_INGOT);

        addShapedRecipe(new ItemStack(ItemInit.MOLECULAR_HELMET), "TTT", "THT", " C ", 'T', ItemInit.THERMOSIL_PLATE, 'H', ItemInit.ADVANCED_QUANT_HELMET, 'C', ItemInit.PHOTON_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.MOLECULAR_CHEST), "TCT", "TMT", "TTT", 'T', ItemInit.THERMOSIL_PLATE, 'C', ItemInit.PHOTON_CRYSTAL, 'M', ItemInit.ADVANCED_QUANT_CHEST);
        addShapedRecipe(new ItemStack(ItemInit.MOLECULAR_LEGGINGS), "TTT", "TLT", "TCT", 'T', ItemInit.THERMOSIL_PLATE, 'L', ItemInit.ADVANCED_QUANT_LEGGINGS, 'C', ItemInit.PHOTON_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.MOLECULAR_BOOTS), "   ", "TBT", "TCT", 'T',ItemInit.THERMOSIL_PLATE, 'B', ItemInit.ADVANCED_QUANT_BOOTS, 'C', ItemInit.PHOTON_CRYSTAL);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_QUANT_HELMET), "MCM", "QHQ", "MUM", 'M', ItemInit.MAGNETRON, 'C', ItemInit.QUANTUM_CORE, 'Q', ItemInit.QUANTUM_CIRCUIT,'H', ItemInit.PROTOTYPE_QUANT_HELMET, 'U', ItemInit.QUANTUM_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_QUANT_CHEST), "GQG", "CHC", "GUG", 'G', ItemInit.GRAVITATION_ENGINE, 'Q', ItemInit.QUANTUM_CORE, 'C', ItemInit.QUANTUM_CIRCUIT, 'H', ItemInit.PROTOTYPE_QUANT_CHEST, 'U', ItemInit.QUANTUM_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_QUANT_LEGGINGS), "EQE", "CLC", "EUE", 'E', ItemInit.ENGINE_BOOSTER, 'Q', ItemInit.QUANTUM_CORE, 'C', ItemInit.QUANTUM_CIRCUIT, 'L',ItemInit.ADVANCED_QUANT_LEGGINGS, 'U', ItemInit.QUANTUM_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_QUANT_BOOTS), "SQS", "CBC", "SUS", 'S', ItemInit.SUPER_CONDUCTOR, 'Q', ItemInit.QUANTUM_CORE, 'C', ItemInit.QUANTUM_CIRCUIT, 'B', ItemInit.PROTOTYPE_QUANT_BOOTS, 'U', ItemInit.QUANTUM_CRYSTAL);

        addShapedRecipe(new ItemStack(ItemInit.PROTOTYPE_QUANT_HELMET), "IHI", "QCQ", "ISI", 'I', iridium_plate, 'H', ItemInit.SERAPHIM_MK2_HELMET, 'Q', ItemInit.QUANTUM_COIL, 'C', ItemInit.AURATON_CRYSTAL, 'S', hazmat_helmet);
        addShapedRecipe(new ItemStack(ItemInit.PROTOTYPE_QUANT_CHEST), "ISI", "CAC", "IPI", 'I',iridium_plate, 'S', ItemInit.SERAPHIM_MK2_CHEST, 'C', ItemInit.QUANTUM_COIL, 'A', ItemInit.AURATON_CRYSTAL, 'P', hazmat_chestplate);
        addShapedRecipe(new ItemStack(ItemInit.PROTOTYPE_QUANT_LEGGINGS), "ISI", "CPC", "ILI", 'I', iridium_plate, 'S', ItemInit.SERAPHIM_MK2_LEGGINGS, 'C', ItemInit.QUANTUM_COIL, 'P', ItemInit.AURATON_CRYSTAL, 'L', hazmat_leggings);
        addShapedRecipe(new ItemStack(ItemInit.PROTOTYPE_QUANT_BOOTS), "ISI", "CAC", "IRI", 'I', iridium_plate, 'S', ItemInit.SERAPHIM_MK2_BOOTS, 'C', ItemInit.QUANTUM_COIL, 'A', ItemInit.AURATON_CRYSTAL, 'R', rubber_boots);

        addShapedRecipe(new ItemStack(ItemInit.SERAPHIM_MK2_HELMET), "GHG", "CPC", "NFN", 'G', new ItemStack(Blocks.STAINED_GLASS, 1, 11), 'H', ItemInit.ADVANCED_NANO_HELMET, 'C', composite, 'N', ItemInit.NANO_CIRCUIT, 'F', ItemInit.FIRE_PROX_HELMET, 'P', ItemInit.PLASMATRON_CRYSTAL);
        addShapedRecipe(new ItemStack(ItemInit.SERAPHIM_MK2_CHEST), "BCB", "NPN", "AFA", 'B', advanced_machine, 'C', ItemInit.ADVANCED_NANO_CHEST, 'N', ItemInit.NANO_CIRCUIT, 'P', ItemInit.PLASMATRON_CRYSTAL, 'A', composite, 'F', ItemInit.FIRE_PROX_CHEST);
        addShapedRecipe(new ItemStack(ItemInit.SERAPHIM_MK2_LEGGINGS), "NLN", "OPO", "CFC", 'N', ItemInit.NANO_CIRCUIT, 'L', ItemInit.ADVANCED_NANO_LEGGINGS, 'O', dense_obsidian, 'P', ItemInit.PLASMATRON_CRYSTAL, 'C', composite, 'F', ItemInit.FIRE_PROX_LEGS);
        addShapedRecipe(new ItemStack(ItemInit.SERAPHIM_MK2_BOOTS), "CLC", "SPS", "BNB", 'C', carbon_plate, 'L', ItemInit.ADVANCED_NANO_BOOTS, 'S', Blocks.SLIME_BLOCK, 'P', ItemInit.PLASMATRON_CRYSTAL, 'B', ItemInit.FIRE_PROX_BOOTS, 'N', ItemInit.NANO_CIRCUIT);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_NANO_HELMET), "LNL", "HAH", "R R", 'L', dense_lapis, 'N', nano_helmet, 'H', ItemInit.HYBRID_CIRCUIT, 'A', lapotron_crystal, 'R', rubber);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_NANO_CHEST), "LNL", "HCH", "LRL", 'L', dense_lapis, 'C', lapotron_crystal, 'H', ItemInit.HYBRID_CIRCUIT, 'N', nano_chestplate, 'R', ItemInit.ADVANCED_ELECTRIC_JETPACK);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_NANO_LEGGINGS), "LCL", "HNH", "RLR", 'L', dense_lapis, 'C', lapotron_crystal, 'H', ItemInit.HYBRID_CIRCUIT, 'N', nano_leggings, 'R', rubber);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_NANO_BOOTS), " R ", "HCH", "LBL", 'R', rubber, 'H', ItemInit.HYBRID_CIRCUIT, 'C', lapotron_crystal, 'L', dense_lapis, 'B', nano_boots);

        addShapedRecipe(new ItemStack(ItemInit.FIRE_PROX_HELMET), " C ", "RGR", "RBR", 'C', carbon_plate, 'R', rubber, 'G', new ItemStack(Blocks.STAINED_GLASS, 1, 7), 'B', Items.BLAZE_POWDER);
        addShapedRecipe(new ItemStack(ItemInit.FIRE_PROX_CHEST), "R R", "RPR", "RCR", 'R', rubber, 'P', Items.BLAZE_POWDER, 'C', carbon_plate);
        addShapedRecipe(new ItemStack(ItemInit.FIRE_PROX_LEGS), "RBR", "R R", "R R", 'R', rubber, 'B', Items.BLAZE_POWDER);
        addShapedRecipe(new ItemStack(ItemInit.FIRE_PROX_BOOTS), "   ", "R R", "RBR", 'R', rubber, 'B', Items.BLAZE_POWDER);

        addShapedRecipe(new ItemStack(ItemInit.THERMO_HAZ_HELMET), " F ", " J ", " S ", 'F', ItemInit.FIRE_PROX_HELMET, 'J', jetpack_attachment_plate, 'S', hazmat_helmet);
        addShapedRecipe(new ItemStack(ItemInit.THERMO_HAZ_CHEST), " F ", " J ", " S ", 'F', ItemInit.FIRE_PROX_CHEST, 'J', jetpack_attachment_plate, 'S', hazmat_chestplate);
        addShapedRecipe(new ItemStack(ItemInit.THERMO_HAZ_LEGS), " F ", " J ", " S ", 'F', ItemInit.FIRE_PROX_LEGS, 'J', jetpack_attachment_plate, 'S', hazmat_leggings);
        addShapedRecipe(new ItemStack(ItemInit.THERMO_HAZ_BOOTS), " F ", " J ", " S ", 'F', ItemInit.FIRE_PROX_BOOTS, 'J', jetpack_attachment_plate, 'S', rubber_boots);

        addShapedRecipe(block.getItemStack(TesRegistry.iv_transformer), " S ", "CTA", " S ", 'S', ItemInit.SUPER_CONDUCTOR, 'C', advanced_circuit, 'T', ev_transformer, 'A', ItemInit.PLASMATRON_CRYSTAL);
        addShapedRecipe(block.getItemStack(TesRegistry.ov_transformer), " S ", "CTA", " S ", 'S', ItemInit.SUPER_CONDUCTOR, 'C', ItemInit.HYBRID_CIRCUIT, 'T', block.getItemStack(TesRegistry.iv_transformer), 'A', ItemInit.AURATON_CRYSTAL);
        addShapedRecipe(block.getItemStack(TesRegistry.cv_transformer), " S ", "CTA", " S ", 'S', ItemInit.SUPER_CONDUCTOR, 'C', ItemInit.NANO_CIRCUIT, 'T', block.getItemStack(TesRegistry.ov_transformer), 'A', ItemInit.QUANTUM_CRYSTAL);
        addShapedRecipe(block.getItemStack(TesRegistry.uv_transformer), " S ", "CTA", " S ", 'S', ItemInit.SUPER_CONDUCTOR, 'C', ItemInit.QUANTUM_CIRCUIT, 'T', block.getItemStack(TesRegistry.cv_transformer), 'A', ItemInit.PHOTON_CRYSTAL);

        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_SABER), " N ", "ISI", " R ", 'N', ItemInit.NANO_CIRCUIT, 'I', ItemInit.IRIDIUM_IRON_PLATE, 'S', nano_saber, 'R', Items.REDSTONE);
        addShapedRecipe(new ItemStack(ItemInit.PLASMA_SABER), "ICI", "RSR", "ILI", 'I', iridium_plate, 'C', ItemInit.QUANTUM_CIRCUIT, 'R', ItemInit.REINFORCED_IRIDIUM_IRON_PLATE, 'S', ItemInit.ADVANCED_SABER, 'L', dense_lapis);
        addShapedRecipe(new ItemStack(ItemInit.ADVANCED_VAIJRA), "FQF", "CVC", "FUF", 'F', ItemInit.FULLERITE, 'Q', ItemInit.QUANTUM_DRILL, 'C', ItemInit.QUANTUM_CIRCUIT, 'V', ItemInit.VAIJRA, 'U', ItemInit.IRRADIANT_URANIUM_INGOT);

        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_ROD_DOUBLE), "   ", "IPI", "   ", 'I', ItemInit.PLUTONIUM_ROD_SINGLE, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_ROD_QUAD), " T ", "CPC", " T ", 'T', ItemInit.PLUTONIUM_ROD_DOUBLE, 'C', copper_plate, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_ROD_QUAD), "TPT", "CPC", "TPT", 'T', ItemInit.PLUTONIUM_ROD_SINGLE, 'P', iron_plate, 'C', copper_plate);

        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_DUAL), "   ", "IPI", "   ", 'I', ItemInit.PLUTONIUM_ROD, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_QUAD), " T ", "CPC", " T ", 'T', ItemInit.PLUTONIUM_DUAL, 'C', copper_plate, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_QUAD), "TPT", "CPC", "TPT", 'T', ItemInit.PLUTONIUM_ROD, 'P', iron_plate, 'C', copper_plate);

        addShapedRecipe(new ItemStack(ItemInit.THORIUM_ROD_DOUBLE), "   ", "IPI", "   ", 'I', ItemInit.THORIUM_ROD_SINGLE, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.THORIUM_ROD_QUAD), " T ", "CPC", " T ", 'T', ItemInit.THORIUM_ROD_DOUBLE, 'C', copper_plate, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.THORIUM_ROD_QUAD), "TPT", "CPC", "TPT", 'T', ItemInit.THORIUM_ROD_SINGLE, 'P', iron_plate, 'C', copper_plate);

        addShapedRecipe(new ItemStack(ItemInit.NEPTUNIUM_ROD_DOUBLE), "   ", "IPI", "   ", 'I', ItemInit.NEPTUNIUM_ROD_SINGLE, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.NEPTUNIUM_ROD_QUAD), " T ", "CPC", " T ", 'T', ItemInit.NEPTUNIUM_ROD_DOUBLE, 'C', copper_plate, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.NEPTUNIUM_ROD_QUAD), "TPT", "CPC", "TPT", 'T', ItemInit.NEPTUNIUM_ROD_SINGLE, 'P', iron_plate, 'C', copper_plate);

        addShapedRecipe(new ItemStack(ItemInit.CALIFORNIUM_ROD_DOUBLE), "   ", "IPI", "   ", 'I', ItemInit.CALIFORNIUM_ROD_SINGLE, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.CALIFORNIUM_ROD_QUAD), " T ", "CPC", " T ", 'T', ItemInit.CALIFORNIUM_ROD_DOUBLE, 'C', copper_plate, 'P', iron_plate);
        addShapedRecipe(new ItemStack(ItemInit.CALIFORNIUM_ROD_QUAD), "TPT", "CPC", "TPT", 'T', ItemInit.CALIFORNIUM_ROD_DOUBLE, 'P', iron_plate, 'C', copper_plate);

        //addShapedRecipe(new ItemStack(ItemInit.CALIFORNIUM_252), "LUL", "UCU", "LUL", 'U', ItemInit.URANIUM_233, 'C', ItemInit.NEPTUNIUM_239, 'L', lithium);

        addShapelessRecipe(small_uranium_2381, uranium_238);

        addShapedRecipe(new ItemStack(ItemInit.PLASMA_CABLE, 6), "III", "CCC", "III", 'I', ItemInit.SUPER_CONDUCTOR, 'C', glass_fibre_cable);
        addShapedRecipe(new ItemStack(ItemInit.QUANTUM_CABLE,6), "MMM", "CCC", "MMM", 'M', ItemInit.MAGNETRON, 'C', ItemInit.PLASMA_CABLE);

        addShapedRecipe(new ItemStack(ItemInit.BREEDER_CORE), "LPL", "PCP", "LPL", 'L', lead_plate, 'P', small_plutonium, 'C', ItemInit.COOLING_CORE);
        addShapedRecipe(block.getItemStack(TesRegistry.breeding_reactor), "CHC", "IBI", "CHC", 'C', reactor_chamber, 'H', ItemInit.HYBRID_CIRCUIT, 'I', ItemInit.IRRADIANT_GLASS_PANE, 'B', ItemInit.BREEDER_CORE);

        addShapedRecipe(new ItemStack(ItemInit.PLUTONIUM_FUEL), "III", "CCC", "III", 'I', small_plutonium, 'C', plutonium);
        addShapedRecipe((plutonium), "III", "UUU", "UUU", 'I', uranium_238, 'U', small_uranium_235);

        addShapedRecipe(new ItemStack(ItemInit.NEPTUNIUM_237), "UNU", "NIN", "UNU", 'U', small_uranium_238, 'N', ItemInit.NEPTUNIUM_239, 'I', small_uranium_235);
        addShapedRecipe(new ItemStack(ItemInit.CALIFORNIUM_252), "MUM", "UIU", "MUM", 'M', mox, 'U', ItemInit.URANIUM_233, 'I',uranium_235);

    }

    private static void addShapedRecipe(ItemStack output, Object... input){
        ic2.api.recipe.Recipes.advRecipes.addRecipe(output,input);
    }

    private static void addShapelessRecipe(ItemStack output, Object... input){
        ic2.api.recipe.Recipes.advRecipes.addShapelessRecipe(output, input);
    }


    public static void addMachineRecipe(){
        IRecipeInputFactory input = ic2.api.recipe.Recipes.inputFactory;

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("minHeat", 100);

        NBTTagCompound nbt1 = new NBTTagCompound();
        nbt1.setInteger("amount", 1000);

        //canning
        addCanningRecipe(input.forStack(fuel_rod), input.forStack(new ItemStack(ItemInit.PLUTONIUM_FUEL)), new ItemStack(ItemInit.PLUTONIUM_ROD_SINGLE));
        addCanningRecipe(input.forStack(fuel_rod), input.forStack(new ItemStack(ItemInit.THORIUM_232)), new ItemStack(ItemInit.THORIUM_ROD_SINGLE));
        addCanningRecipe(input.forStack(fuel_rod), input.forStack(new ItemStack(ItemInit.NEPTUNIUM_237)), new ItemStack(ItemInit.NEPTUNIUM_ROD_SINGLE));
        addCanningRecipe(input.forStack(fuel_rod), input.forStack(new ItemStack(ItemInit.CALIFORNIUM_252)), new ItemStack(ItemInit.CALIFORNIUM_ROD_SINGLE));

        addCanningRecipe(input.forStack(fuel_rod), input.forStack(plutonium), new ItemStack(ItemInit.PLUTONIUM_ROD));

        //comp
        //addCompressorsRecipe(input.forStack(new ItemStack(Items.SLIME_BALL),9), new ItemStack(Block.getBlockById(165)));

        //centrifuge
        addMedCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.CRUSHED_THORIUM)), thorium232x4, stone, nbt);
        addMinCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.PURIFIED_THORIUM)), thorium232x6, nbt);

        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_THORIUM)), small_plutonium, uranium233x2, iron_dust, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_THORIUM_DUAL)), small_plutoniumx2, uranium233x3, iron_dustx2, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_THORIUM_QUAD)), small_plutoniumx4, uranium233x6, iron_dustx4, nbt);

        addMedCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_NEPTUNIUM)), neptunium239, iron_dust, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_NEPTUNIUM_DUAL)), neptunium239x2, small_plutoniumx2, iron_dustx2, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_NEPTUNIUM_QUAD)), neptunium239x3, small_plutoniumx4, iron_dustx4, nbt);

        addMinCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_CALIFORNIUM)), iron_dust, nbt);
        addMedCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_DUAL)), iron_dust, iron_dust, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.DEPLETED_CALIFORNIUM_QUAD)), iron_dustx2, iron_dust, iron_dust, nbt);

        addMedCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.NEPTUNIUM_ROD)), neptunium239, iron_dust, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.NEPTUNIUM_DUAL_ROD)), neptunium239x2, small_plutonium, iron_dustx2, nbt);
        addAdvCentrifugeRecipe(input.forStack(new ItemStack(ItemInit.NEPTUNIUM_QUAD_ROD)), neptunium239x3, small_plutoniumx3, iron_dustx4, nbt);

        addRollingRecipe(input.forStack(new ItemStack(ItemInit.DURITANIUM_INGOT)), new ItemStack(ItemInit.DURITANIUM_PLATE));
        addRollingRecipe(input.forStack(new ItemStack(ItemInit.RAW_TRITANIUM_INGOT)), new ItemStack(ItemInit.RAW_TRITANIUM_PLATE));
        addRollingRecipe(input.forStack(new ItemStack(ItemInit.TRITANIUM_INGOT)), new ItemStack(ItemInit.TRITANIUM_PLATE));

        //macerator
        addMaceratorRecipe(input.forStack(new ItemStack(BlockInit.THORIUM_ORE)), new ItemStack(ItemInit.CRUSHED_THORIUM,2));

        //ore washing
        addAdvOreWashingRecipe(input.forStack(new ItemStack(ItemInit.CRUSHED_THORIUM)), new ItemStack(ItemInit.PURIFIED_THORIUM), small_lead, stone, nbt1);


    }

    private static void addRollingRecipe(IRecipeInput input, ItemStack output) {
        ic2.api.recipe.Recipes.metalformerRolling.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[] {output});
    }

    private static void addCanningRecipe(IRecipeInput container, IRecipeInput fill, ItemStack output){
        ic2.api.recipe.Recipes.cannerBottle.addRecipe(container, fill, output);
    }

    private static void addCompressorsRecipe(IRecipeInput input, ItemStack output){
        ic2.api.recipe.Recipes.compressor.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[]{output});
    }

    private static void addMaceratorRecipe(IRecipeInput input, ItemStack output){
        ic2.api.recipe.Recipes.macerator.addRecipe(input, (NBTTagCompound)null, false, new ItemStack[]{output});
    }

    private static void addMinCentrifugeRecipe(IRecipeInput input, ItemStack output, NBTTagCompound nbt) {
        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, (NBTTagCompound)nbt, false, new ItemStack[] {output});
    }

    private static void addMedCentrifugeRecipe(IRecipeInput input, ItemStack output, ItemStack output2, NBTTagCompound nbt) {
        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, (NBTTagCompound)nbt, false, new ItemStack[] {output, output2});
    }

    private static void addAdvCentrifugeRecipe(IRecipeInput input, ItemStack output, ItemStack output2, ItemStack output3, NBTTagCompound nbt) {
        ic2.api.recipe.Recipes.centrifuge.addRecipe(input, (NBTTagCompound)nbt, false, new ItemStack[] {output, output2, output3});
    }

//    public static void addMinOreWashingRecipe(IRecipeInput input, ItemStack output, NBTTagCompound nbt2) {
//        ic2.api.recipe.Recipes.oreWashing.addRecipe(input, (NBTTagCompound)nbt2, false, new ItemStack[] {output});
//    }
//
//    public static void addMedOreWashingRecipe(IRecipeInput input, ItemStack output, ItemStack output2, NBTTagCompound nbt2) {
//        ic2.api.recipe.Recipes.oreWashing.addRecipe(input, (NBTTagCompound)nbt2, false, new ItemStack[] {output, output2});
//    }

    public static void addAdvOreWashingRecipe(IRecipeInput input, ItemStack output, ItemStack output2, ItemStack output3, NBTTagCompound nbt2) {
        ic2.api.recipe.Recipes.oreWashing.addRecipe(input, (NBTTagCompound)nbt2, false, new ItemStack[] {output, output2, output3});
    }

    public static void addFurnaceRecipe(){
        GameRegistry.addSmelting(ItemInit.IRRADIANT_PURIFIED_URANIUM, new ItemStack(ItemInit.IRRADIANT_URANIUM_INGOT), 1.0F);
    }

}
