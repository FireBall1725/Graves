package com.fireball1725.graves.common.network.messages;

import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.fireball1725.graves.common.util.TileTools;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetHeadstoneName implements IMessage {
    protected BlockPos pos;
    protected String playerName;

    public MessageSetHeadstoneName() {
    }

    public MessageSetHeadstoneName(BlockPos pos, String playerName) {
        this.pos = pos;
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, playerName);
    }

    public static class HANDLER implements IMessageHandler<MessageSetHeadstoneName, IMessage> {
        @Override
        public IMessage onMessage(MessageSetHeadstoneName message, MessageContext ctx) {
			TileEntityGrave headStone = TileTools.getTileEntity(ctx.getServerHandler().playerEntity.worldObj, message.pos, TileEntityGrave.class);
			if(headStone != null)
			{
				headStone.setCustomName(message.playerName);
				headStone.markForUpdate();
			}
			return null;
        }
    }
}
