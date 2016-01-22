package com.fireball1725.graves.util;

import com.fireball1725.graves.client.gui.GuiScreenHeadstone;
import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                TileEntityHeadStone headstone = TileTools.getTileEntity(world, new BlockPos(x, y, z), TileEntityHeadStone.class);
                if (headstone != null) {
                    return new GuiScreenHeadstone(headstone);
                }
        }
        return null;
    }
}
