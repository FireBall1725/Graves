package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class BlockGraveSlave extends BlockBase
{
	public BlockGraveSlave()
	{
		super(Material.cloth);
		setStepSound(Block.soundTypeStone);
		setHardness(2.2F);
		setResistance(5.0F);
		setTileEntity(TileEntityGraveSlave.class);
	}

	@Override
	public int getRenderType()
	{
		return -1;
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		if(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone)
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
		setBlockBoundsBasedOnState(worldIn, pos);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	@Override
	public boolean addLandingEffects(WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles)
	{
		return Blocks.BLOCK_GRAVESTONE.block.addLandingEffects(worldObj, blockPosition, iblockstate, entity, numberOfParticles);
	}

	@Override
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		return Blocks.BLOCK_GRAVESTONE.block.addHitEffects(worldObj, target, effectRenderer);
	}

	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer)
	{
		return Blocks.BLOCK_GRAVESTONE.block.addDestroyEffects(world, pos, effectRenderer);
	}
}
