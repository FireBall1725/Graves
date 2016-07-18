package com.fireball1725.graves.common.network.packets;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.helpers.GuiHelper;
import io.netty.buffer.ByteBuf;
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
                Graves.proxy.openGui(GuiHelper.STARTUPSCREEN);
                return null;
            } else {
                return new OpenStartupScreenPacket();
            }
        }
    }
}
