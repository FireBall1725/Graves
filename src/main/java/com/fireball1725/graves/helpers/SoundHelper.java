package com.fireball1725.graves.helpers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

public class SoundHelper
{
	public static SoundEvent getRegisteredSoundEvent(String id)
	{
		SoundEvent soundevent = SoundEvent.soundEventRegistry.getObject(new ResourceLocation(id));

		if(soundevent == null)
		{
			throw new IllegalStateException("Invalid Sound requested: " + id);
		}
		else
		{
			return soundevent;
		}
	}
}
