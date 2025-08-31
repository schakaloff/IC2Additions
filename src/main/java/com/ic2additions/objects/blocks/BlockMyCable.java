package com.ic2additions.objects.blocks;

import com.ic2additions.init.BlockInit;
import com.ic2additions.tilentity.TileEntityMyCable;
import com.ic2additions.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class BlockMyCable extends Block {
    public static final PropertyBool UP    = PropertyBool.create("up");
    public static final PropertyBool DOWN  = PropertyBool.create("down");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST  = PropertyBool.create("west");
    public static final PropertyBool EAST  = PropertyBool.create("east");

    public BlockMyCable(String name) {
        super(Material.CIRCUITS);
        setRegistryName(Reference.MODID, name);
        setUnlocalizedName(Reference.MODID + "." + name);
        setCreativeTab(CreativeTabs.REDSTONE);

        // default state
        setDefaultState(this.blockState.getBaseState()
                .withProperty(UP, false).withProperty(DOWN, false)
                .withProperty(NORTH, false).withProperty(SOUTH, false)
                .withProperty(WEST, false).withProperty(EAST, false));
    }

    @Override public boolean hasTileEntity(IBlockState state) { return true; }
    @Override public TileEntity createTileEntity(World world, IBlockState state) { return new TileEntityMyCable(); }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, DOWN, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMyCable) {
            byte c = ((TileEntityMyCable) te).getConnectivity();
            state = state
                    .withProperty(DOWN,  (c & (1 << EnumFacing.DOWN.ordinal()))  != 0)
                    .withProperty(UP,    (c & (1 << EnumFacing.UP.ordinal()))    != 0)
                    .withProperty(NORTH, (c & (1 << EnumFacing.NORTH.ordinal())) != 0)
                    .withProperty(SOUTH, (c & (1 << EnumFacing.SOUTH.ordinal())) != 0)
                    .withProperty(WEST,  (c & (1 << EnumFacing.WEST.ordinal()))  != 0)
                    .withProperty(EAST,  (c & (1 << EnumFacing.EAST.ordinal()))  != 0);
        }
        return state;
    }

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

    @Override
    public void neighborChanged(IBlockState s, World w, BlockPos p, Block b, BlockPos from) {
        TileEntity te = w.getTileEntity(p);
        if (te instanceof TileEntityMyCable && !w.isRemote) {
            ((TileEntityMyCable) te).updateConnectivity();
        }
        super.neighborChanged(s, w, p, b, from);
    }

    @Override public boolean isOpaqueCube(IBlockState state) { return true; }
    @Override public boolean isFullCube(IBlockState state) { return true; }
    @Override public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }
    @Override public EnumBlockRenderType getRenderType(IBlockState s) { return EnumBlockRenderType.MODEL; }
    @Override public int getMetaFromState(IBlockState state) { return 0; }
    @Override public IBlockState getStateFromMeta(int meta) { return getDefaultState(); }
    @Override public int damageDropped(IBlockState state) { return 0; }
}
