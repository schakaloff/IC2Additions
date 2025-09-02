package com.ic2additions.objects.blocks;
import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.tilentity.TileEntityMyCable;
import com.ic2additions.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class BlockMyCable extends Block {

    public BlockMyCable(String name) {
        super(Material.CIRCUITS);
        setUnlocalizedName(Reference.MODID + "." + name);
        setRegistryName(Reference.MODID,name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setHardness(0.2F);
        setResistance(1.0F);
    }
    @Override public boolean hasTileEntity(IBlockState state) { return true; }
    @Override public TileEntity createTileEntity(World world, IBlockState state) { return new TileEntityMyCable(); }

    @Override
    public void onBlockAdded(World w, BlockPos pos, IBlockState state) {
        super.onBlockAdded(w, pos, state);
        if (!w.isRemote) {
            TileEntity te = w.getTileEntity(pos);
            if (te instanceof TileEntityMyCable) ((TileEntityMyCable) te).updateConnectivity();
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
}