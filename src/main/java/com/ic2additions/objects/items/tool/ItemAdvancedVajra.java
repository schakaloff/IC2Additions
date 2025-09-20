package com.ic2additions.objects.items.tool;
import ic2.api.item.ElectricItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.*;

public class ItemAdvancedVajra extends ItemVajra {
    private static final String NBT_ROOT = "adv_vajra_data";
    private static final String NBT_MODE = "Mode";
    private static final String MODE_NONE    = "NONE";
    private static final String MODE_SILK    = "SILK";
    private static final String MODE_AOE_3x3 = "AOE_3x3";

    private static final double EXTRA_BLOCK_COST = 1.0;

    public ItemAdvancedVajra(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        final String mode = readMode(stack);
        tooltip.add(TextFormatting.YELLOW + "Current Mode: " + TextFormatting.WHITE + label(mode));
        tooltip.add(TextFormatting.YELLOW + "Right-click to toggle");
        tooltip.add(TextFormatting.YELLOW + "Shift + Right Click to set to None");
        addModeLine(tooltip, MODE_NONE.equals(mode), "None");
        addModeLine(tooltip, MODE_SILK.equals(mode), "Silk Touch");
        addModeLine(tooltip, MODE_AOE_3x3.equals(mode), "3x3");
    }

    private static void addModeLine(List<String> tooltip, boolean active, String label) {
        tooltip.add((active ? TextFormatting.WHITE : TextFormatting.YELLOW) + " - " + label);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        String m = readMode(stack);
        return MODE_SILK.equals(m) || MODE_AOE_3x3.equals(m);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            String current = readMode(stack);
            String next;
            if (player.isSneaking()) {
                next = MODE_NONE;
            } else {
                next = nextMode(current);
            }
            writeMode(stack, next);
            if (stack.getTagCompound() != null) stack.getTagCompound().removeTag("ench");
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Vajra mode: " + label(next)));
            player.inventory.markDirty();
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    private static String label(String mode) {
        if (MODE_SILK.equals(mode)) return "Silk Touch";
        if (MODE_AOE_3x3.equals(mode)) return "3x3";
        return "None";
    }

    private static String nextMode(String cur) {
        if (MODE_NONE.equals(cur)) return MODE_SILK;
        if (MODE_SILK.equals(cur)) return MODE_AOE_3x3;
        return MODE_NONE;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        World world = player.world;
        if (world.isRemote) return super.onBlockStartBreak(stack, pos, player);

        if (!MODE_SILK.equals(readMode(stack))) {
            return super.onBlockStartBreak(stack, pos, player);
        }
        if (!ic2.api.item.ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return super.onBlockStartBreak(stack, pos, player);
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block.isAir(state, world, pos) || !block.canHarvestBlock(world, pos, player)) {
            return super.onBlockStartBreak(stack, pos, player);
        }
        Map<Enchantment, Integer> before = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        try {
            Map<Enchantment, Integer> temp = new HashMap<>(before);
            temp.remove(Enchantments.FORTUNE);
            temp.put(Enchantments.SILK_TOUCH, 1);
            EnchantmentHelper.setEnchantments(temp, stack);

            int xp = 0;
            if (player instanceof EntityPlayerMP) {
                GameType gameType = ((EntityPlayerMP) player).interactionManager.getGameType();
                xp = ForgeHooks.onBlockBreakEvent(world, gameType, (EntityPlayerMP) player, pos);
                if (xp < 0) return true; // cancelled
            }

            block.onBlockHarvested(world, pos, state, player);

            if (player.capabilities.isCreativeMode) {
                if (block.removedByPlayer(state, world, pos, player, false)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                }
            } else {
                if (block.removedByPlayer(state, world, pos, player, true)) {
                    block.onBlockDestroyedByPlayer(world, pos, state);
                    block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack); // Silk applies here
                    if (xp > 0) block.dropXpOnBlockBreak(world, pos, xp);
                }
                stack.onBlockDestroyed(world, state, pos, player);
            }

            ElectricItem.manager.use(stack, this.operationEnergyCost, player);
            world.playEvent(2001, pos, Block.getStateId(state));
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, pos));
            }
            return true;
        } finally {
            EnchantmentHelper.setEnchantments(before, stack);
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos center, EntityLivingBase entity) {
        boolean centerOk = super.onBlockDestroyed(stack, world, state, center, entity);
        if (world.isRemote) return centerOk;
        if (!(entity instanceof EntityPlayerMP)) return centerOk;
        if (!MODE_AOE_3x3.equals(readMode(stack))) return centerOk;

        EntityPlayerMP player = (EntityPlayerMP) entity;

        RayTraceResult rt = this.rayTrace(world, player, false);
        EnumFacing face = (rt != null && rt.sideHit != null) ? rt.sideHit : player.getHorizontalFacing();

        final double perBlockEU = this.operationEnergyCost * EXTRA_BLOCK_COST;

        for (BlockPos p : getAoEPlane(face, center, 1)) { // radius 1 => 3x3
            if (p.equals(center) || world.isAirBlock(p)) continue;

            IBlockState st = world.getBlockState(p);
            if (st.getBlockHardness(world, p) < 0) continue;
            if (!st.getBlock().canHarvestBlock(world, p, player)) continue;
            if (this.getDestroySpeed(stack, st) <= 1.0F) continue;

            if (!ElectricItem.manager.use(stack, perBlockEU, player)) break;

            GameType gt = player.interactionManager.getGameType();
            int xp = ForgeHooks.onBlockBreakEvent(world, gt, player, p);
            if (xp < 0) continue;

            Block b = st.getBlock();
            b.onBlockHarvested(world, p, st, player);
            boolean removed = b.removedByPlayer(st, world, p, player, !player.capabilities.isCreativeMode);

            if (removed) {
                b.onBlockDestroyedByPlayer(world, p, st);
                b.harvestBlock(world, player, p, st, world.getTileEntity(p), stack);
                if (xp > 0) b.dropXpOnBlockBreak(world, p, xp);

                world.playEvent(2001, p, Block.getStateId(st));
                player.connection.sendPacket(new SPacketBlockChange(world, p));
            }
        }
        return centerOk;
    }

    private static List<BlockPos> getAoEPlane(EnumFacing face, BlockPos center, int r) {
        List<BlockPos> out = new ArrayList<>((2 * r + 1) * (2 * r + 1));
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                BlockPos bp;
                switch (face) {
                    case UP:
                    case DOWN:  bp = center.add(dx, 0, dy); break;
                    case NORTH:
                    case SOUTH: bp = center.add(dx, dy, 0); break;
                    case WEST:
                    case EAST:
                    default:    bp = center.add(0, dy, dx); break;
                }
                out.add(bp);
            }
        }
        return out;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.EPIC;
    }

    private static String readMode(ItemStack stack) {
        NBTTagCompound root = stack.getSubCompound(NBT_ROOT);
        String m = root != null ? root.getString(NBT_MODE) : "";
        if (m == null || m.isEmpty()) return MODE_NONE;
        return m;
    }

    private static void writeMode(ItemStack stack, String mode) {
        NBTTagCompound root = stack.getOrCreateSubCompound(NBT_ROOT);
        root.setString(NBT_MODE, mode);
    }
}

