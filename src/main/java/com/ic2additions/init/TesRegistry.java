    package com.ic2additions.init;

    import java.util.Set;

    import com.ic2additions.main.IC2Additions;
    import com.ic2additions.tilentity.TileEntityMolecularAssembler;
    import com.ic2additions.tilentity.TileEntityRFtoEU;
    import com.ic2additions.tilentity.energystorage.*;
    import com.ic2additions.tilentity.solarpanels.TileEntityBetterSolarPanel;
    import com.ic2additions.tilentity.solarpanels.TileEntityHybridSolarPanel;
    import com.ic2additions.tilentity.solarpanels.TileEntityQuantumSolarPanel;
    import com.ic2additions.tilentity.solarpanels.TileEntityUltimateSolarPanel;
    import com.ic2additions.tilentity.transformers.TileEntityTransformerCV;
    import com.ic2additions.tilentity.transformers.TileEntityTransformerIV;
    import com.ic2additions.tilentity.transformers.TileEntityTransformerOV;
    import com.ic2additions.tilentity.transformers.TileEntityTransformerUV;
    import com.ic2additions.util.Reference;
    import ic2.core.block.ITeBlock;
    import ic2.core.block.TileEntityBlock;
    import ic2.core.ref.TeBlock;
    import ic2.core.ref.TeBlock.DefaultDrop;
    import ic2.core.ref.TeBlock.HarvestTool;
    import ic2.core.util.Util;
    import net.minecraft.block.material.Material;
    import net.minecraft.item.EnumRarity;
    import net.minecraft.item.ItemStack;
    import net.minecraft.tileentity.TileEntity;
    import net.minecraft.util.EnumFacing;
    import net.minecraft.util.ResourceLocation;
    import net.minecraft.util.math.BlockPos;
    import net.minecraftforge.fml.relauncher.Side;
    import net.minecraftforge.fml.relauncher.SideOnly;

    public enum TesRegistry implements ITeBlock {

        better_solar_panel(TileEntityBetterSolarPanel.class, 2, false, Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F, EnumRarity.COMMON),
        hybrid_solar_panel(TileEntityHybridSolarPanel.class, 3, false, Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F, EnumRarity.UNCOMMON),
        ultimate_solar_panel(TileEntityUltimateSolarPanel .class, 4, false,Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F,EnumRarity.RARE),
        quantum_solar_panel(TileEntityQuantumSolarPanel.class, 5, false,Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F,EnumRarity.EPIC),

        molecular_assembler(TileEntityMolecularAssembler.class, 6, true,Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F,EnumRarity.EPIC, Material.IRON,true),
        rf_to_eu_converter(TileEntityRFtoEU.class, 15, false,Util.onlyNorth, true,HarvestTool.Pickaxe, DefaultDrop.Self, 2F, 5F,EnumRarity.EPIC, Material.IRON,false),

        plasmatronmfe(TileEntityPlasmatronMFE.class, 7, false, Util.allFacings, true, HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10, EnumRarity.EPIC),
        auratonmfe(TileEntityAuratonMFE.class, 8, false, Util.allFacings, true, HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10, EnumRarity.EPIC),
        quantummfe(TileEntityQuantumMFE.class, 9, false, Util.allFacings, true, HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10, EnumRarity.EPIC),
        photonmfe(TileEntityPhotonMFE.class, 10, false, Util.allFacings, true, HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10, EnumRarity.RARE),

        iv_transformer(TileEntityTransformerIV .class, 11, false,Util.allFacings, true,HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10,EnumRarity.RARE),
        ov_transformer(TileEntityTransformerOV .class, 12, false,Util.allFacings, true,HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10,EnumRarity.RARE),
        cv_transformer(TileEntityTransformerCV .class, 13, false,Util.allFacings, true,HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10,EnumRarity.RARE),
        uv_transformer(TileEntityTransformerUV .class, 14, false,Util.allFacings, true,HarvestTool.Pickaxe, DefaultDrop.Self, 1, 10,EnumRarity.EPIC);

        public static final ResourceLocation IDENTITY = new ResourceLocation(Reference.MODID, "te");

        private final Class<? extends TileEntityBlock> teClass;
        private final int itemMeta;
        private final boolean hasActive;
        private final Set<EnumFacing> possibleFacings;
        private final boolean canBeWrenched;
        private final HarvestTool tool;
        private final DefaultDrop drop;
        private final float hardness;
        private final float explosionResistance;
        private final EnumRarity rarity;
        private final Material material;
        private final boolean isTransparent;
        private TileEntityBlock dummyTe;
        private TeBlock.ITePlaceHandler placeHandler;

        private TesRegistry(Class<? extends TileEntityBlock> teClass, int itemMeta, boolean hasActive, Set<EnumFacing> supportedFacings, boolean allowWrenchRotating, HarvestTool harvestTool, DefaultDrop defaultDrop, float hardness, float explosionResistance, EnumRarity rarity) {
            this(teClass, itemMeta, hasActive, supportedFacings, allowWrenchRotating, harvestTool, defaultDrop, hardness, explosionResistance, rarity, Material.IRON, false);
        }

        private TesRegistry(Class<? extends TileEntityBlock> teClass, int itemMeta, boolean hasActive, Set<EnumFacing> possibleFacings, boolean canBeWrenched, HarvestTool tool, DefaultDrop drop, float hardness, float explosionResistance, EnumRarity rarity, Material material, boolean isTransparent) {
            this.teClass = teClass;
            this.itemMeta = itemMeta;
            this.hasActive = hasActive;
            this.possibleFacings = possibleFacings;
            this.canBeWrenched = canBeWrenched;
            this.tool = tool;
            this.drop = drop;
            this.hardness = hardness;
            this.explosionResistance = explosionResistance;
            this.rarity = rarity;
            this.material = material;
            this.isTransparent = isTransparent;
        }

        @Override public boolean hasItem() { return teClass != null && itemMeta != -1; }
        @Override public String getName() { return name(); }
        @Override public ResourceLocation getIdentifier() { return IDENTITY; }
        @Override public Class<? extends TileEntityBlock> getTeClass() { return teClass; }
        @Override public boolean hasActive() { return hasActive; }
        @Override public int getId() { return itemMeta; }
        @Override public float getHardness() { return hardness; }
        @Override public HarvestTool getHarvestTool() { return tool; }
        @Override public DefaultDrop getDefaultDrop() { return drop; }
        @Override public float getExplosionResistance() { return explosionResistance; }
        @Override public boolean allowWrenchRotating() { return canBeWrenched; }
        @Override public Set<EnumFacing> getSupportedFacings() { return possibleFacings; }
        @Override public EnumRarity getRarity() { return rarity; }
        @Override public Material getMaterial() { return material; }
        @Override public boolean isTransparent() { return isTransparent; }
        @Override public void setPlaceHandler(TeBlock.ITePlaceHandler handler) { this.placeHandler = handler; }
        @Override public TeBlock.ITePlaceHandler getPlaceHandler() { return placeHandler; }




        public static void buildDummies() {
            for (TesRegistry block : values()) {
                if (block.teClass != null) {
                    try {
                        block.dummyTe = block.teClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        static {
            for (TesRegistry e : values()) {
                if (e.getTeClass() != null) {
                    TileEntity.register(
                            new ResourceLocation(Reference.MODID, "te_" + e.getName()).toString(),
                            (Class<? extends TileEntity>) e.getTeClass()
                    );
                }
            }
        }
        @Override public TileEntityBlock getDummyTe() { return dummyTe; }

    }
