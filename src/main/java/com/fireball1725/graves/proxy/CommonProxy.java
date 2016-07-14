package com.fireball1725.graves.proxy;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.configuration.ConfigurationFile;
import com.fireball1725.graves.common.entity.Entities;
import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.event.Events;
import com.fireball1725.graves.common.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IProxy
{

	@Override
	public void preInit(FMLPreInitializationEvent event)
	{
		//Config
		Graves.configuration = ConfigurationFile.init(event.getSuggestedConfigurationFile());
		MinecraftForge.EVENT_BUS.register(new ConfigurationFile());
		//Capabilities
		GraveCapability.register();
		//Blocks
		Blocks.registerAll();
		//Items
		//Items.registerAll();
		//Entities
		Entities.registerEntities();
		//Events
		MinecraftForge.EVENT_BUS.register(new Events());
		//Recipes
		GameRegistry.addRecipe(new ItemStack(Blocks.BLOCK_GRAVE.block), "x x", "xzx", "xxx",
				'x', new ItemStack(net.minecraft.init.Blocks.stone), 'v', new ItemStack(net.minecraft.init.Blocks.obsidian));
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		//Packet Handler
		PacketHandler.init();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
