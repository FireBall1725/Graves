package com.fireball1725.graves.helpers;

import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class SafeBlockReplacer {


    private static boolean CheckBlock(World world, BlockPos blockPos, boolean forceAir) {
        IBlockState blockState = world.getBlockState(blockPos);

        if (blockState == null)
            return true;

        if (blockState.getBlock() instanceof BlockAir && forceAir)
            return true;
        else if (forceAir)
            return false;

        if (blockState.getBlock().hasTileEntity(blockState))
            return false;

        if (blockState.getBlock().getBlockHardness(null, null) == -1.0F)
            return false;

        return true;
    }

    private static boolean CheckGraveSite(World world, BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        boolean placeGrave = true;

        if (!CheckBlock(world, new BlockPos(x, y, z), true))
            placeGrave = false;

        if (!CheckBlock(world, new BlockPos(x + 1, y, z), true))
            placeGrave = false;

        if (!CheckBlock(world, new BlockPos(x, y - 1, z), false))
            placeGrave = false;

        if (!CheckBlock(world, new BlockPos(x + 1, y - 1, z), false))
            placeGrave = false;

        return placeGrave;
    }

    public static BlockPos GetSafeGraveSite(World world, BlockPos blockPos) {
        BlockPos finalBlockPos = CheckSafeGraveSite(world, blockPos);

        if (finalBlockPos == null) {
            LogHelper.info(">>> Unable to find place to put grave...");

            finalBlockPos = world.getTopSolidOrLiquidBlock(blockPos);
            finalBlockPos = CheckSafeGraveSite(world, finalBlockPos);
        }

        if (finalBlockPos == null) {
            LogHelper.info(">>> Sorry, still can't find a good place...");

            finalBlockPos = blockPos;
        }

        return finalBlockPos;
    }

    public static BlockPos CheckSafeGraveSite(World world, BlockPos blockPos) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();

        if (y < 1) {
            y = 9;
        }

        if (y > 256) {
            y = 246;
        }

        int blockX = x;
        int blockY = y;
        int blockZ = z;
        for (int searchY = 0; searchY < 17; searchY++) {
            if (searchY != 0 && searchY % 2 == 0) {
                blockY = y - searchY / 2;
            } else if (searchY != 0) {
                blockY = y + Math.round(searchY / 2) + 1;
            } else {
                blockY = y;
            }

            for (int searchX = 0; searchX < 17; searchX++) {
                if (searchX != 0 && searchX % 2 == 0) {
                    blockX = x - searchX / 2;
                } else if (searchX != 0) {
                    blockX = x + Math.round(searchX / 2) + 1;
                } else {
                    blockX = x;
                }

                for (int searchZ = 0; searchZ < 17; searchZ++) {
                    if (searchZ != 0 && searchZ % 2 == 0) {
                        blockZ = z - searchZ / 2;
                    } else if (searchZ != 0) {
                        blockZ = z + Math.round(searchZ / 2) + 1;
                    } else {
                        blockZ = z;
                    }

                    if (CheckGraveSite(world, new BlockPos(blockX, blockY, blockZ)))
                        return new BlockPos(blockX, blockY, blockZ);
                }
            }
        }

        return null;
    }
}
