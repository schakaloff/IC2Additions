package com.ic2additions.objects;

import com.ic2additions.init.ItemInit;
import com.ic2additions.tilentity.TileEntityMyCable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockMyCable extends ItemBlock {
    public ItemBlockMyCable(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
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
    public boolean placeBlockAt(ItemStack stack, net.minecraft.entity.player.EntityPlayer player, World world, net.minecraft.util.math.BlockPos pos,
                                net.minecraft.util.EnumFacing side, float hitX, float hitY, float hitZ, net.minecraft.block.state.IBlockState newState) {
        boolean ok = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (ok && !world.isRemote) {
            if (stack.hasTagCompound() && world.getTileEntity(pos) instanceof TileEntityMyCable) {
                TileEntityMyCable te = (TileEntityMyCable) world.getTileEntity(pos);
                NBTTagCompound t = stack.getTagCompound();
                if (t.hasKey("loss")) te.setLoss(t.getDouble("loss"));
                if (t.hasKey("capacity")) te.setCapacity(t.getInteger("capacity"));
                te.markDirty();
            }
        }
        return ok;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> lines, ITooltipFlag flagIn) {
        double loss = 0.05;
        int cap = 4096;
        if (stack.hasTagCompound()) {
            NBTTagCompound t = stack.getTagCompound();
            if (t.hasKey("loss")) loss = t.getDouble("loss");
            if (t.hasKey("capacity")) cap = t.getInteger("capacity");
        }
        lines.add(TextFormatting.GRAY + String.format("Loss: %.3f EU/block", loss));
        lines.add(TextFormatting.GRAY + "Capacity: " + cap + " EU/t");
    }
}
