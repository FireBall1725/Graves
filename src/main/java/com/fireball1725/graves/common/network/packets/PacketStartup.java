package com.fireball1725.graves.common.network.packets;

import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.entity.capabilities.IGraveCapability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStartup implements IMessage {
    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class HANDLER implements IMessageHandler<PacketStartup, IMessage> {

        @Override
        public IMessage onMessage(PacketStartup message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            if (player.hasCapability(GraveCapability.GRAVE_CAPABILITY, null)) {
                IGraveCapability grave = player.getCapability(GraveCapability.GRAVE_CAPABILITY, null);
                grave.setSeenStartUp();
            }
            return null;
        }
    }
}
