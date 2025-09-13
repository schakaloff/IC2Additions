package com.ic2additions.objects.items;

import com.ic2additions.init.ItemInit;
import com.ic2additions.tilentity.TileEntityPlasmaCable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockPlasmaCable extends ItemBlock {
    public ItemBlockPlasmaCable(Block block) {
        super(block);
        setRegistryName(block.getRegistryName()); // 🔴 This is required
        ItemInit.ITEMS.add(this);
    }
    public static ItemStack withParams(Block block, double loss, int capacity) {
        ItemStack s = new ItemStack(block);
        NBTTagCompound t = new NBTTagCompound();
        t.setDouble("loss", loss);
        t.setInteger("capacity", capacity);
        s.setTagCompound(t);
        return s;
    }
    @Override
    public boolean placeBlockAt(ItemStack stack, net.minecraft.entity.player.EntityPlayer player, World world, net.minecraft.util.math.BlockPos pos, net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ, net.minecraft.block.state.IBlockState newState) {
        boolean ok = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (ok && !world.isRemote && stack.hasTagCompound() && world.getTileEntity(pos) instanceof TileEntityPlasmaCable) {
            TileEntityPlasmaCable te = (TileEntityPlasmaCable) world.getTileEntity(pos);
            NBTTagCompound t = stack.getTagCompound();
            if (t.hasKey("loss")) te.setLoss(t.getDouble("loss"));
            if (t.hasKey("capacity")) te.setCapacity(t.getInteger("capacity"));
            te.markDirty();
        }
        return ok;
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World w, List<String> lines, ITooltipFlag flag) {
        double loss = TileEntityPlasmaCable.DEFAULT_LOSS;
        int cap = TileEntityPlasmaCable.DEFAULT_CAPACITY;
        if (stack.hasTagCompound()) {
            NBTTagCompound t = stack.getTagCompound();
            if (t.hasKey("loss")) loss = t.getDouble("loss");
            if (t.hasKey("capacity")) cap = t.getInteger("capacity");
        }
        lines.add(TextFormatting.GRAY + String.format("Loss: %.3f EU/block", loss));
        lines.add(TextFormatting.GRAY + "Capacity: " + cap + " EU/t");
    }
}