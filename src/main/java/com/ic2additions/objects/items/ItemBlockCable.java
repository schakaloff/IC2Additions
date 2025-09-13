package com.ic2additions.objects.items;

import com.ic2additions.init.ItemInit;
import com.ic2additions.objects.blocks.BlockCable;
import com.ic2additions.tilentity.TileEntityCable;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockCable extends ItemBlock {

    public ItemBlockCable(Block block) {
        super(block);
        setRegistryName(block.getRegistryName());
        ItemInit.ITEMS.add(this);
    }

    public static ItemStack withParams(Block block, String name, double loss, int capacity) {
        ItemStack s = new ItemStack(block);
        NBTTagCompound t = new NBTTagCompound();
        t.setString(TileEntityCable.NBT_NAME, name);
        t.setDouble(TileEntityCable.NBT_LOSS, loss);
        t.setInteger(TileEntityCable.NBT_CAPACITY, capacity);
        s.setTagCompound(t);
        return s;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World w, List<String> lines, ITooltipFlag flag) {
        String name = TileEntityCable.DEFAULT_NAME;
        double loss = TileEntityCable.DEFAULT_LOSS;
        int cap = TileEntityCable.DEFAULT_CAPACITY;

        if (stack.hasTagCompound()) {
            NBTTagCompound t = stack.getTagCompound();
            if (t.hasKey(TileEntityCable.NBT_NAME))     name = t.getString(TileEntityCable.NBT_NAME);
            if (t.hasKey(TileEntityCable.NBT_LOSS))     loss = t.getDouble(TileEntityCable.NBT_LOSS);
            if (t.hasKey(TileEntityCable.NBT_CAPACITY)) cap = t.getInteger(TileEntityCable.NBT_CAPACITY);
        } else if (block instanceof BlockCable) {
            BlockCable b = (BlockCable) block;
            name = b.getDefaultName();
            loss = b.getDefaultLoss();
            cap = b.getDefaultCapacity();
        }
        lines.add(TextFormatting.GRAY + "Type: " + name);
        lines.add(TextFormatting.GRAY + String.format("Loss: %.3f EU/block", loss));
        lines.add(TextFormatting.GRAY + "Capacity: " + cap + " EU/t");
    }
}
