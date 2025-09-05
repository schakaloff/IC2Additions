package com.ic2additions.objects.items.tool;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ElectricItem;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemDrill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.*;

public class BaseDrill extends ItemDrill {
    protected static final String NBT_ROOT = "adv_drill_data";
    protected static final String NBT_MODE = "Mode";

    protected static final String MODE_NONE        = "NONE";
    protected static final String MODE_SILK        = "SILK";
    protected static final String MODE_FORTUNE     = "FORTUNE";

    protected static final String MODE_AOE_SMALL   = "AOE_3x3";
    protected static final String MODE_AOE_MEDIUM  = "AOE_5x5";
    protected static final String MODE_AOE_LARGE   = "AOE_7x7";

    protected static final String MODE_VEIN_1      = "VEIN_I";   // ~6 blocks
    protected static final String MODE_VEIN_2      = "VEIN_II";  // ~13 blocks

    protected static final String MODE_AOE_LEGACY  = "3x3";
    protected static final String MODE_VEIN_LEGACY = "VEIN";

    protected final int opEnergyCost;
    protected final HarvestLevel harvestLevelCfg;
    protected final int maxCharge;
    protected final int transferLimit;
    protected final int tier;
    protected final float efficiency;

    protected final int energyUse;
    protected final int breakTime;
    protected final double blockCost;

    protected final boolean enableSilk;
    protected final boolean enableFortune;
    protected final int fortuneLevel;

    protected final boolean enableAoeSmall;
    protected final int aoeSmallRadius;           // 1 => 3x3
    protected final double aoeSmallBlockCost;

    protected final boolean enableAoeMedium;
    protected final int aoeMediumRadius;          // 2 => 5x5
    protected final double aoeMediumBlockCost;

    protected final boolean enableAoeLarge;
    protected final int aoeLargeRadius;           // 3 => 7x7
    protected final double aoeLargeBlockCost;

    protected final boolean enableVein1;
    protected final int vein1MaxBlocks;
    protected final int vein1MaxDistance;
    protected final double vein1BlockCost;

    protected final boolean enableVein2;
    protected final int vein2MaxBlocks;
    protected final int vein2MaxDistance;
    protected final double vein2BlockCost;

