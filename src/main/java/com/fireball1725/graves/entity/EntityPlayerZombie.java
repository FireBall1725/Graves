package com.fireball1725.graves.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityPlayerZombie extends EntityPigZombie
{
	private GameProfile playerProfile;
	private EntityPlayer player;

	public EntityPlayerZombie(World worldIn)
	{
		super(worldIn);
	}

	public void makeAngry() {
		ReflectionHelper.setPrivateValue(EntityLivingBase.class, this, 60, new String[]{"recentlyHit", "field_70718_bc"});
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
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
