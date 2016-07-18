package com.fireball1725.graves.common.entity.capabilities;

import com.fireball1725.graves.Graves;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GraveCapDefaultImpl implements IGraveCapability
{
    private ItemStack displayStack;
    private boolean hasSeenStartUp = false;

    @Override
    public boolean hasSeenStartUp() {
        return hasSeenStartUp;
    }

    @Override
    public void setSeenStartUp() {
        hasSeenStartUp = true;
    }

    @Override
    public ItemStack getGraveItemStack() {
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
        NBTTagCompound tag = new NBTTagCompound();

        if (displayStack != null) {
            tag.setTag("displayTag", displayStack.writeToNBT(new NBTTagCompound()));
        }

        tag.setBoolean("hasSeenStartUp", hasSeenStartUp);

        Graves.logger.info("saving cap");
        return tag;
    }

	@Override
	public void deserializeNBT(NBTTagCompound tagCompound)
	{
        if (tagCompound.hasKey("displayTag"))
            displayStack = ItemStack.loadItemStackFromNBT(tagCompound);
        if (tagCompound.hasKey("hasSeenStartUp"))
            hasSeenStartUp = tagCompound.getBoolean("hasSeenStartUp");
    }
}