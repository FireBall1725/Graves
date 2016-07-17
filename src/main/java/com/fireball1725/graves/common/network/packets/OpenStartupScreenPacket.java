package com.fireball1725.graves.common.network.packets;

import com.fireball1725.graves.client.gui.GuiStartUp;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class OpenStartupScreenPacket implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class HANDLER implements IMessageHandler<OpenStartupScreenPacket, OpenStartupScreenPacket> {

        @Override
        public OpenStartupScreenPacket onMessage(OpenStartupScreenPacket message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiStartUp());
                return null;
            } else {
                return new OpenStartupScreenPacket();
            }
        }
    }
}
