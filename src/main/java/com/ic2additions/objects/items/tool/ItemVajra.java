package com.ic2additions.objects.items.tool;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.api.item.ElectricItem;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemElectricTool;
import ic2.core.item.tool.ToolClass;
import ic2.core.util.StackUtil;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemVajra extends ItemElectricTool {

    public static boolean accurateEnabled = true;
    private static final String NBT_ACCURATE = "accurate";
    private static final String MODE_NONE = "NONE";
    private static final String MODE_SILK = "SILK";

    public ItemVajra(String name) {
        super(null, 3333, HarvestLevel.Iridium, EnumSet.of(ToolClass.Pickaxe, ToolClass.Shovel, ToolClass.Axe));
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        this.maxCharge = 10_000_000;
        this.transferLimit = 60_000;
        this.tier = 3;
        this.attackDamage = 25;
        this.efficiency = 20000.0f;
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        final String mode = readMode(stack);
        tooltip.add(TextFormatting.YELLOW + "Current Mode: " + TextFormatting.WHITE + label(mode));
        tooltip.add(TextFormatting.YELLOW + "Right-click to toggle");
        tooltip.add(TextFormatting.YELLOW + "Shift + Right Click to set to None");
        addModeLine(tooltip, MODE_NONE.equals(mode), "None");
        addModeLine(tooltip, MODE_SILK.equals(mode), "Silk Touch");
    }

    private static void addModeLine(List<String> tooltip, boolean active, String label) {
        tooltip.add((active ? TextFormatting.WHITE : TextFormatting.YELLOW) + " - " + label);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return MODE_SILK.equals(readMode(stack));
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
                next = MODE_NONE.equals(current) ? MODE_SILK : MODE_NONE;
            }

            writeMode(stack, next);
            player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Vajra mode: " + label(next)));
            player.inventory.markDirty();
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    private static String label(String mode) {
        return MODE_SILK.equals(mode) ? "Silk Touch" : "None";
    }


    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (!accurateEnabled) return super.onBlockStartBreak(stack, pos, player);
        if (!MODE_SILK.equals(readMode(stack))) return super.onBlockStartBreak(stack, pos, player);
        World world = player.world;
        if (world.isRemote) return true;
        if (!ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return super.onBlockStartBreak(stack, pos, player);
        }
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        boolean didHarvest = false;
        Map<Enchantment, Integer> enchants = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        enchants.remove(Enchantments.FORTUNE);
        enchants.put(Enchantments.SILK_TOUCH, 1);
        EnchantmentHelper.setEnchantments(enchants, stack);

        try {
            if (!block.isAir(state, world, pos) && block.canHarvestBlock(world, pos, player)) {
                int xp = 0;
                if (player instanceof EntityPlayerMP) {
                    GameType gameType = ((EntityPlayerMP) player).interactionManager.getGameType();
                    xp = ForgeHooks.onBlockBreakEvent(world, gameType, (EntityPlayerMP) player, pos);
                    if (xp < 0) return true;
                }
                block.onBlockHarvested(world, pos, state, player);
                if (player.capabilities.isCreativeMode) {
                    if (block.removedByPlayer(state, world, pos, player, false)) {
                        block.onBlockDestroyedByPlayer(world, pos, state);
                    }
                } else {
                    if (block.removedByPlayer(state, world, pos, player, true)) {
                        block.onBlockDestroyedByPlayer(world, pos, state);
                        block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                        if (xp > 0) block.dropXpOnBlockBreak(world, pos, xp);
                    }
                    stack.onBlockDestroyed(world, state, pos, player);
                }

                ElectricItem.manager.use(stack, this.operationEnergyCost, player);
                world.playEvent(2001, pos, Block.getStateId(state)); // particles/sound
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, pos));
                }
                didHarvest = true;
            }
        } finally {
            Map<Enchantment, Integer> after = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
            after.remove(Enchantments.SILK_TOUCH);
            EnchantmentHelper.setEnchantments(after, stack);
        }
        return didHarvest;
    }

    @Override
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        return state.getBlock() != Blocks.BEDROCK;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (attacker instanceof EntityPlayer) {
            if (ElectricItem.manager.use(stack, this.operationEnergyCost * 2.0, attacker)) {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 25.0F);
            } else {
                target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) attacker), 1.0F);
            }
        }
        return true;
    }

    @Override
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.RARE;
    }


    private static String readMode(ItemStack stack) {
        return getNbt(stack).getBoolean(NBT_ACCURATE) ? MODE_SILK : MODE_NONE;
    }

    private static void writeMode(ItemStack stack, String mode) {
        getNbt(stack).setBoolean(NBT_ACCURATE, MODE_SILK.equals(mode));
    }

    private static NBTTagCompound getNbt(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack);
    }
}
