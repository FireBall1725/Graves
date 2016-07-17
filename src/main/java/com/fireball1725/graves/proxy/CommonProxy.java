package com.fireball1725.graves.proxy;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.configuration.ConfigurationFile;
import com.fireball1725.graves.common.entity.Entities;
import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.event.Events;
import com.fireball1725.graves.common.helpers.GuiHelper;
import com.fireball1725.graves.common.world.WorldGeneration;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
        //WorldGen
        GameRegistry.registerWorldGenerator(new WorldGeneration(), 1);
        //Entities
        Entities.registerEntities();
		//Events
        NetworkRegistry.INSTANCE.registerGuiHandler(Graves.instance, new GuiHelper());
        MinecraftForge.EVENT_BUS.register(new Events());
		//Recipes
		GameRegistry.addRecipe(new ItemStack(Blocks.BLOCK_GRAVE.block), " o ", " s ", "sss",
				's', new ItemStack(net.minecraft.init.Blocks.STONE), 'o', new ItemStack(net.minecraft.init.Blocks.OBSIDIAN));
	}

	@Override
	public void init(FMLInitializationEvent event)
	{
		//Packet Handler
        Graves.packetHandler.init();
    }

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
