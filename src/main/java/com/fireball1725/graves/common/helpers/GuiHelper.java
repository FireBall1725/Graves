package com.fireball1725.graves.common.helpers;

import com.fireball1725.graves.client.gui.GuiStartUp;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHelper implements IGuiHandler {
    public static final int STARTUPSCREEN = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0:
                return new GuiStartUp();
        }
        return null;
    }
}
