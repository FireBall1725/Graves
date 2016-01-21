package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by FusionLord on 1/20/2016.
 */
public class BlockHeadStone extends BlockBase
{
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

	protected BlockHeadStone()
	{
		super(Material.rock);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		this.setHardness(10F);
		this.setResistance(10000.0F);
		this.setTileEntity(TileEntityHeadStone.class);
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		switch(state.getValue(FACING))
		{
			case NORTH:
				setBlockBounds(.1f, 0f, 0f, .9f, .95f, .3f);
				break;
			case SOUTH:
				setBlockBounds(.1f, 0f, .7f, .9f, .95f, 1f);
				break;
			case WEST:
				setBlockBounds(0f, 0f, .1f, .3f, .95f, .9f);
				break;
			case EAST:
				setBlockBounds(.7f, 0f, .1f, 1f, .95f, .9f);
				break;
		}
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		setBlockBoundsBasedOnState(worldIn, pos);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

}
