package com.fireball1725.graves.proxy;

import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.entity.Entities;
import com.fireball1725.graves.event.EventBlockBreak;
import com.fireball1725.graves.event.EventDeathHandler;
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
	public void registerEntities()
	{
		Entities.registerEntities();
	}

	@Override
	public void registerEvents() {
        MinecraftForge.EVENT_BUS.register(new EventDeathHandler());
        MinecraftForge.EVENT_BUS.register(new EventBlockBreak());
    }

    @Override
    public void registerConfiguration(File configFile) {

    }

    @Override
    public void registerRenderers() {
        /* Client Side Only */
    }

    @Override
    public void registerRecipes() {
        // Headstone
		GameRegistry.addRecipe(new ItemStack(Blocks.BLOCK_GRAVE_HEADSTONE.block), " x ",
				"xzx",
				"xxx",
				'x', new ItemStack(net.minecraft.init.Blocks.stone, 1, 4), 'z', new ItemStack(net.minecraft.init.Blocks.stone, 1, 6));
	}
}
