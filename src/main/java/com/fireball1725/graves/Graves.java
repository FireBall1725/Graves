package com.fireball1725.graves;

import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.proxy.IProxy;
import com.fireball1725.graves.reference.ModInfo;
import com.google.common.base.Stopwatch;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.commons.lang3.time.StopWatch;

import java.util.concurrent.TimeUnit;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, certificateFingerprint = ModInfo.FINGERPRINT, dependencies = ModInfo.DEPENDENCIES, version = ModInfo.VERSION_BUILD)
public class Graves {
    @Mod.Instance
    public static Graves instance;

    @SidedProxy(clientSide = ModInfo.CLIENT_PROXY_CLASS, serverSide = ModInfo.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        final Stopwatch stopWatch = Stopwatch.createStarted();
        LogHelper.info("Pre Initialization ( started )");

        // Setup Configuration
        proxy.registerConfiguration(event.getSuggestedConfigurationFile());

        // Register Blocks
        proxy.registerBlocks();

        // Register Items
        proxy.registerItems();

        // Register Events
        proxy.registerEvents();

        LogHelper.info("Pre Initalization ( ended after " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + "ms )");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
