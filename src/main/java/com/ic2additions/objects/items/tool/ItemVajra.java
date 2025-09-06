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
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        boolean acc = getNbt(stack).getBoolean(NBT_ACCURATE);
        tooltip.add(TextFormatting.GOLD + "Silk Touch: " + (acc ? TextFormatting.DARK_GREEN + "ON" : TextFormatting.DARK_RED + "OFF"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && ic2.core.IC2.keyboard.isModeSwitchKeyDown(player)) {
            ItemStack stack = player.getHeldItem(hand);
            NBTTagCompound nbt = getNbt(stack);
            if (nbt.getBoolean(NBT_ACCURATE)) {
                nbt.setBoolean(NBT_ACCURATE, false);
                player.sendMessage(new net.minecraft.util.text.TextComponentString(TextFormatting.DARK_RED + "Silk Touch OFF"));
            } else if (accurateEnabled) {
                nbt.setBoolean(NBT_ACCURATE, true);
                player.sendMessage(new net.minecraft.util.text.TextComponentString(TextFormatting.DARK_GREEN + "Silk Touch ON"));
            } else {
                player.sendMessage(new net.minecraft.util.text.TextComponentString(TextFormatting.DARK_RED + "Silk Touch is disabled in config"));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {
        if (!accurateEnabled) return super.onBlockStartBreak(stack, pos, player);

        NBTTagCompound nbt = getNbt(stack);
        if (!nbt.getBoolean(NBT_ACCURATE)) return super.onBlockStartBreak(stack, pos, player);

        World world = player.world;
        if (world.isRemote) return true;

        if (!ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            return super.onBlockStartBreak(stack, pos, player);
        }

        Map<Enchantment, Integer> enchants = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        enchants.put(Enchantments.SILK_TOUCH, 10);
        EnchantmentHelper.setEnchantments(enchants, stack);

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        boolean didHarvest = false;

        try {
            if (!block.isAir(state, world, pos) && block.canHarvestBlock(world, pos, player)) {
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
                        block.harvestBlock(world, player, pos, state, world.getTileEntity(pos), stack);
                        if (xp > 0) {
                            block.dropXpOnBlockBreak(world, pos, xp);
                        }
                    }
                    stack.onBlockDestroyed(world, state, pos, player);
                }

                ElectricItem.manager.use(stack, this.operationEnergyCost, player);
                world.playEvent(2001, pos, Block.getStateId(state)); // break particles/sound
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
        // In original they forbid bedrock explicitly; same here
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
        return EnumRarity.EPIC;
    }

    private static NBTTagCompound getNbt(ItemStack stack) {
        return StackUtil.getOrCreateNbtData(stack);
    }
}
