package com.fireball1725.graves.common.entity.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class GraveCapStorage implements Capability.IStorage<IGraveCapability>
{
	@Override
	public void readNBT(Capability<IGraveCapability> capability, IGraveCapability iGraveCapability, EnumFacing enumFacing, NBTBase nbtBase)
	{
		iGraveCapability.deserializeNBT((NBTTagCompound) nbtBase);
	}

	@Override
	public NBTBase writeNBT(Capability<IGraveCapability> capability, IGraveCapability iGraveCapability, EnumFacing enumFacing)
	{
		return iGraveCapability.serializeNBT();
	}
}