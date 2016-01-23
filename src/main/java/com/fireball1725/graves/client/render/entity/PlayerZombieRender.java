package com.fireball1725.graves.client.render.entity;

import com.fireball1725.graves.entity.EntityPlayerZombie;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class PlayerZombieRender extends RenderLiving<EntityPlayerZombie>
{
	protected static final ModelPlayer modelPlayer = new ModelPlayer(0F, false);
	protected static final ModelPlayer modelSmallPlayer = new ModelPlayer(0F, true);

	public PlayerZombieRender(RenderManager renderManager)
	{
		super(renderManager, modelPlayer, .5f);
	}

	@Override
	public void doRender(EntityPlayerZombie entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		mainModel = modelSmallPlayer;
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityPlayerZombie entity)
	{
		GameProfile profile = entity.getPlayerProfile();
		ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();

		if(profile != null)
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);

			if(map.containsKey(MinecraftProfileTexture.Type.SKIN))
			{
				resourcelocation = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
			}
			else
			{
				UUID uuid = EntityPlayer.getUUID(profile);
				resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
			}
		}
		return resourcelocation;
	}
}
