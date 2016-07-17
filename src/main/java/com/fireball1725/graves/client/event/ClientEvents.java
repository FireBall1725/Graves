package com.fireball1725.graves.client.event;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.entity.capabilities.IGraveCapability;
import com.fireball1725.graves.common.network.packets.OpenStartupScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientEvents {
    private static int tick = 0;

    public static int getTick() {
        return tick;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !Minecraft.getMinecraft().isGamePaused() && Minecraft.getMinecraft().theWorld != null) {
            tick++;
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null && player.hasCapability(GraveCapability.GRAVE_CAPABILITY, null)) {
            IGraveCapability grave = player.getCapability(GraveCapability.GRAVE_CAPABILITY, null);
            if (!grave.hasSeenStartUp() && Minecraft.getMinecraft().currentScreen == null) {
                Graves.packetHandler.sendToServer(new OpenStartupScreenPacket());
            }
        }
    }
}
