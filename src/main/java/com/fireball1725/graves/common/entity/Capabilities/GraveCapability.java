package com.fireball1725.graves.common.entity.capabilities;

import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GraveCapability
{
	@CapabilityInject(IGraveCapability.class)
	public static final Capability<IGraveCapability> GRAVE_CAP = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IGraveCapability.class, new Storage(), DefaultImpl.class);
		MinecraftForge.EVENT_BUS.register(new GraveCapability());
	}

	// Handles playerData props from being wiped on death
	@SubscribeEvent
	public void cloneEvent(PlayerEvent.Clone evt)
	{
		NBTTagCompound grave = evt.getOriginal().getCapability(GRAVE_CAP, null).serializeNBT();
		evt.getEntityPlayer().getCapability(GRAVE_CAP, null).deserializeNBT(grave);
	}

	@SubscribeEvent
	public void onPlayerLoad(AttachCapabilitiesEvent.Entity event)
	{
		class Provider implements ICapabilitySerializable<NBTBase>
		{
			private IGraveCapability inst = GRAVE_CAP.getDefaultInstance();

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing enumFacing)
			{
				return capability == GRAVE_CAP;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing enumFacing)
			{
				return capability == GRAVE_CAP ? GRAVE_CAP.<T>cast(inst) : null;
			}

			@Override
			public NBTBase serializeNBT()
			{
				return GRAVE_CAP.getStorage().writeNBT(GRAVE_CAP, inst, null);
			}

			@Override
			public void deserializeNBT(NBTBase tag)
			{
				GRAVE_CAP.getStorage().readNBT(GRAVE_CAP, inst, null, tag);
			}
		}

		if(event.getEntity() instanceof EntityPlayer)
		{ event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "GraveCap"), new Provider()); }
	}

	public interface IGraveCapability
	{
		ItemStack getGraveItemStack();

		void setGraveItemStack(ItemStack itemStack);

		NBTTagCompound serializeNBT();

		void deserializeNBT(NBTTagCompound tag);
	}



	public static class Storage implements IStorage<IGraveCapability>
	{
		@Override
		public NBTBase writeNBT(Capability<IGraveCapability> capability, IGraveCapability instance, EnumFacing side)
		{
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<IGraveCapability> capability, IGraveCapability instance, EnumFacing side, NBTBase nbt)
		{
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}



	public static class DefaultImpl implements IGraveCapability
	{
		ItemStack stack;

		@Override
		public ItemStack getGraveItemStack()
		{
			return stack;
		}

		@Override
		public void setGraveItemStack(ItemStack stack)
		{
			this.stack = stack;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return stack == null ? new NBTTagCompound() : stack.serializeNBT();
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag)
		{
			if(tag.hasNoTags())
			{ return; }
			setGraveItemStack(ItemStack.loadItemStackFromNBT(tag));
		}
	}
}