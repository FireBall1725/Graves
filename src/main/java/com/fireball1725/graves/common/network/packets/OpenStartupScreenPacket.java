package com.fireball1725.graves.common.network.packets;

import com.fireball1725.graves.common.block.Blocks;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
                handleClient();
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void handleClient() {
            Minecraft.getMinecraft().guiAchievement.displayAchievement(new Achievement("Graves Mod", "gravesmod", 0, 0, Blocks.BLOCK_GRAVE.block, AchievementList.ACQUIRE_IRON));
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Random Graves spawn for loot!"));
        }
    }
}
