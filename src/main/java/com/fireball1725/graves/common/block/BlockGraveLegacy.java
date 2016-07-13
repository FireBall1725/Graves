package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGraveLegacy extends BlockBase
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool HASLID = PropertyBool.create("haslid");

	public BlockGraveLegacy()
	{
		super(Material.CLOTH);
		setDefaultState(this.blockState.getBaseState().withProperty(HASLID, true).withProperty(FACING, EnumFacing.NORTH));
		this.setTileEntity(TileEntityGrave.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HASLID, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return Blocks.BLOCK_GRAVE.block.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
	}
}
