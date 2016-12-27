package com.fireball1725.graves.common.network;

import com.fireball1725.graves.common.network.packets.OpenStartupScreenPacket;
import com.fireball1725.graves.common.network.packets.SyncHeadstonesToClients;
import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler extends SimpleNetworkWrapper {
    private int packetCount = 0;

    public PacketHandler() {
        super(ModInfo.MOD_ID.toLowerCase());
    }

    public void init() {
        registerMessage(SyncHeadstonesToClients.HANDLER.class, SyncHeadstonesToClients.class, Side.CLIENT);
        registerMessage(OpenStartupScreenPacket.HANDLER.class, OpenStartupScreenPacket.class, Side.CLIENT);
    }

    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> requestMessageType, Side side) {
        registerMessage(messageHandler, requestMessageType, packetCount++, side);
    }
}