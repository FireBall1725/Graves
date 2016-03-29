package com.fireball1725.graves.client.render.entity;

import com.fireball1725.graves.configuration.ConfigZombie;
import com.fireball1725.graves.entity.EntityPlayerZombie;
import com.fireball1725.graves.util.TextureUtils;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.item.ItemBow;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderPlayerZombie extends RenderBiped<EntityPlayerZombie> {
    private static final ModelPlayer STEVE = new ModelPlayer(0, false);
    private static final ModelPlayer ALEX = new ModelPlayer(0, true);

    public RenderPlayerZombie(RenderManager renderManager) {
        super(renderManager, STEVE, 0.5F, 1.0F);

        addLayer(new LayerBipedArmor(this));
        addLayer(new LayerHeldItem(this));
        addLayer(new LayerArrow(this));
        addLayer(new LayerHumanCape(this));
        addLayer(new LayerCustomHead(getMainModel().bipedHead));
    }

    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer) super.getMainModel();
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    @Override
    public void doRender(EntityPlayerZombie entity, double x, double y, double z, float f0, float partialTickTime) {
        setModel(entity);
        if (ConfigZombie.configZombieShowBossBar)
		{
			// TODO: Do what was done here...
		}

		if(entity.getHeldItem() != null && entity.getHeldItem().getItem() instanceof ItemBow)
		{ modelBipedMain.aimedBow = true; }

        super.doRender(entity, x, y, z, f0, partialTickTime);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPlayerZombie entity) {
		return TextureUtils.getPlayerSkin((entity).getProfile());
	}

    @Override
    protected void preRenderCallback(EntityPlayerZombie entity, float float0) {
        GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
        GlStateManager.color(1, 1, 1, 0.75f);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void setModel(EntityPlayerZombie playerZombie) {
        String type = TextureUtils.getPlayerSkinType(playerZombie.getProfile());
        boolean isAlex = "slim".equals(type);
        mainModel = modelBipedMain = isAlex ? ALEX : STEVE;
    }
}
