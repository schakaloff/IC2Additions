package com.ic2additions.objects.blocks;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.tilentity.TileEntityMolecularAssembler;
import com.ic2additions.util.Reference;
import ic2.core.IC2;
import ic2.core.IHasGui;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class MolecularAssemblerBlock extends Block {
    public MolecularAssemblerBlock(String name){
        super(Material.CIRCUITS);
        setUnlocalizedName(Reference.MODID + "." + name);
        setRegistryName(Reference.MODID,name);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
        setHardness(0.2F);
        setResistance(1.0F);
        setLightOpacity(0);

    }
    @Override public boolean hasTileEntity(IBlockState state) { return true; }
    @Override public TileEntity createTileEntity(World world, IBlockState state) { return new TileEntityMolecularAssembler(); }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand, EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        // optional: let sneak-right-click pass through for wrenching, etc.
        if (player.isSneaking()) return false;

        if (world.isRemote) {
            // client: consume the click so the hand animates immediately
            return true;
        }

        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IHasGui) {
            IC2.platform.launchGui((EntityPlayerMP) player, (IHasGui) te);
            return true;
        }
        return false;
    }
}
