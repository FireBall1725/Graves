package com.fireball1725.graves.common.network;

import com.fireball1725.graves.common.network.messages.MessageSetHeadstoneName;
import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(ModInfo.MOD_ID.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(MessageSetHeadstoneName.HANDLER.class, MessageSetHeadstoneName.class, 0, Side.SERVER);
    }
}