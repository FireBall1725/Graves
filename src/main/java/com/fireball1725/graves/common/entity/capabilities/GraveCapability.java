package com.fireball1725.graves.common.entity.capabilities;

import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GraveCapability
{
	@CapabilityInject(IGraveCapability.class)
	public static final Capability<IGraveCapability> GRAVE_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IGraveCapability.class, new GraveCapStorage(), GraveCapDefaultImpl.class);
		MinecraftForge.EVENT_BUS.register(new GraveCapability());
	}

	@SubscribeEvent
	public void cloneEvent(PlayerEvent.Clone event)
	{
		NBTTagCompound grave = event.getOriginal().getCapability(GRAVE_CAPABILITY, null).serializeNBT();
		event.getEntityPlayer().getCapability(GRAVE_CAPABILITY, null).deserializeNBT(grave);
	}

	@SubscribeEvent
	public void onPlayerLoad(AttachCapabilitiesEvent.Entity event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{ event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "gravecap"), new GraveCapProvider()); }
	}
}