    public BaseDrill(
            String name,
            int opEnergyCost,
            HarvestLevel harvestLevel,
            int maxCharge,
            int transferLimit,
            int tier,
            float efficiency,
            int energyUse,
            int breakTime,
            double blockCost,
            int fortuneLevel,
            boolean enableSilk,
            boolean enableFortune,
            boolean enableAoe,
            int aoeRadius,
            double aoeBlockCost
    ) {
        this(
                name, opEnergyCost, harvestLevel, maxCharge, transferLimit, tier, efficiency,
                energyUse, breakTime, blockCost,
                fortuneLevel, enableSilk, enableFortune,
                enableAoe, aoeRadius, aoeBlockCost,
                false, 0, 0.0D,
                false, 0, 0.0D,
                false, 0, 0, 0.0D,
                false, 0, 0, 0.0D
        );
    }
    public BaseDrill(
            String name,
            int opEnergyCost,
            HarvestLevel harvestLevel,
            int maxCharge,
            int transferLimit,
            int tier,
            float efficiency,
            int energyUse,
            int breakTime,
            double blockCost,
            int fortuneLevel,
            boolean enableSilk,
            boolean enableFortune,
            boolean enableAoeSmall,  int aoeSmallRadius,  double aoeSmallBlockCost,
            boolean enableAoeMedium, int aoeMediumRadius, double aoeMediumBlockCost
    ) {
        this(
                name, opEnergyCost, harvestLevel, maxCharge, transferLimit, tier, efficiency,
                energyUse, breakTime, blockCost,
                fortuneLevel, enableSilk, enableFortune,
                enableAoeSmall, aoeSmallRadius, aoeSmallBlockCost,
                enableAoeMedium, aoeMediumRadius, aoeMediumBlockCost,
                false, 0, 0.0D,
                false, 0, 0, 0.0D,
                false, 0, 0, 0.0D
        );
    }
    public BaseDrill(
            String name,
            int opEnergyCost,
            HarvestLevel harvestLevel,
            int maxCharge,
            int transferLimit,
            int tier,
            float efficiency,
            int energyUse,
            int breakTime,
            double blockCost,
            int fortuneLevel,
            boolean enableSilk,
            boolean enableFortune,
            boolean enableAoeSmall,  int aoeSmallRadius,  double aoeSmallBlockCost,
            boolean enableAoeMedium, int aoeMediumRadius, double aoeMediumBlockCost,
            boolean enableAoeLarge,  int aoeLargeRadius,  double aoeLargeBlockCost,
            boolean enableVein1, int vein1MaxBlocks, int vein1MaxDistance, double vein1BlockCost,
            boolean enableVein2, int vein2MaxBlocks, int vein2MaxDistance, double vein2BlockCost
    ) {
        super(null, opEnergyCost, harvestLevel, maxCharge, transferLimit, tier, efficiency);

        this.opEnergyCost = opEnergyCost;
        this.harvestLevelCfg = harvestLevel;
        this.maxCharge = maxCharge;
        this.transferLimit = transferLimit;
        this.tier = tier;
        this.efficiency = efficiency;

        this.energyUse = energyUse;
        this.breakTime = breakTime;
        this.blockCost = blockCost;

        this.fortuneLevel = Math.max(1, fortuneLevel);
        this.enableSilk = enableSilk;
        this.enableFortune = enableFortune;

        this.enableAoeSmall   = enableAoeSmall   && aoeSmallRadius   > 0;
        this.aoeSmallRadius   = aoeSmallRadius;
        this.aoeSmallBlockCost= aoeSmallBlockCost;

        this.enableAoeMedium  = enableAoeMedium  && aoeMediumRadius  > 0;
        this.aoeMediumRadius  = aoeMediumRadius;
        this.aoeMediumBlockCost= aoeMediumBlockCost;

        this.enableAoeLarge   = enableAoeLarge   && aoeLargeRadius   > 0;
        this.aoeLargeRadius   = aoeLargeRadius;
        this.aoeLargeBlockCost= aoeLargeBlockCost;

        this.enableVein1      = enableVein1 && vein1MaxBlocks > 0;
        this.vein1MaxBlocks   = Math.max(0, vein1MaxBlocks);
        this.vein1MaxDistance = Math.max(0, vein1MaxDistance);
        this.vein1BlockCost   = vein1BlockCost;

        this.enableVein2      = enableVein2 && vein2MaxBlocks > 0;
        this.vein2MaxBlocks   = Math.max(0, vein2MaxBlocks);
        this.vein2MaxDistance = Math.max(0, vein2MaxDistance);
        this.vein2BlockCost   = vein2BlockCost;

        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    @Override public int energyUse(ItemStack s, World w, BlockPos p, IBlockState st) { return energyUse; }
    @Override public int breakTime(ItemStack s, World w, BlockPos p, IBlockState st) { return breakTime; }
    private boolean tryUseEUFromAnySource(ItemStack stack, EntityPlayer player, double eu) {
        return ElectricItem.manager.use(stack, eu, player);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            NBTTagCompound tag = stack.getOrCreateSubCompound(NBT_ROOT);
            String mode = normalizeMode(tag.getString(NBT_MODE));

            if (stack.getTagCompound() != null) stack.getTagCompound().removeTag("ench");

            if (player.isSneaking()) {
                tag.setString(NBT_MODE, MODE_NONE);
                player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Enchantments disabled"));
            } else {
                String next = nextMode(mode);
                tag.setString(NBT_MODE, next);

                if (MODE_SILK.equals(next)) {
                    stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Switched to Silk Touch"));
                } else if (MODE_FORTUNE.equals(next)) {
                    stack.addEnchantment(Enchantments.FORTUNE, fortuneLevel);
                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Switched to Fortune " + fortuneLevel));
                } else {
                    player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Switched to " + labelFor(next)));
                }
            }
            player.inventory.markDirty();
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    protected String normalizeMode(String mode) {
        if (mode == null || mode.isEmpty()) return MODE_NONE;
        if (MODE_AOE_LEGACY.equals(mode)) return MODE_AOE_SMALL;  // old "3x3"
        if (MODE_VEIN_LEGACY.equals(mode)) return MODE_VEIN_1;    // old "VEIN"
        return mode;
    }

    protected String nextMode(String current) {
        List<String> cycle = new ArrayList<>();
        cycle.add(MODE_NONE);
        if (enableSilk)      cycle.add(MODE_SILK);
        if (enableFortune)   cycle.add(MODE_FORTUNE);
        if (enableAoeSmall)  cycle.add(MODE_AOE_SMALL);
        if (enableAoeMedium) cycle.add(MODE_AOE_MEDIUM);
        if (enableAoeLarge)  cycle.add(MODE_AOE_LARGE);
        if (enableVein1)     cycle.add(MODE_VEIN_1);
        if (enableVein2)     cycle.add(MODE_VEIN_2);

        int idx = cycle.indexOf(current);
        if (idx < 0) idx = 0;
        return cycle.get((idx + 1) % cycle.size());
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound(NBT_ROOT);
        if (tag == null) return false;
        String mode = normalizeMode(tag.getString(NBT_MODE));
        return MODE_SILK.equals(mode)
                || MODE_FORTUNE.equals(mode)
                || MODE_AOE_SMALL.equals(mode)
                || MODE_AOE_MEDIUM.equals(mode)
                || MODE_AOE_LARGE.equals(mode)
                || MODE_VEIN_1.equals(mode)
                || MODE_VEIN_2.equals(mode);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        NBTTagCompound tag = stack.getSubCompound(NBT_ROOT);
        String mode = normalizeMode(tag != null ? tag.getString(NBT_MODE) : MODE_NONE);

        String nice =
                MODE_SILK.equals(mode)       ? "Silk Touch" :
                        MODE_FORTUNE.equals(mode)    ? ("Fortune " + fortuneLevel) :
                                MODE_AOE_SMALL.equals(mode)  ? labelFor(MODE_AOE_SMALL) :
                                        MODE_AOE_MEDIUM.equals(mode) ? labelFor(MODE_AOE_MEDIUM) :
                                                MODE_AOE_LARGE.equals(mode)  ? labelFor(MODE_AOE_LARGE) :
                                                        MODE_VEIN_1.equals(mode)     ? "Vein Miner I" :
                                                                MODE_VEIN_2.equals(mode)     ? "Vein Miner II" :
                                                                        "None";

        tooltip.add(TextFormatting.YELLOW + "Current Mode: " + TextFormatting.WHITE + nice);
        tooltip.add(TextFormatting.YELLOW + "Right-click to toggle:");
        addModeLine(tooltip, MODE_NONE.equals(mode), "None");
        if (enableSilk)      addModeLine(tooltip, MODE_SILK.equals(mode), "Silk Touch");
        if (enableFortune)   addModeLine(tooltip, MODE_FORTUNE.equals(mode), "Fortune " + fortuneLevel);
        if (enableVein1)     addModeLine(tooltip, MODE_VEIN_1.equals(mode), "Vein Miner I");
        if (enableVein2)     addModeLine(tooltip, MODE_VEIN_2.equals(mode), "Vein Miner II");
        if (enableAoeSmall)  addModeLine(tooltip, MODE_AOE_SMALL.equals(mode),  labelFor(MODE_AOE_SMALL));
        if (enableAoeMedium) addModeLine(tooltip, MODE_AOE_MEDIUM.equals(mode), labelFor(MODE_AOE_MEDIUM));
        if (enableAoeLarge)  addModeLine(tooltip, MODE_AOE_LARGE.equals(mode),  labelFor(MODE_AOE_LARGE));

    }

    protected String labelFor(String mode) {
        if (MODE_AOE_SMALL.equals(mode))  { int s = 2 * aoeSmallRadius  + 1; return s + "x" + s; }
        if (MODE_AOE_MEDIUM.equals(mode)) { int s = 2 * aoeMediumRadius + 1; return s + "x" + s; }
        if (MODE_AOE_LARGE.equals(mode))  { int s = 2 * aoeLargeRadius  + 1; return s + "x" + s; }
        if (MODE_VEIN_1.equals(mode)) return "Vein Miner I";
        if (MODE_VEIN_2.equals(mode)) return "Vein Miner II";
        return "AOE";
    }

    protected void addModeLine(List<String> tooltip, boolean active, String label) {
        tooltip.add((active ? TextFormatting.WHITE : TextFormatting.YELLOW) + " - " + label);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {
        boolean centerOk = super.onBlockDestroyed(stack, world, state, pos, entity);
        if (world.isRemote) return centerOk;
        if (!(entity instanceof EntityPlayerMP)) return centerOk;

        EntityPlayerMP player = (EntityPlayerMP) entity;

        NBTTagCompound tag = stack.getSubCompound(NBT_ROOT);
        String mode = normalizeMode(tag == null ? MODE_NONE : tag.getString(NBT_MODE));

        // Plane AOEs
        if (MODE_AOE_SMALL.equals(mode) && enableAoeSmall) {
            doPlaneAOE(world, player, stack, pos, aoeSmallRadius, aoeSmallBlockCost);
            return centerOk;
        }
        if (MODE_AOE_MEDIUM.equals(mode) && enableAoeMedium) {
            doPlaneAOE(world, player, stack, pos, aoeMediumRadius, aoeMediumBlockCost);
            return centerOk;
        }
        if (MODE_AOE_LARGE.equals(mode) && enableAoeLarge) {
            doPlaneAOE(world, player, stack, pos, aoeLargeRadius, aoeLargeBlockCost);
            return centerOk;
        }

        // Vein Miner tiers
        if (MODE_VEIN_1.equals(mode) && enableVein1) {
            doVeinMine(world, player, stack, pos, state, vein1MaxBlocks, vein1MaxDistance, vein1BlockCost);
            return centerOk;
        }
        if (MODE_VEIN_2.equals(mode) && enableVein2) {
            doVeinMine(world, player, stack, pos, state, vein2MaxBlocks, vein2MaxDistance, vein2BlockCost);
            return centerOk;
        }

        return centerOk;
    }

    protected void doPlaneAOE(World world, EntityPlayerMP player, ItemStack stack, BlockPos center, int radius, double costPer) {
        RayTraceResult rt = this.rayTrace(world, player, false);
        EnumFacing face = (rt != null && rt.sideHit != null) ? rt.sideHit : player.getHorizontalFacing();

        for (BlockPos p : getAoEPlane(face, center, radius)) {
            if (p.equals(center) || world.isAirBlock(p)) continue;

            IBlockState st = world.getBlockState(p);
            if (st.getBlockHardness(world, p) < 0) continue;
            if (!st.getBlock().canHarvestBlock(world, p, player)) continue;
            if (this.getDestroySpeed(stack, st) <= 1.0F) continue;

            if (!tryUseEUFromAnySource(stack, player, costPer)) break;

            GameType gt = player.interactionManager.getGameType();
            int xp = ForgeHooks.onBlockBreakEvent(world, gt, player, p);
            if (xp < 0) continue;

            st.getBlock().onBlockHarvested(world, p, st, player);
            boolean removed = world.destroyBlock(p, true);
            if (removed && xp > 0) st.getBlock().dropXpOnBlockBreak(world, p, xp);
        }
    }

    protected void doVeinMine(World world, EntityPlayerMP player, ItemStack stack, BlockPos start, IBlockState targetState,
                              int maxBlocks, int maxDistance, double costPer) {
        final net.minecraft.block.Block targetBlock = targetState.getBlock();
        final int targetMeta = targetBlock.getMetaFromState(targetState);

        Set<BlockPos> visited = new HashSet<>();
        ArrayDeque<BlockPos> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty() && visited.size() < maxBlocks) {
            BlockPos p = queue.poll();
            if (!visited.add(p)) continue;

            if (!p.equals(start)) {
                if (world.isAirBlock(p)) continue;

                IBlockState st = world.getBlockState(p);
                if (st.getBlock() != targetBlock) continue;
                if (st.getBlock().getMetaFromState(st) != targetMeta) continue;
                if (st.getBlockHardness(world, p) < 0) continue;
                if (!st.getBlock().canHarvestBlock(world, p, player)) continue;
                if (this.getDestroySpeed(stack, st) <= 1.0F) continue;

                if (!tryUseEUFromAnySource(stack, player, costPer)) break;

                GameType gt = player.interactionManager.getGameType();
                int xp = ForgeHooks.onBlockBreakEvent(world, gt, player, p);
                if (xp >= 0) {
                    st.getBlock().onBlockHarvested(world, p, st, player);
                    boolean removed = world.destroyBlock(p, true);
                    if (removed && xp > 0) st.getBlock().dropXpOnBlockBreak(world, p, xp);
                }
            }

            for (EnumFacing f : EnumFacing.values()) {
                BlockPos n = p.offset(f);
                if (visited.contains(n)) continue;

                if (maxDistance > 0 && (
                        Math.abs(n.getX() - start.getX()) > maxDistance ||
                                Math.abs(n.getY() - start.getY()) > maxDistance ||
                                Math.abs(n.getZ() - start.getZ()) > maxDistance)) continue;

                IBlockState ns = world.getBlockState(n);
                if (ns.getBlock() == targetBlock &&
                        ns.getBlock().getMetaFromState(ns) == targetMeta) {
                    queue.add(n);
                }
            }
        }
    }

    protected List<BlockPos> getAoEPlane(EnumFacing face, BlockPos center, int r) {
        List<BlockPos> out = new ArrayList<>((2 * r + 1) * (2 * r + 1));
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                BlockPos bp;
                switch (face) {
                    case UP:
                    case DOWN:  bp = center.add(dx, 0, dy); break;   // XZ
                    case NORTH:
                    case SOUTH: bp = center.add(dx, dy, 0); break;   // XY
                    case WEST:
                    case EAST:
                    default:    bp = center.add(0, dy, dx); break;   // YZ
                }
                out.add(bp);
            }
        }
        return out;
    }
}