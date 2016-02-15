package com.fireball1725.graves.proxy;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.configuration.ConfigurationFile;
import com.fireball1725.graves.entity.Entities;
import com.fireball1725.graves.event.EventBlockBreak;
import com.fireball1725.graves.event.EventDeathHandler;
import com.fireball1725.graves.helpers.BreakableWhiteListHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public void registerEntities() {
        Entities.registerEntities();
    }

    @Override
    public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new EventDeathHandler());
        MinecraftForge.EVENT_BUS.register(new EventBlockBreak());
    }

    @Override
    public void registerConfiguration(File configFile) {
        Graves.configuration = ConfigurationFile.init(configFile);
        MinecraftForge.EVENT_BUS.register(new ConfigurationFile());
    }

    @Override
    public void registerRenderers() {
        /* Client Side Only */
    }

    @Override
    public void registerRecipes() {
        // Headstone
        GameRegistry.addRecipe(new ItemStack(Blocks.BLOCK_GRAVE_HEADSTONE.block),
                " x ",
                "xzx",
                "xxx",
                'x', new ItemStack(net.minecraft.init.Blocks.stone, 1, 4),
                'z', new ItemStack(net.minecraft.init.Blocks.stone, 1, 6));
    }

    @Override
    public void registerWhiteList() {
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.dirt.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.dirt.getStateFromMeta(2));
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.grass.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.stone.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.stone.getStateFromMeta(1));
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.stone.getStateFromMeta(3));
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.stone.getStateFromMeta(5));
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.cobblestone.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.sand.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.sand.getStateFromMeta(1));
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.gravel.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.soul_sand.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.netherrack.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.clay.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.water.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.lava.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.flowing_water.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.flowing_lava.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.snow_layer.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.ice.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.packed_ice.getDefaultState());
        BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.mycelium.getDefaultState());
    }
}
