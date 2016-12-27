package com.fireball1725.graves.common.network.packets;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.util.Headstones;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SyncHeadstonesToClients implements IMessage {

    NBTTagCompound tag;

    public SyncHeadstonesToClients() {
    }

    public SyncHeadstonesToClients(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, tag);
    }

    public static class HANDLER implements IMessageHandler<SyncHeadstonesToClients, IMessage> {

        @Override
        public IMessage onMessage(SyncHeadstonesToClients message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT)
                Headstones.get(Graves.proxy.getWorld()).deserializeNBT(message.tag);
            return null;
        }
    }
}
