package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.tileentity.TileEntityGraveStone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockGraveLegacy extends BlockContainer
{
	private static final PropertyDirection FACING = BlockHorizontal.FACING;
	private static final PropertyBool HASLID = PropertyBool.create("haslid");

	public BlockGraveLegacy()
	{
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(HASLID, true).withProperty(FACING, EnumFacing.NORTH));
		GameRegistry.registerTileEntity(TileEntityGraveStone.class, "tileentity.graves.TileEntityGraveStone");
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityGraveStone();
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HASLID, FACING);
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
}
