package com.fireball1725.graves.common.configuration;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.Arrays;

@SuppressWarnings("all")
public class GravesConfig extends GuiConfig {
    public GravesConfig(GuiScreen parentScreen) {
        super(parentScreen,
                Arrays.asList(new IConfigElement[]{
                        new ConfigElement(Graves.configuration.getCategory("general")),
                        new ConfigElement(Graves.configuration.getCategory("worldgen")),
                        new ConfigElement(Graves.configuration.getCategory("zombie")),
                }),
                ModInfo.MOD_ID, false, false, "Graves Configuration");
    }
}