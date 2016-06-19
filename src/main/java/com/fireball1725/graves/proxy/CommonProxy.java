package com.fireball1725.graves.proxy;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.configuration.ConfigurationFile;
import com.fireball1725.graves.common.entity.Entities;
import com.fireball1725.graves.common.event.EventBlockBreak;
import com.fireball1725.graves.common.event.EventDeathHandler;
import com.fireball1725.graves.common.helpers.BreakableWhiteListHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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
				'x', new ItemStack(net.minecraft.init.Blocks.STONE, 1, 4),
				'z', new ItemStack(net.minecraft.init.Blocks.STONE, 1, 6));
	}

    @Override
    public void registerWhiteList() {
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.DIRT.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.DIRT.getStateFromMeta(2));
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.GRASS.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.STONE.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.STONE.getStateFromMeta(1));
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.STONE.getStateFromMeta(3));
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.STONE.getStateFromMeta(5));
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.COBBLESTONE.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.SAND.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.SAND.getStateFromMeta(1));
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.GRAVEL.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.SOUL_SAND.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.NETHERRACK.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.CLAY.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.WATER.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.LAVA.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.FLOWING_WATER.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.FLOWING_LAVA.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.SNOW_LAYER.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.ICE.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.PACKED_ICE.getDefaultState());
		BreakableWhiteListHelper.addBlock(net.minecraft.init.Blocks.MYCELIUM.getDefaultState());
	}
}
