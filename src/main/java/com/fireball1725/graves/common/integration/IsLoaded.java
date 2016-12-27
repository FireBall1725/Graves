package com.fireball1725.graves.common.integration;

import net.minecraftforge.fml.common.Loader;

public class IsLoaded {
    public static final boolean CHISELSANDBITS = Loader.isModLoaded("chiselsandbits");
    public static final boolean FUSIONSPATRONS = Loader.isModLoaded("fusionlordspatrons");
    public static final boolean JOURNEYMAP = Loader.isModLoaded("journeymap");

    public static void initialize() {
    }
}
