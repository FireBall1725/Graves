package com.fireball1725.graves.entity;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.client.render.entity.RenderPlayerZombie;
import com.fireball1725.graves.helpers.LogHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

/**
 * Created by FusionLord on 1/22/2016.
 */
public enum Entities// implements <E extends Entity>
{
    PLAYERZOMBIE(EntityPlayerZombie.class, "playerzombie", 64, 1, true, RenderPlayerZombie.class);

    private final Class entityClass;
    private final Class renderClass;
    public String entityStringID;
    public int trackingRange;
    public int updateFrequency;
    public boolean sendsVelocityUpdates;

    <E extends Entity> Entities(Class<E> entityClass, String entityStringID, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, Class<? extends Render<E>> renderClass) {
        this.entityClass = entityClass;
        this.entityStringID = entityStringID;
        this.trackingRange = trackingRange;
        this.updateFrequency = updateFrequency;
        this.sendsVelocityUpdates = sendsVelocityUpdates;
        this.renderClass = renderClass;
    }

    public static void registerEntities() {
        int count = 0;
        for (Entities entity : values()) {
            EntityRegistry.registerModEntity(entity.entityClass, entity.entityStringID, count++, Graves.instance, entity.trackingRange, entity.updateFrequency, entity.sendsVelocityUpdates);
        }
    }

    public static void registerEntitiesRenderers() {
        for (Entities entity : values()) {
            registerEntityRenderer(entity.entityClass, entity.renderClass);
        }
    }

    private static <E extends Entity> void registerEntityRenderer(Class<E> entityClass, Class<? extends Render<E>> renderClass) {
        LogHelper.info("Registered entity renderer: " + entityClass.getSimpleName() + ", " + renderClass.getSimpleName());
        RenderingRegistry.registerEntityRenderingHandler(entityClass, new EntityRenderFactory<E>(renderClass));
    }

    private static class EntityRenderFactory<E extends Entity> implements IRenderFactory<E> {
        private Class<? extends Render<E>> renderClass;

        private EntityRenderFactory(Class<? extends Render<E>> renderClass) {
            this.renderClass = renderClass;
        }

        @Override
        public Render<E> createRenderFor(RenderManager manager) {
            Render<E> renderer = null;

            try {
                renderer = renderClass.getConstructor(RenderManager.class).newInstance(manager);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return renderer;
        }
    }
}
