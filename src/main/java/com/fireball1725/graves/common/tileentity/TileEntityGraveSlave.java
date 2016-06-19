package com.fireball1725.graves.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TileEntityGraveSlave extends TileEntityBase {
    protected BlockPos masterBlock;

    public TileEntityGraveSlave() {
        super();
    }

    public BlockPos getMasterBlock() {
        return masterBlock;
    }

    public void setMasterBlock(BlockPos masterBlock) {
        this.masterBlock = masterBlock;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("masterBlock")) {
            masterBlock = BlockPos.fromLong(compound.getLong("masterBlock"));
        }
    }

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		if (masterBlock != null) {
            compound.setLong("masterBlock", masterBlock.toLong());
        }
		return super.writeToNBT(compound);
	}
}
