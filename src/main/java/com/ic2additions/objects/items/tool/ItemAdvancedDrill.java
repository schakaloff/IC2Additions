package com.ic2additions.objects.items.tool;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.init.ItemInit;
import ic2.core.item.tool.HarvestLevel;
import ic2.core.item.tool.ItemDrill;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
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
import javax.annotation.Nullable;
import java.util.*;

public class ItemAdvancedDrill extends ItemDrill {

    private static final int OP_ENERGY_COST  = 20000;
    private static final HarvestLevel HARVEST = HarvestLevel.Diamond;
    private static final int MAX_CHARGE      = 200_000;
    private static final int TRANSFER_LIMIT  = 3_200;
    private static final int TIER            = 3;
    private static final float EFFICIENCY    = 16.0f;

    private static final int    ENERGY_USE   = 30;
    private static final int    BREAK_TIME   = 120;
    private static final double BLOCK_COST   = 120.0D;

    private static final String NBT_ROOT = "adv_drill_data";
    private static final String NBT_MODE = "Mode";
    private static final String MODE_NONE    = "NONE";
    private static final String MODE_SILK    = "SILK";
    private static final String MODE_FORTUNE = "FORTUNE";
    private static final String MODE_AOE = "AOE_3x3";


    public ItemAdvancedDrill(String name) {
        super(null, OP_ENERGY_COST, HARVEST, MAX_CHARGE, TRANSFER_LIMIT, TIER, EFFICIENCY);
        setRegistryName(name);
        setUnlocalizedName(name);
        setMaxStackSize(1);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        ItemInit.ITEMS.add(this);
    }
    @Override
    public int energyUse(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return ENERGY_USE;
    }

    @Override
    public int breakTime(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return BREAK_TIME;
    }

    @Override
    public boolean breakBlock(ItemStack stack, World world, BlockPos pos, IBlockState state) {
        return this.tryUsePower(stack, BLOCK_COST);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            NBTTagCompound tag = stack.getOrCreateSubCompound(NBT_ROOT);
            String mode = tag.getString(NBT_MODE);
            if (stack.getTagCompound() != null) {
                stack.getTagCompound().removeTag("ench");
            }
            if (player.isSneaking()) {
                tag.setString(NBT_MODE, MODE_NONE);
                player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Enchantments disabled"));
            } else {
                switch (mode) {
                    case MODE_SILK:
                        stack.addEnchantment(Enchantments.FORTUNE, 2);
                        tag.setString(NBT_MODE, MODE_FORTUNE);
                        player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Switched to Fortune II"));
                        break;

                    case MODE_FORTUNE:
                        tag.setString(NBT_MODE, MODE_NONE);
                        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Enchantments disabled"));
                        break;

                    case MODE_AOE:
                        tag.setString(NBT_MODE, MODE_NONE);
                        player.sendMessage(new TextComponentString(TextFormatting.GRAY + "Enchantments disabled"));
                        break;

                    case MODE_NONE:
                    default:
                        stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
                        tag.setString(NBT_MODE, MODE_SILK);
                        player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Switched to Silk Touch"));
                        break;
                }
            }
            player.inventory.markDirty();
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        NBTTagCompound tag = stack.getSubCompound(NBT_ROOT);
        if (tag != null) {
            String mode = tag.getString(NBT_MODE);
            return MODE_SILK.equals(mode) || MODE_FORTUNE.equals(mode);
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        NBTTagCompound tag = stack.getSubCompound(NBT_ROOT);
        String mode = (tag != null) ? tag.getString(NBT_MODE) : MODE_NONE;
        tooltip.add(TextFormatting.YELLOW + "Current Mode: " + TextFormatting.WHITE + (MODE_SILK.equals(mode) ? "Silk Touch" : MODE_FORTUNE.equals(mode) ? "Fortune II" : "None"));
        tooltip.add(TextFormatting.YELLOW + "Right-click to toggle:");
        tooltip.add((MODE_NONE.equals(mode)?TextFormatting.WHITE : TextFormatting.YELLOW) + " - None");
        tooltip.add((MODE_SILK.equals(mode)?TextFormatting.WHITE : TextFormatting.YELLOW) + " - Silk Touch");
        tooltip.add((MODE_FORTUNE.equals(mode)? TextFormatting.WHITE : TextFormatting.YELLOW) + " - Fortune II");
    }

}
