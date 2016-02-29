package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;

public class BlockGraveSlave extends BlockBase {
	public enum SlaveType implements IStringSerializable
	{
		LID,
		BOX,
		BOXFOOT,
		NORENDER,
		;

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	public static final PropertyBool isFoot = PropertyBool.create("isFoot");
	public static final PropertyEnum<SlaveType> slaveType = PropertyEnum.create("slaveType", SlaveType.class);

    public BlockGraveSlave() {
        super(Material.cloth);
		setDefaultState(blockState.getBaseState().withProperty(slaveType, SlaveType.LID).withProperty(isFoot, true).withProperty(BlockGraveStone.FACING, EnumFacing.NORTH));
        setStepSound(Block.soundTypeStone);
        setHardness(1.0F);
        setResistance(10000.0F);
        setTileEntity(TileEntityGraveSlave.class);
    }

	@Override
	protected BlockState createBlockState() {
		PropertyDirection test = BlockGraveStone.FACING;
		return new BlockState(this, slaveType, isFoot, BlockGraveStone.FACING);
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
    public int getRenderType() {
        return 3;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos, EntityPlayer player) {
        TileEntityGraveSlave slave = TileTools.getTileEntity(world, pos, TileEntityGraveSlave.class);
        if (slave == null) {
            return null;
        }
        return world.getBlockState(slave.getMasterBlock()).getBlock().getPickBlock(target, world, slave.getMasterBlock(), player);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        AxisAlignedBB selectionBox;
        if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone) {
            selectionBox = AxisAlignedBB.fromBounds(0f, 0f, 0f, 1f, 1f, 1f);
        } else {
            selectionBox = AxisAlignedBB.fromBounds(0, 0, 0, 1, .1425f, 1);
        }
        return selectionBox.offset(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        TileEntityGraveSlave graveSlave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
        if (graveSlave != null && graveSlave.getMasterBlock() != null && graveSlave.getMasterBlock().up().getY() == pos.getY()) {
            setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
        } else {
            setBlockBounds(0, 0, 0, 1, .1425f, 1);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
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
                    setBlockBounds(0f, 0f, 0f, 1f, 0.01f, 1f);
                    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
					switch (facing)
					{
						case NORTH:
							setBlockBounds(0f, 0f, 0f, pixel, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
							setBlockBounds(1f-pixel, 0f, 0f, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
							if (isFoot)
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
							setBlockBounds(1f-pixel, 0f, 0f, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
							if (!isFoot)
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
							setBlockBounds(0f, 0f, 1f-pixel, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
							if (isFoot)
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
							setBlockBounds(0f, 0f, 1f-pixel, 1f, 1f, 1f);
							super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
							if (!isFoot)
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
                } else {
                    if (hasLid) {
                        setBlockBounds(0, 0, 0, 1, .1425f, 1);
                        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
                    } else {
                        setBlockBounds(0, 0, 0, .000001f, .000001f, .000001f);
                        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
                    }
                }
            }
        }
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntityGraveSlave slave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
		if (slave != null)
		{
			if (slave.getMasterBlock() != null)
			{
				TileEntityGraveStone master = TileTools.getTileEntity(worldIn, slave.getMasterBlock(), TileEntityGraveStone.class);
				if (master != null)
				{
					IBlockState masterState = worldIn.getBlockState(slave.getMasterBlock());
					IBlockState masterActualState = masterState.getBlock().getActualState(masterState, worldIn, slave.getMasterBlock());
					EnumFacing masterFacing = masterActualState.getValue(BlockGraveStone.FACING);

					SlaveType st = SlaveType.NORENDER;
					if (pos.equals(master.getPos().offset(masterFacing)))
					{
						if (masterActualState.getValue(BlockGraveStone.HASLID))
						{
							st = SlaveType.LID;
						}
					}
					else if (pos.equals(master.getPos().down()))
					{
						st = SlaveType.BOX;
					}
					else if (pos.equals(master.getPos().down().offset(masterFacing)))
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
    public boolean addLandingEffects(WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return super.addLandingEffects(worldObj, blockPosition, iblockstate, entity, numberOfParticles);
    }

    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return super.addHitEffects(worldObj, target, effectRenderer);
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        return super.addDestroyEffects(world, pos, effectRenderer);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }
}
