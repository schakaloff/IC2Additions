package com.ic2additions.objects.items.tool;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.tile.IWrenchable;
import ic2.core.IC2;
import ic2.core.audio.PositionSpec;
import ic2.core.block.TileEntityBarrel;
import ic2.core.init.Localization;
import ic2.core.item.ElectricItemManager;
import ic2.core.item.tool.ItemTreetap;
import ic2.core.util.StackUtil;
import ic2.core.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ItemMultiTool extends ItemTool implements IElectricItem {
    private static final double COST_ROTATE = 50.0;
    private static final double COST_HOE = 50.0;
    private static final double COST_TAP = 50.0;
    private static final double COST_SCREW = 500.0;

    private static final double MAX_CHARGE = 300_000.0;
    private static final int    TIER       = 2;
    private static final double TRANSFER   = 10_000.0;

    private static final String MODID   = "ic2additions";
    private static final String TAG_MODE = "toolMode";

    public ItemMultiTool(String name){
        super(Item.ToolMaterial.IRON, Collections.emptySet());
        this.attackDamage = 16.0F;
        setRegistryName(MODID, name);
        setUnlocalizedName(MODID + "." + name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setMaxStackSize(1);
        setMaxDamage(27);
        ItemInit.ITEMS.add(this);
    }

    // ---------- Mode enum: only data ----------
    public enum Mode {
        HOE(TextFormatting.YELLOW),
        TREETAP(TextFormatting.YELLOW),
        WRENCH(TextFormatting.YELLOW),
        SCREWDRIVER(TextFormatting.YELLOW);

        public final TextFormatting color;
        public final String i18nKey;
        public final ModelResourceLocation model;

        Mode(TextFormatting color) {
            this.color = color;
            this.i18nKey = "ic2additions.graviTool." + name().toLowerCase(Locale.ENGLISH);
            this.model = new ModelResourceLocation(
                    MODID + ":gravitool/" + name().toLowerCase(Locale.ENGLISH), null
            );
        }

        public static Mode byId(int id) {
            Mode[] vals = values();
            return vals[Math.floorMod(id, vals.length)];
        }
    }

    // ---------- Mode helpers on the item ----------
    public static boolean hasMode(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey(TAG_MODE, 3);
    }

    public static Mode getMode(ItemStack stack) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(stack);
        return Mode.byId(tag.getInteger(TAG_MODE));
    }

    public static void setMode(ItemStack stack, Mode mode) {
        StackUtil.getOrCreateNbtData(stack).setInteger(TAG_MODE, mode.ordinal());
    }

    private static Mode nextMode(ItemStack stack) {
        return Mode.byId(getMode(stack).ordinal() + 1);
    }

    // ---------- Display ----------
    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (hasMode(stack)) {
            Mode m = getMode(stack);
            // ic2additions keys
            return Localization.translate("ic2additions.graviTool.set",
                    Localization.translate(getUnlocalizedNameInefficiently(stack)),
                    Localization.translate(m.i18nKey));
        }
        return Localization.translate(getUnlocalizedNameInefficiently(stack));
    }

    // ---------- Input handling ----------
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);

        if (IC2.keyboard.isModeSwitchKeyDown(player)) {
            Mode newMode = Mode.byId(getMode(stack).ordinal() + 1);
            setMode(stack, newMode);
            world.playSound(
                    player,                            // who hears it
                    player.getPosition(),               // position
                    SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, // the sound event
                    SoundCategory.PLAYERS,              // sound category
                    0.8F,                               // volume
                    1.2F                                // pitch (slightly higher "peep")
            );

            if (!world.isRemote) {
                String msg = Localization.translate("ic2additions.graviTool.changeTool", newMode.color + Localization.translate(newMode.i18nKey));
                player.sendMessage(new TextComponentString(msg));
                ((EntityPlayerMP) player).inventoryContainer.detectAndSendChanges();
            }

            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side,
                                           float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        switch (getMode(stack)) {
            case WRENCH:
                return onUseWrench(stack, player, world, pos, side) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case SCREWDRIVER:
                return onUseScrewdriver(stack, player, world, pos) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            default:
                return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        }
    }

    @Override
    public void onUpdate(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean held) {
        if (!hasMode(stack)) setMode(stack, Mode.HOE);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand,
                                      EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        switch (getMode(stack)) {
            case HOE:
                return onUseHoe(stack, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            case TREETAP:
                return onUseTreetap(stack, player, world, pos, facing) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
            default:
                return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        }
    }

    // ---------- Behaviors ----------
    private boolean onUseHoe(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        if (!player.canPlayerEdit(pos.offset(side), side, stack) || !hasEU(stack, COST_HOE, player)) return false;

        UseHoeEvent event = new UseHoeEvent(player, stack, world, pos);
        if (MinecraftForge.EVENT_BUS.post(event)) return false;
        if (event.getResult() == Event.Result.ALLOW) return spendEU(stack, COST_HOE, player, true);

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (side != EnumFacing.DOWN && world.isAirBlock(pos.up())) {
            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
                return setHoed(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
            }
            if (block == Blocks.DIRT) {
                BlockDirt.DirtType type = state.getValue(BlockDirt.VARIANT);
                if (type == BlockDirt.DirtType.DIRT) {
                    return setHoed(stack, player, world, pos, Blocks.FARMLAND.getDefaultState());
                }
                if (type == BlockDirt.DirtType.COARSE_DIRT) {
                    return setHoed(stack, player, world, pos, Blocks.DIRT.getDefaultState()
                            .withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                }
            }
        }
        return false;
    }

    private boolean setHoed(ItemStack stack, EntityPlayer player, World world, BlockPos pos, IBlockState state) {
        if (!spendEU(stack, COST_HOE, player, true)) return false;
        world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1f, 1f);
        if (!world.isRemote) world.setBlockState(pos, state, 11);
        return true;
    }

    private boolean onUseTreetap(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        TileEntity te = world.getTileEntity(pos);

        if (side.getAxis() != EnumFacing.Axis.Y && te instanceof TileEntityBarrel) {
            TileEntityBarrel barrel = (TileEntityBarrel) te;
            if (!barrel.getActive() && spendEU(stack, COST_TAP, player, true)) {
                if (!world.isRemote) {
                    barrel.setActive(true);
                    barrel.onPlaced(stack, null, side.getOpposite());
                }
                return true;
            }
            return false;
        }

        if (state.getBlock() == ic2.core.ref.BlockName.rubber_wood.getInstance() && hasEU(stack, COST_TAP, player)) {
            boolean ok = ItemTreetap.attemptExtract(player, world, pos, side, state, null);
            return ok && spendEU(stack, COST_TAP, player, false);
        }
        return false;
    }

    private boolean onUseWrench(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos)) return false;

        if (block instanceof IWrenchable) {
            IWrenchable w = (IWrenchable) block;

            EnumFacing current = w.getFacing(world, pos);
            EnumFacing desired;
            if (IC2.keyboard.isAltKeyDown(player)) {
                EnumFacing.Axis axis = side.getAxis();
                boolean forward = (!player.isSneaking() && side.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE)
                        || (player.isSneaking() && side.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE);
                desired = forward ? current.rotateAround(axis)
                        : current.rotateAround(axis).rotateAround(axis).rotateAround(axis);
            } else {
                desired = player.isSneaking() ? side.getOpposite() : side;
            }

            if (current != desired) {
                if (!hasEU(stack, COST_ROTATE, player)) return false;
                if (w.setFacing(world, pos, desired, player)) return spendEU(stack, COST_ROTATE, player, false);
            }

            if (w.wrenchCanRemove(world, pos, player)) {
                if (!hasEU(stack, COST_ROTATE, player)) return false;
                if (!world.isRemote) {
                    int exp;
                    if (player instanceof EntityPlayerMP) {
                        exp = ForgeHooks.onBlockBreakEvent(world,
                                ((EntityPlayerMP) player).interactionManager.getGameType(),
                                (EntityPlayerMP) player, pos);
                        if (exp < 0) return false;
                    } else exp = 0;

                    TileEntity te = world.getTileEntity(pos);
                    block.onBlockHarvested(world, pos, state, player);
                    if (!block.removedByPlayer(state, world, pos, player, true)) return false;
                    block.onBlockDestroyedByPlayer(world, pos, state);

                    List<ItemStack> drops = w.getWrenchDrops(world, pos, state, te, player, 0);
                    for (ItemStack drop : drops) StackUtil.dropAsEntity(world, pos, drop);
                    if (!player.capabilities.isCreativeMode && exp > 0) block.dropXpOnBlockBreak(world, pos, exp);
                }
                return spendEU(stack, COST_ROTATE, player, false);
            }
        }
        return false;
    }

    private boolean onUseScrewdriver(ItemStack stack, EntityPlayer player, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos)) return false;
        if (!(block instanceof BlockHorizontal)) return false;
        if (!spendEU(stack, COST_SCREW, player, false)) return false;

        EnumFacing facing = state.getValue(BlockHorizontal.FACING);
        facing = player.isSneaking() ? facing.rotateYCCW() : facing.rotateY();
        world.setBlockState(pos, state.withProperty(BlockHorizontal.FACING, facing));
        return true;
    }

    // ---------- Energy ----------
    private static boolean hasEU(ItemStack stack, double cost, EntityPlayer player) {
        ElectricItem.manager.chargeFromArmor(stack, player);
        double probe = ElectricItem.manager.discharge(stack, cost, Integer.MAX_VALUE, true, false, true);
        return Util.isSimilar(probe, cost);
    }

    private static boolean spendEU(ItemStack stack, double cost, EntityPlayer player, boolean silent) {
        if (ElectricItem.manager.use(stack, cost, player)) {
            if (!silent && player.world.isRemote) {
                IC2.audioManager.playOnce(player, PositionSpec.Hand, MODID + ":wrench", true,
                        IC2.audioManager.getDefaultVolume());
            }
            return true;
        }
        IC2.platform.messagePlayer(player, Localization.translate("ic2additions.graviTool.noEnergy"));
        return false;
    }

    // ---------- Misc ----------
    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity) {return false;}
    @Override public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity){ return true; }
    @Override public boolean doesSneakBypassUse(ItemStack stack, IBlockAccess world, BlockPos pos, EntityPlayer player){ return true; }
    @Override public boolean isRepairable() { return false; }
    @Override public int getItemEnchantability(){ return 0; }
    @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair){ return false; }
    @Override public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {return HashMultimap.create();}
    @Override public EnumRarity getRarity(ItemStack stack){ return EnumRarity.UNCOMMON; }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            ElectricItemManager.addChargeVariants(this, items);
        }
    }

    // IElectricItem
    @Override public boolean canProvideEnergy(ItemStack stack){ return false; }
    @Override public double getMaxCharge(ItemStack stack){ return MAX_CHARGE; }
    @Override public int getTier(ItemStack stack){ return TIER; }
    @Override public double getTransferLimit(ItemStack stack){ return TRANSFER; }

    // ---------- Client-only ----------
    @SideOnly(Side.CLIENT)
    public void registerModels() {
        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            @Override public ModelResourceLocation getModelLocation(ItemStack stack) {
                Mode m = hasMode(stack) ? getMode(stack) : Mode.HOE;
                return m.model;
            }
        });
        for (Mode m : Mode.values()) {
            ModelBakery.registerItemVariants(this, m.model);
        }
    }
}
