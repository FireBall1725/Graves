package com.fireball1725.graves.common.helpers;

import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Map;

public class SoundHelper
{
	public static void registerSound(String soundName, Map<String, SoundEvent> map)
	{
		final ResourceLocation soundID = new ResourceLocation(ModInfo.MOD_ID, soundName);
		map.put(soundName, GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID)));
	}
}
