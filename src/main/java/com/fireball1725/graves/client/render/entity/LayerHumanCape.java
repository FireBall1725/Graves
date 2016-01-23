package com.fireball1725.graves.client.render.entity;

import com.fireball1725.graves.entity.EntityPlayerZombie;
import com.fireball1725.graves.util.TextureUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerHumanCape implements LayerRenderer<EntityPlayerZombie> {

    private final RenderPlayerZombie renderer;

    public LayerHumanCape(RenderPlayerZombie renderer) {
        this.renderer = renderer;
    }

    @Override
    public void doRenderLayer(EntityPlayerZombie human, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        ResourceLocation cape = TextureUtils.getPlayerCape(human.getProfile());

        if (!human.isInvisible() && cape != null) {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            renderer.bindTexture(cape);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.0F, 0.125F);
            double d0 = human.getInterpolatedCapeX(partialTicks);
            double d1 = human.getInterpolatedCapeY(partialTicks);
            double d2 = human.getInterpolatedCapeZ(partialTicks);
            float f = human.prevRenderYawOffset + (human.renderYawOffset - human.prevRenderYawOffset) * partialTicks;
            double d3 = MathHelper.sin(f * (float) Math.PI / 180.0F);
            double d4 = -MathHelper.cos(f * (float) Math.PI / 180.0F);
            float f1 = (float) d1 * 10.0F;
            f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
            float f2 = (float) (d0 * d3 + d2 * d4) * 100.0F;
            float f3 = (float) (d0 * d4 - d2 * d3) * 100.0F;

            if (f2 < 0.0F)
                f2 = 0.0F;

            float f4 = human.prevRenderYawOffset + (human.renderYawOffset - human.prevRenderYawOffset) * partialTicks;
            f1 = f1 + MathHelper.sin((human.prevDistanceWalkedModified + (human.distanceWalkedModified - human.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

            if (human.isSneaking())
                f1 += 25.0F;

            GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            renderer.getMainModel().renderCape(0.0625F);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}