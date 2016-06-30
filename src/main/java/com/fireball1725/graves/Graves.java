package com.fireball1725.graves;

import com.fireball1725.graves.chiselsandbits.GraveCapability;
import com.fireball1725.graves.common.helpers.LogHelper;
import com.fireball1725.graves.common.network.PacketHandler;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.util.GuiHandler;
import com.fireball1725.graves.proxy.IProxy;
import com.google.common.base.Stopwatch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.concurrent.TimeUnit;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, certificateFingerprint = ModInfo.FINGERPRINT, dependencies = ModInfo.DEPENDENCIES, version = ModInfo.VERSION_BUILD, guiFactory = ModInfo.GUI_FACTORY)
public class Graves {
    @Mod.Instance(ModInfo.MOD_ID)
    public static Graves instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    public static Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		boolean load = ModInfo.chiselsAndBits = Loader.isModLoaded("chiselsandbits");
		if(load)
		{
			GraveCapability.register();
		}
		final Stopwatch stopWatch = Stopwatch.createStarted();
        LogHelper.info("Pre Initialization ( started )");

        // Setup Configuration
        proxy.registerConfiguration(event.getSuggestedConfigurationFile());

		// Register Blocks
		proxy.registerBlocks();

		// Register Items
		proxy.registerItems();

		// Register Entities
		proxy.registerEntities();

		// Register Events
		proxy.registerEvents();

		// Register Crafting Recipes
		proxy.registerRecipes();

		// Register Whitelist
		proxy.registerWhiteList();

		LogHelper.info("Pre Initalization ( ended after " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms )");
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PacketHandler.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        proxy.registerRenderers();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
