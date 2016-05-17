package com.fireball1725.graves.client.gui;

import com.fireball1725.graves.common.network.PacketHandler;
import com.fireball1725.graves.common.network.messages.MessageSetHeadstoneName;
import com.fireball1725.graves.common.tileentity.TileEntityHeadStone;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;

import java.io.IOException;

/**
 * Created by FusionLord on 1/21/2016.
 */
public class GuiScreenHeadstone extends GuiScreen {
    protected TileEntityHeadStone headStone;
    protected String name;

    public GuiScreenHeadstone(TileEntityHeadStone headStone) {
        super();
        this.headStone = headStone;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.width = 176;
        this.height = 69;
        name = headStone.getCustomName();
    }

    @Override
    public void onGuiClosed() {
        PacketHandler.INSTANCE.sendToServer(new MessageSetHeadstoneName(headStone.getPos(), name));
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        switch (keyCode) {
            case 14:
                if (name.length() > 0) {
                    if (name.length() > 2 && name.substring(name.length() - 2).equalsIgnoreCase("\\n")) {
                        name = name.substring(0, name.length() - 2);
                    } else {
                        name = name.substring(0, name.length() - 1);
                    }
                }
                break;
            case 28:
            case 156:
                name = name + "\\n";
                break;
            default:

                if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                    name = name + Character.toString(typedChar);
                }
        }
        headStone.setCustomName(name);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }
}
