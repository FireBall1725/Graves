package com.fireball1725.graves;

import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.proxy.IProxy;
import com.fireball1725.graves.reference.ModInfo;
import com.google.common.base.Stopwatch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.concurrent.TimeUnit;

@Mod(modid = ModInfo.MOD_ID, name = ModInfo.MOD_NAME, certificateFingerprint = ModInfo.FINGERPRINT, dependencies = ModInfo.DEPENDENCIES, version = ModInfo.VERSION_BUILD)
public class Graves {
    @Mod.Instance(ModInfo.MOD_ID)
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
        if (event.getSide() == Side.CLIENT) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();

            renderItem.getItemModelMesher().register(Item.getItemFromBlock(Blocks.BLOCK_GRAVESTONE.block), 0, new ModelResourceLocation(ModInfo.MOD_ID + ":" + "gravestone", "inventory"));
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
