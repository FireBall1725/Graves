package com.fireball1725.graves.common.entity.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GraveCapDefaultImpl implements IGraveCapability
{
	ItemStack displayStack;

	@Override
	public ItemStack getGraveItemStack()
	{
		return displayStack;
	}

	@Override
	public void setGraveItemStack(ItemStack displayStack)
	{
		this.displayStack = displayStack;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		if(displayStack != null)
		{ return displayStack.writeToNBT(new NBTTagCompound()); }
		else
		{ return new NBTTagCompound(); }
	}

	@Override
	public void deserializeNBT(NBTTagCompound tagCompound)
	{
		if(!tagCompound.hasNoTags())
		{
			displayStack = ItemStack.loadItemStackFromNBT(tagCompound);
		}
	}
}