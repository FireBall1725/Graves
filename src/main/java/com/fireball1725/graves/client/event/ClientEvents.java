package com.fireball1725.graves.client.event;

import net.minecraft.client.Minecraft;
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
}
