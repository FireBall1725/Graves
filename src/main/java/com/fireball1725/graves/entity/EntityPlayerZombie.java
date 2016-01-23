package com.fireball1725.graves.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;

public class EntityPlayerZombie extends EntityPigZombie
{
	private GameProfile playerProfile;

	public EntityPlayerZombie(World worldIn)
	{
		super(worldIn);
	}

	public GameProfile getPlayerProfile()
	{
		return playerProfile;
	}

	public void setPlayerProfile(GameProfile playerProfile)
	{
		this.playerProfile = playerProfile;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);

		this.playerProfile = NBTUtil.readGameProfileFromNBT(nbtTagCompound.getCompoundTag("playerProfile"));
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);

		NBTTagCompound profileTag = new NBTTagCompound();
		NBTUtil.writeGameProfile(profileTag, playerProfile);
		nbtTagCompound.setTag("playerProfile", profileTag);
	}
}
