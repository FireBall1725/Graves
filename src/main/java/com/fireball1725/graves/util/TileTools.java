package com.fireball1725.graves.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class TileTools {
    /**
     * Gets tile entity at @BlockPos location
     *
     * @param world    World
     * @param blockPos Block Position
     * @param tClass   Class of Tile Entity
     * @param <T>
     * @return Null is not the Tile Entity or the instance of the Tile Entity
     */
    public static <T> T getTileEntity(IBlockAccess world, BlockPos blockPos, Class<T> tClass) {
        TileEntity tileEntity = world.getTileEntity(blockPos);
        return !tClass.isInstance(tileEntity) ? null : (T) tileEntity;
    }
}
