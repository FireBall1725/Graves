package com.fireball1725.graves.tileentity;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by FusionLord on 1/20/2016.
 */
public class TileEntityHeadStone extends TileEntityBase
{
	private String playerName = "";
	private String eulogy = "";

	public TileEntityHeadStone()
	{
		super();
	}

	public String getPlayerName()
	{
		return this.playerName;
	}

	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	public String getEulogy()
	{
		return eulogy;
	}

	public void setEulogy(String eulogy)
	{
		this.eulogy = eulogy;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);

		this.playerName = nbtTagCompound.getString("playerName");
		this.eulogy = nbtTagCompound.getString("eulogy");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);

		nbtTagCompound.setString("playerName", this.playerName);
		nbtTagCompound.setString("eulogy", this.eulogy);
	}
}
