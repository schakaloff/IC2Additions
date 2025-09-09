package com.ic2additions.objects.blocks;

import com.ic2additions.init.IC2AdditionsCreativeTabs;
import com.ic2additions.tilentity.TileEntityEUtoRF;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockEUtoRF extends Block implements ITileEntityProvider {

    private final String name;

    public BlockEUtoRF(String name) {
        super(Material.IRON);
        this.name = name;
        setUnlocalizedName(name);
        setRegistryName(name);
        setHardness(2.0F);
        setResistance(10.0F);
        setCreativeTab(IC2AdditionsCreativeTabs.tab);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityEUtoRF();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileEntityEUtoRF) {
                TileEntityEUtoRF converter = (TileEntityEUtoRF) te;
                playerIn.sendMessage(new TextComponentString(
                        "EU Buffer: " + converter.getEuBuffer() +
                                " | RF Stored: " + converter.getRfStorage().getEnergyStored()));
            }
        }
        return true;
    }
}
