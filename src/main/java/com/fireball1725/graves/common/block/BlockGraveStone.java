package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.tileentity.TileEntityGraveStone;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

public class BlockGraveStone extends BlockBase
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool HASLID = PropertyBool.create("haslid");

	public BlockGraveStone()
	{
		super(Material.CLOTH);
		this.setTileEntity(TileEntityGraveStone.class);
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HASLID, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]).withProperty(HASLID, false);
	}

	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}
}
