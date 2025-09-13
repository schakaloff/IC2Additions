package com.ic2additions.objects.blocks;

import com.ic2additions.tilentity.TileEntityCable;
import net.minecraft.block.Block;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.util.Reference;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockCable extends Block {
    private final String defaultName;
    private final double defaultLoss;
    private final int defaultCapacity;

    public BlockCable(String registryName, String defaultName, double defaultLoss, int defaultCapacity) {
        super(Material.CIRCUITS);
        this.defaultName = defaultName;
        this.defaultLoss = defaultLoss;
        this.defaultCapacity = defaultCapacity;

        setUnlocalizedName(Reference.MODID + "." + registryName);
        setRegistryName(Reference.MODID, registryName);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setHardness(0.2F);
        setResistance(1.0F);
    }

    @Override public boolean hasTileEntity(IBlockState state) { return true; }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        TileEntityCable te = new TileEntityCable();
        te.setDisplayName(defaultName);
        te.setLoss(defaultLoss);
        te.setCapacity(defaultCapacity);
        return te;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (world.isRemote) return;
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityCable) {
            TileEntityCable cable = (TileEntityCable) te;
            String name = defaultName;
            double loss = defaultLoss;
            int cap = defaultCapacity;
            if (stack.hasTagCompound()) {
                if (stack.getTagCompound().hasKey(TileEntityCable.NBT_NAME))
                    name = stack.getTagCompound().getString(TileEntityCable.NBT_NAME);
                if (stack.getTagCompound().hasKey(TileEntityCable.NBT_LOSS))
                    loss = stack.getTagCompound().getDouble(TileEntityCable.NBT_LOSS);
                if (stack.getTagCompound().hasKey(TileEntityCable.NBT_CAPACITY))
                    cap = stack.getTagCompound().getInteger(TileEntityCable.NBT_CAPACITY);
            }
            cable.setDisplayName(name);
            cable.setLoss(loss);
            cable.setCapacity(cap);
            cable.markDirty();
            cable.updateConnectivity();
        }
    }

    @Override
    public void onBlockAdded(World w, BlockPos pos, IBlockState state) {
        super.onBlockAdded(w, pos, state);
        if (!w.isRemote) {
            TileEntity te = w.getTileEntity(pos);
            if (te instanceof TileEntityCable) ((TileEntityCable) te).updateConnectivity();
            for (EnumFacing f : EnumFacing.VALUES) w.neighborChanged(pos.offset(f), this, pos);
        }
    }

    @Override
    public void breakBlock(World w, BlockPos pos, IBlockState state) {
        if (!w.isRemote) {
            for (EnumFacing f : EnumFacing.VALUES) w.neighborChanged(pos.offset(f), this, pos);
        }
        super.breakBlock(w, pos, state);
    }
    public String getDefaultName() { return defaultName; }
    public double getDefaultLoss() { return defaultLoss; }
    public int getDefaultCapacity() { return defaultCapacity; }
}
