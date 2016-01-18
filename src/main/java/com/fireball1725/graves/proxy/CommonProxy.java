package com.fireball1725.graves.proxy;

import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.event.EventDeathHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;

public abstract class CommonProxy implements IProxy {
    @Override
    public void registerBlocks() {
        Blocks.registerAll();
    }

    @Override
    public void registerItems() {

    }

    @Override
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new EventDeathHandler());
    }

    @Override
    public void registerConfiguration(File configFile) {

    }
}
