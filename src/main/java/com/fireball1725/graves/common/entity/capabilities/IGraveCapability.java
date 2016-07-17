package com.fireball1725.graves.common.entity.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public interface IGraveCapability
{
    boolean hasSeenStartUp();

    void setSeenStartUp();

	ItemStack getGraveItemStack();

	void setGraveItemStack(ItemStack displayStack);

	NBTTagCompound serializeNBT();

	void deserializeNBT(NBTTagCompound tagCompound);
}