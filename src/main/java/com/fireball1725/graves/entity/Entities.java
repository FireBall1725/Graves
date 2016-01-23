package com.fireball1725.graves.entity;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.client.render.entity.RenderPlayerZombie;
import com.fireball1725.graves.helpers.LogHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public enum Entities
{
	PLAYERZOMBIE(EntityPlayerZombie.class, "playerzombie", 64, 16, true);

	private final Class<? extends Entity> entityClass;
	private String entityStringID;
	private int trackingRange;
	private int updateFrequency;
	private boolean sendsVelocityUpdates;

	Entities(Class<? extends Entity> entityClass, String entityStringID, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		this.entityClass = entityClass;
		this.entityStringID = entityStringID;
		this.trackingRange = trackingRange;
		this.updateFrequency = updateFrequency;
		this.sendsVelocityUpdates = sendsVelocityUpdates;
		this.renderClass = renderClass;
	}

	public static void registerEntities()
	{
		int count = 0;
		for(Entities entity : values())
		{
			EntityRegistry.registerModEntity(entity.entityClass.asSubclass(Entity.class), entity.entityStringID, count++, Graves.instance, entity.trackingRange, entity.updateFrequency, entity.sendsVelocityUpdates);
		}
	}
}
