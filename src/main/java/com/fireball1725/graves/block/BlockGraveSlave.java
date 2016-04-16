package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockGraveSlave extends BlockBase {
	private static final PropertyBool isFoot = PropertyBool.create("isfoot");
	private static final PropertyEnum<SlaveType> slaveType = PropertyEnum.create("slavetype", SlaveType.class);

	BlockGraveSlave()
	{
		super(Material.cloth);
		setDefaultState(blockState.getBaseState().withProperty(slaveType, SlaveType.LID).withProperty(isFoot, true).withProperty(BlockGraveStone.FACING, EnumFacing.NORTH));
		setStepSound(SoundType.STONE);
		setHardness(1.0F);
		setResistance(10000.0F);
        setTileEntity(TileEntityGraveSlave.class);
    }

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
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, slaveType, isFoot, BlockGraveStone.FACING);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		TileEntityGraveSlave slave = TileTools.getTileEntity(world, pos, TileEntityGraveSlave.class);
		if(slave == null)
		{
			return null;
		}
		return world.getBlockState(slave.getMasterBlock()).getBlock().getPickBlock(state, target, world, slave.getMasterBlock(), player);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		AxisAlignedBB selectionBox = new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, 1f);
		TileEntityGraveSlave graveSlave = TileTools.getTileEntity(source, pos, TileEntityGraveSlave.class);
		if(graveSlave != null)
		{
			SlaveType st = getActualState(state, source, pos).getValue(slaveType);
			if(st == SlaveType.LID || st == SlaveType.NORENDER)
			{
				selectionBox = new AxisAlignedBB(0, 0, 0, 1, .1425f, 1);
			}
		}
		return selectionBox;//.offset(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_)
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
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 0.01f, 1f));
				switch(facing)
				{
					case NORTH:
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, pixel, 1f, 1f));
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(1f - pixel, 0f, 0f, 1f, 1f, 1f));
						if(isFoot)
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, pixel));
						}
						else
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 1f - pixel, 1f, 1f, 1f));
						}
						break;
					case SOUTH:
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, pixel, 1f, 1f));
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(1f - pixel, 0f, 0f, 1f, 1f, 1f));
						if(!isFoot)
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, pixel));
						}
						else
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 1f - pixel, 1f, 1f, 1f));
						}
						break;
					case WEST:
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, pixel));
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 1f - pixel, 1f, 1f, 1f));
						if(isFoot)
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, pixel, 1f, 1f));
						}
						else
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(1f - pixel, 0f, 0f, 1f, 1f, 1f));
						}
						break;
					case EAST:
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 1f, pixel));
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 1f - pixel, 1f, 1f, 1f));
						if(!isFoot)
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, pixel, 1f, 1f));
						}
						else
						{
							addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(1f - pixel, 0f, 0f, 1f, 1f, 1f));
						}
						break;
				}
			}
			if(type == SlaveType.NORENDER)
			{
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, 0, 0, 0));
			}
			if(type == SlaveType.LID)
			{
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, 1, .1425f, 1));
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
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

    @Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
    }

    @Override
	public boolean isFullCube(IBlockState state)
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

	private enum SlaveType implements IStringSerializable
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
