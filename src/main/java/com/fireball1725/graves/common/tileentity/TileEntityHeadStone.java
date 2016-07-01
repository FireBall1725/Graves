package com.fireball1725.graves.common.tileentity;

import com.fireball1725.graves.common.helpers.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

public class TileEntityHeadStone extends TileEntityBase
{
	private ItemStack displayStack = null;

	public TileEntityHeadStone()
	{
		super();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), serializeNBT());
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity sPacketUpdateTileEntity)
	{
		deserializeNBT(sPacketUpdateTileEntity.getNbtCompound());
	}

	@Override
	public String getCustomName()
	{
		return hasCustomName() ? this.customName : "";
	}

	public ItemStack getDisplayStack()
	{
		return displayStack;
	}

	public void setDisplayStack(ItemStack displayStack)
	{
		this.displayStack = displayStack;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		if(displayStack != null)
		{ nbtTagCompound.setTag("displayStack", displayStack.serializeNBT()); }
		return nbtTagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		displayStack = null;
		if(nbtTagCompound.hasKey("displayStack"))
		{
			LogHelper.info("Loading stack!");
			displayStack = ItemStack.loadItemStackFromNBT(nbtTagCompound.getCompoundTag("displayStack"));
		}
	}
}
