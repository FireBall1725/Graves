package com.fireball1725.graves.chiselsandbits;

import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class GraveCapability
{
	@CapabilityInject(IGraveCapability.class)
	public static final Capability<IGraveCapability> GRAVE_CAP = null;

	public static void register()
	{
		boolean load = ModInfo.chiselsAndBits = Loader.isModLoaded("chiselsandbits");
		if(load)
		{
			CapabilityManager.INSTANCE.register(IGraveCapability.class, new Storage(), DefaultImpl.class);
			MinecraftForge.EVENT_BUS.register(new GraveCapability());
		}
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
		Block getGraveBlock();

		void setGraveBlock(Block block);

		int getGraveMeta();

		void setGraveMeta(int meta);

		NBTTagCompound getGraveTag();

		void setGraveTag(NBTTagCompound tag);

		boolean hasTag();
	}



	public static class Storage implements IStorage<IGraveCapability>
	{
		@Override
		public NBTBase writeNBT(Capability<IGraveCapability> capability, IGraveCapability instance, EnumFacing side)
		{
			NBTTagCompound tagCompound = new NBTTagCompound();
			if(instance.getGraveBlock() == null)
			{ return tagCompound; }

			tagCompound.setString("graveBlock", ForgeRegistries.BLOCKS.getKey(instance.getGraveBlock()).toString());
			tagCompound.setInteger("graveMeta", instance.getGraveMeta());
			if(instance.hasTag())
			{ tagCompound.setTag("graveTag", instance.getGraveTag()); }
			return tagCompound;
		}

		@Override
		public void readNBT(Capability<IGraveCapability> capability, IGraveCapability instance, EnumFacing side, NBTBase nbt)
		{
			if(nbt.hasNoTags())
			{ return; }

			instance.setGraveBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(((NBTTagCompound) nbt).getString("graveBlock"))));
			instance.setGraveMeta(((NBTTagCompound) nbt).getInteger("graveMeta"));
			if(((NBTTagCompound) nbt).hasKey("graveTag"))
			{ instance.setGraveTag(((NBTTagCompound) nbt).getCompoundTag("graveTag")); }
		}
	}



	public static class DefaultImpl implements IGraveCapability
	{
		Block graveBlock;
		int graveMeta;
		NBTTagCompound graveTag;

		@Override
		public NBTTagCompound getGraveTag()
		{
			return graveTag;
		}

		@Override
		public void setGraveTag(NBTTagCompound chiselAndBitsGrave)
		{
			this.graveTag = chiselAndBitsGrave;
		}

		@Override
		public boolean hasTag()
		{
			return graveTag != null;
		}

		@Override
		public Block getGraveBlock()
		{
			return graveBlock;
		}

		@Override
		public void setGraveBlock(Block block)
		{
			graveBlock = block;
		}

		@Override
		public int getGraveMeta()
		{
			return graveMeta;
		}

		@Override
		public void setGraveMeta(int meta)
		{
			this.graveMeta = meta;
		}
	}
}