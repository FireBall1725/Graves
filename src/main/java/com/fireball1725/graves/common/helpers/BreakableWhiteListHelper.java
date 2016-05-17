package com.fireball1725.graves.common.helpers;

import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.List;

public class BreakableWhiteListHelper {
    private static List<IBlockState> blocksWhiteList = new ArrayList<IBlockState>();

    public static void addBlock(IBlockState blockState) {
        blocksWhiteList.add(blockState);
    }

    public static boolean checkBlock(IBlockState blockState) {
        return blocksWhiteList.contains(blockState);
    }
}
