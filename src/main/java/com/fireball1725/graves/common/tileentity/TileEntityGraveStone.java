package com.fireball1725.graves.common.tileentity;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

import java.util.List;

public class TileEntityGraveStone extends TileEntity implements ITickable
{
	private List<ReplaceableBlock> blocks = Lists.newArrayList();
	private NBTTagCompound tagCompound;

	@Override
	public void update()
	{
		for(ReplaceableBlock block : blocks)
		{
			if(block.getPos().equals(getPos()))
			{ continue; }
			block.placeBlock(worldObj);
		}
		worldObj.setBlockState(getPos(), Blocks.BLOCK_GRAVE.block.getDefaultState(), 3);
		worldObj.removeTileEntity(getPos());
		TileEntityGrave grave = new TileEntityGrave();
		grave.readFromNBT(tagCompound);
		worldObj.setTileEntity(getPos(), grave);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		tagCompound = compound;
		Graves.logger.info("Loading Legacy Grave!");
		if(compound.hasKey("replaceableBlocks"))
		{
			NBTTagCompound replaceableTag = compound.getCompoundTag("replaceableBlocks");
			int size = replaceableTag.getInteger("size");
			for(int i = 0; i < size; i++)
			{
				ReplaceableBlock block = ReplaceableBlock.readNBT((NBTTagCompound) replaceableTag.getTag("block:" + i));
				if(block != null)
				{
					blocks.add(block);
				}
			}
		}
	}
}
