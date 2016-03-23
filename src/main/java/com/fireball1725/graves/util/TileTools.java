package com.fireball1725.graves.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class TileTools {
    /**
     * Gets tile entity at @BlockPos location
     *
	 * @param world    The World
	 * @param blockPos Block Position
	 * @param tClass   Class of TileEntity
	 * @param <T> Generic Class Type
	 * @return The TileEntity or Null if not the TileEntity
	 */
    public static <T> T getTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
		return !tClass.isInstance(tileEntity) ? null : tClass.cast(tileEntity);
	}
}
