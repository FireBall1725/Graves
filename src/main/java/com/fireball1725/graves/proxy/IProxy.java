package com.fireball1725.graves.proxy;

import java.io.File;

public interface IProxy {
    void registerBlocks();

    void registerItems();

    void registerEntities();

    void registerEvents();

    void registerConfiguration(File configFile);

    void registerRenderers();

    void registerRecipes();

    void registerWhiteList();
}
