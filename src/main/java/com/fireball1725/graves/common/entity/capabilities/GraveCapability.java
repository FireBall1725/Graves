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
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

public class GraveCapability
{
	@CapabilityInject(IGraveCapability.class)
	public static final Capability<IGraveCapability> GRAVE_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IGraveCapability.class, new Storage(), DefaultImpl.class);
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
		class Provider implements ICapabilitySerializable<NBTBase>
		{
			private IGraveCapability inst = GRAVE_CAPABILITY.getDefaultInstance();

			@Override
			public <T> T getCapability(Capability<T> capability,
					@Nullable
							EnumFacing enumFacing)
			{
				return capability == GRAVE_CAPABILITY ? GRAVE_CAPABILITY.<T>cast(inst) : null;
			}

			@Override
			public boolean hasCapability(Capability<?> capability,
					@Nullable
							EnumFacing enumFacing)
			{
				return capability == GRAVE_CAPABILITY;
			}

			@Override
			public void deserializeNBT(NBTBase tagCompound)
			{
				GRAVE_CAPABILITY.getStorage().readNBT(GRAVE_CAPABILITY, inst, null, tagCompound);
			}

			@Override
			public NBTBase serializeNBT()
			{
				return GRAVE_CAPABILITY.getStorage().writeNBT(GRAVE_CAPABILITY, inst, null);
			}
		}

		if(event.getEntity() instanceof EntityPlayer)
		{ event.addCapability(new ResourceLocation(ModInfo.MOD_ID, "gravecap"), new Provider()); }
	}

	public interface IGraveCapability
	{
		ItemStack getGraveItemStack();

		void setGraveItemStack(ItemStack displayStack);

		NBTTagCompound serializeNBT();

		void deserializeNBT(NBTTagCompound tagCompound);
	}



	public static class Storage implements Capability.IStorage<IGraveCapability>
	{
		@Override
		public void readNBT(Capability<IGraveCapability> capability, IGraveCapability iGraveCapability, EnumFacing enumFacing, NBTBase nbtBase)
		{
			iGraveCapability.deserializeNBT((NBTTagCompound) nbtBase);
		}

		@Override
		public NBTBase writeNBT(Capability<IGraveCapability> capability, IGraveCapability iGraveCapability, EnumFacing enumFacing)
		{
			return iGraveCapability.serializeNBT();
		}
	}



	public static class DefaultImpl implements IGraveCapability
	{
		ItemStack displayStack;

		@Override
		public ItemStack getGraveItemStack()
		{
			return displayStack;
		}

		@Override
		public void setGraveItemStack(ItemStack displayStack)
		{
			this.displayStack = displayStack;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			if(displayStack != null)
			{ return displayStack.writeToNBT(new NBTTagCompound()); }
			else
			{ return new NBTTagCompound(); }
		}

		@Override
		public void deserializeNBT(NBTTagCompound tagCompound)
		{
			if(!tagCompound.hasNoTags())
			{
				displayStack = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
	}
}
