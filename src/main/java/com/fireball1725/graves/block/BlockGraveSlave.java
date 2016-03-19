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
	public static final PropertyBool isFoot = PropertyBool.create("isfoot");
	public static final PropertyEnum<SlaveType> slaveType = PropertyEnum.create("slavetype", SlaveType.class);

	public BlockGraveSlave()
	{
		super(Material.cloth);
		setDefaultState(blockState.getBaseState().withProperty(slaveType, SlaveType.LID).withProperty(isFoot, true).withProperty(BlockGraveStone.FACING, EnumFacing.NORTH));
		setStepSound(SoundType.STONE);
		setHardness(1.0F);
		setResistance(10000.0F);
        setTileEntity(TileEntityGraveSlave.class);
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
		if(graveSlave != null && graveSlave.getMasterBlock() != null)
		{
			EnumFacing facing = getActualState(state, source, pos).getValue(BlockGraveStone.FACING);
			if(graveSlave.getMasterBlock().offset(facing).equals(pos))
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
        if (graveSlave != null && graveSlave.getMasterBlock() != null) {
            IBlockState masterState = worldIn.getBlockState(graveSlave.getMasterBlock());
            IBlockState actualState = masterState.getBlock().getActualState(masterState, worldIn, graveSlave.getMasterBlock());
			EnumFacing facing = getActualState(state, worldIn, pos).getValue(BlockGraveStone.FACING);
            float pixel = 0.0625f;
			boolean isFoot = worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave;
            if (masterState.getBlock() instanceof BlockGraveStone) {
                boolean hasLid = actualState.getValue(BlockGraveStone.HASLID);
                if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone) {
					addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, 0f, 1f, 0.01f, 1f));
					switch (facing)
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
				else
				{
					if(hasLid)
					{
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, 1, .1425f, 1));
					}
					else
					{
						addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, .000001f, .000001f, .000001f));
					}
				}
			}
		}
	}

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
					IBlockState masterState = worldIn.getBlockState(slave.getMasterBlock());
					IBlockState masterActualState = masterState.getBlock().getActualState(masterState, worldIn, slave.getMasterBlock());
					EnumFacing masterFacing = masterActualState.getValue(BlockGraveStone.FACING);

					SlaveType st = SlaveType.NORENDER;
					if(pos.equals(master.getPos().offset(masterFacing)))
					{
						if(masterActualState.getValue(BlockGraveStone.HASLID))
						{
							st = SlaveType.LID;
						}
					}
					else if(pos.equals(master.getPos().down()))
					{
						st = SlaveType.BOX;
					}
					else if(pos.equals(master.getPos().offset(masterFacing).down()))
					{
						st = SlaveType.BOXFOOT;
					}

					return getDefaultState()
							.withProperty(slaveType, st)
							.withProperty(isFoot, !pos.equals(slave.getMasterBlock().down()))
							.withProperty(BlockGraveStone.FACING, masterFacing);
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
