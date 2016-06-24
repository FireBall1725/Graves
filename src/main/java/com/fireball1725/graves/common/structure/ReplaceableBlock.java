package com.fireball1725.graves.common.structure;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ReplaceableBlock
{
	private BlockPos pos;
	private IBlockState state;
	private NBTTagCompound tagCompound;

	public ReplaceableBlock(IBlockState state, BlockPos pos, NBTTagCompound tagCompound)
	{
		this.state = state;
		this.pos = pos;
		this.tagCompound = tagCompound;
	}

	public static ReplaceableBlock readNBT(NBTTagCompound tag)
	{
		if(!tag.hasKey("blockID") || !tag.hasKey("blockMeta"))
		{ return null; }
		IBlockState state;
		NBTTagCompound tileTag = null;

		Block block = Block.getBlockById(tag.getInteger("blockID"));
		state = block.getStateFromMeta(tag.getInteger("blockMeta"));
		BlockPos pos = BlockPos.fromLong(tag.getLong("blockPos"));

		if(tag.hasKey("tileData"))
		{
			tileTag = tag.getCompoundTag("tileData");
		}

		return new ReplaceableBlock(state, pos, tileTag);
	}

	public boolean placeBlock(World world)
	{
		if (state == null) return false;
		world.setBlockState(pos, state, 3);
		Block block = state.getBlock();
		if (block instanceof ITileEntityProvider && tagCompound != null)
		{
			world.setTileEntity(pos, TileEntity.func_190200_a(world, tagCompound));
		}
		return true;
	}

	public NBTTagCompound writeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		if (state != null)
		{
			tag.setInteger("blockID", Block.REGISTRY.getIDForObject(state.getBlock()));
			tag.setInteger("blockMeta", state.getBlock().getMetaFromState(state));
			tag.setLong("blockPos", pos.toLong());
		}
		if (tagCompound != null)
		{
			tag.setTag("tileData", tagCompound);
		}
		return tag;
	}
}
