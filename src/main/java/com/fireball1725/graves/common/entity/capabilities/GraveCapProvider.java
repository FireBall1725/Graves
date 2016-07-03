package com.fireball1725.graves.common.entity.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

import static com.fireball1725.graves.common.entity.capabilities.GraveCapability.GRAVE_CAPABILITY;

public class GraveCapProvider implements ICapabilitySerializable<NBTBase>
{
	private IGraveCapability inst = GRAVE_CAPABILITY.getDefaultInstance();

	@Override
	public <T> T getCapability(Capability<T> capability,
			@Nullable
					EnumFacing enumFacing)
	{
		return capability == GRAVE_CAPABILITY ? GRAVE_CAPABILITY.<T>cast(inst) : null;
	}

	@Override
	public boolean hasCapability(Capability<?> capability,
			@Nullable
					EnumFacing enumFacing)
	{
		return capability == GRAVE_CAPABILITY;
	}

	@Override
	public void deserializeNBT(NBTBase tagCompound)
	{
		GRAVE_CAPABILITY.getStorage().readNBT(GRAVE_CAPABILITY, inst, null, tagCompound);
	}

	@Override
	public NBTBase serializeNBT()
	{
		return GRAVE_CAPABILITY.getStorage().writeNBT(GRAVE_CAPABILITY, inst, null);
	}
}