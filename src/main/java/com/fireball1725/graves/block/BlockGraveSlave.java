package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockGraveSlave extends BlockBase {
	public static final PropertyBool isFoot = PropertyBool.create("isfoot");
	public static final PropertyEnum<SlaveType> slaveType = PropertyEnum.create("slavetype", SlaveType.class);

	public BlockGraveSlave()
	{
		super(Material.cloth);
		setDefaultState(blockState.getBaseState().withProperty(slaveType, SlaveType.LID).withProperty(isFoot, true).withProperty(BlockGraveStone.FACING, EnumFacing.NORTH));
		setStepSound(soundTypeStone);
		setHardness(1.0F);
		setResistance(10000.0F);
		setTileEntity(TileEntityGraveSlave.class);
    }

	@SuppressWarnings("Duplicates")
	public static IBlockState getActualStatePre(IBlockState state, IBlockAccess worldIn, BlockPos pos, BlockPos masterPos)
	{
		if(masterPos != null)
		{
			TileEntityGraveStone master = TileTools.getTileEntity(worldIn, masterPos, TileEntityGraveStone.class);
			if(master != null)
			{
				IBlockState masterState = master.getBlockState();
				EnumFacing masterFacing = masterState.getValue(BlockGraveStone.FACING);

				SlaveType st = SlaveType.NORENDER;
				if(pos.offset(masterFacing.getOpposite()).up().equals(master.getPos()))
				{
					st = SlaveType.BOXFOOT;
				}
				if(pos.up().equals(master.getPos()))
				{
					st = SlaveType.BOX;
				}
				if(master.getPos().offset(masterFacing).equals(pos) && master.getHasLid())
				{
					st = SlaveType.LID;
				}

				return state
						.withProperty(slaveType, st)
						.withProperty(isFoot, !pos.up().equals(master.getPos()))
						.withProperty(BlockGraveStone.FACING, master.getFacing());
			}
		}
		return null;
	}

	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, slaveType, isFoot, BlockGraveStone.FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 3;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntityGraveSlave slave = TileTools.getTileEntity(world, pos, TileEntityGraveSlave.class);
		if(slave == null)
		{
			return null;
		}
		return world.getBlockState(slave.getMasterBlock()).getBlock().getPickBlock(target, world, slave.getMasterBlock(), player);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
	{
		AxisAlignedBB selectionBox;
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone)
		{
			selectionBox = AxisAlignedBB.fromBounds(0f, 0f, 0f, 1f, 1f, 1f);
		}
		else
		{
			selectionBox = AxisAlignedBB.fromBounds(0, 0, 0, 1, .1425f, 1);
		}
		return selectionBox.offset(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityGraveSlave graveSlave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
		if(graveSlave != null && graveSlave.getMasterBlock() != null && graveSlave.getMasterBlock().up().getY() == pos.getY())
		{
			setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
		}
		else
		{
			setBlockBounds(0, 0, 0, 1, .1425f, 1);
		}
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		TileEntityGraveSlave graveSlave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
		if(graveSlave != null)
		{
			IBlockState actualState = getActualState(state, worldIn, pos);
			EnumFacing facing = actualState.getValue(BlockGraveStone.FACING);

			float pixel = 0.0625f;
			boolean isFoot = worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave;
			SlaveType type = actualState.getValue(slaveType);
			if(type == SlaveType.BOX || type == SlaveType.BOXFOOT)
			{
				setBlockBounds(0f, 0f, 0f, 1f, 0.01f, 1f);
				super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
				switch(facing)
				{
					case NORTH:
						setBlockBounds(0f, 0f, 0f, pixel, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						setBlockBounds(1f - pixel, 0f, 0f, 1f, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						if(isFoot)
						{
							setBlockBounds(0f, 0f, 0f, 1f, 1f, pixel);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						else
						{
							setBlockBounds(0f, 0f, 1f - pixel, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						break;
					case SOUTH:
						setBlockBounds(0f, 0f, 0f, pixel, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						setBlockBounds(1f - pixel, 0f, 0f, 1f, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						if(!isFoot)
						{
							setBlockBounds(0f, 0f, 0f, 1f, 1f, pixel);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						else
						{
							setBlockBounds(0f, 0f, 1f - pixel, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						break;
					case WEST:
						setBlockBounds(0f, 0f, 0f, 1f, 1f, pixel);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						setBlockBounds(0f, 0f, 1f - pixel, 1f, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						if(isFoot)
						{
							setBlockBounds(0f, 0f, 0f, pixel, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						else
						{
							setBlockBounds(1f - pixel, 0f, 0f, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						break;
					case EAST:
						setBlockBounds(0f, 0f, 0f, 1f, 1f, pixel);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						setBlockBounds(0f, 0f, 1f - pixel, 1f, 1f, 1f);
						super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						if(!isFoot)
						{
							setBlockBounds(0f, 0f, 0f, pixel, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						else
						{
							setBlockBounds(1f - pixel, 0f, 0f, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
						}
						break;
				}
			}
			if(type == SlaveType.NORENDER)
			{
				setBlockBounds(0, 0, 0, 0, 0, 0);
				super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
			}
			if(type == SlaveType.LID)
			{
				setBlockBounds(0, 0, 0, 1, .1425f, 1);
				super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
			}
		}
	}

	@SuppressWarnings("Duplicates")
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityGraveSlave slave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
		if(slave != null)
		{
			if(slave.getMasterBlock() != null)
			{
				TileEntityGraveStone master = TileTools.getTileEntity(worldIn, slave.getMasterBlock(), TileEntityGraveStone.class);
				if(master != null)
				{
					IBlockState masterState = master.getBlockState();
					EnumFacing masterFacing = masterState.getValue(BlockGraveStone.FACING);

					SlaveType st = SlaveType.NORENDER;
					if(pos.offset(masterFacing.getOpposite()).up().equals(master.getPos()))
					{
						st = SlaveType.BOXFOOT;
					}
					if(pos.up().equals(master.getPos()))
					{
						st = SlaveType.BOX;
					}
					if(master.getPos().offset(masterFacing).equals(pos) && master.getHasLid())
					{
						st = SlaveType.LID;
					}

					return getDefaultState()
							.withProperty(slaveType, st)
							.withProperty(isFoot, !pos.up().equals(master.getPos()))
							.withProperty(BlockGraveStone.FACING, master.getFacing());
				}
			}
		}
		return super.getActualState(state, worldIn, pos);
	}



    @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @Override
	public boolean isFullBlock()
	{
		return false;
    }

    @Override
	public boolean isFullCube()
	{
		return false;
    }

    @Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }

	public enum SlaveType implements IStringSerializable
	{
		LID,
		BOX,
		BOXFOOT,
		NORENDER,;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}
}
