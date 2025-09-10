package com.ic2additions.objects.blocks;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.tilentity.TileEntityMolecularAssembler;
import com.ic2additions.util.Reference;
import ic2.core.IC2;
import ic2.core.IHasGui;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class MolecularAssemblerBlock extends Block implements ITileEntityProvider {
    public MolecularAssemblerBlock(String name){
        super(Material.CIRCUITS);
        setUnlocalizedName(Reference.MODID + "." + name);
        setRegistryName(Reference.MODID, name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setHardness(0.2F);
        setResistance(1.0F);
        setLightOpacity(0);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMolecularAssembler();
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityMolecularAssembler();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IHasGui) {
            if (!world.isRemote && player instanceof EntityPlayerMP) {
                IC2.platform.launchGui((EntityPlayerMP) player, (IHasGui) te);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }


}
