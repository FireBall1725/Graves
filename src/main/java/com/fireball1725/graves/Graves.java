package com.fireball1725.graves;

import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.proxy.IProxy;
import com.google.common.base.Stopwatch;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, certificateFingerprint = ModInfo.FINGERPRINT, dependencies = ModInfo.DEPENDENCIES, version = ModInfo.VERSION_BUILD, guiFactory = ModInfo.GUI_FACTORY)
public class Graves {
    @Mod.Instance(ModInfo.MOD_ID)
    public static Graves instance;

	@SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.COMMON_PROXY_CLASS)
	public static IProxy proxy;

	public static Logger logger;

    public static Configuration configuration;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		final Stopwatch stopWatch = Stopwatch.createStarted();
		logger.info("Pre-Initialization ( started )");

		proxy.preInit(event);

		logger.info("Pre-Initialization ( ended after " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms )");
	}

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
		final Stopwatch stopWatch = Stopwatch.createStarted();
		logger.info("Initialization ( started )");
		proxy.init(event);
		logger.info("Initialization ( ended after " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms )");
	}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
		final Stopwatch stopWatch = Stopwatch.createStarted();
		logger.info("Post-Initialization ( started )");
		proxy.postInit(event);
		logger.info("Post-Initialization ( ended after " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms )");
	}
}
