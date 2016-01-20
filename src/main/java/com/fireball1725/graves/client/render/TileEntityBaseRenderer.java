package com.fireball1725.graves.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public abstract class TileEntityBaseRenderer extends TileEntitySpecialRenderer {
    static final int orientRotation[] = {0, 0, 0, 2, 3, 1, 0};
    static final int sideRotation[] = {1, 3, 0, 0, 0, 0};
    protected float scale = 1f / 256f;
    protected Minecraft mc = Minecraft.getMinecraft();
    protected TextureManager texManager = mc.renderEngine;
    protected FontRenderer renderFont = mc.fontRendererObj;
    protected int boundTexIndex;

    protected void setLight(TileEntity tileEntity, EnumFacing side) {
        float ambientLight = tileEntity.getWorld().getLightBrightness(tileEntity.getPos());
        float var6 = ambientLight % 65536;
        float var7 = ambientLight / 65536;
        float var8 = 1.0F;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 * var8, var7 * var8);
    }

    protected void renderTextOnBlock(String renderString, EnumFacing side, EnumFacing orientation, double x, double y, double z, float size, double posx, double posy, int red, int green, int blue, int alpha, byte align) {
        int color = (alpha << 24) | (red << 16) | (blue << 8) | green;
        this.renderTextOnBlock(renderString, side, orientation, x, y, z, size, posx, posy, color, align);
    }

    protected void renderTextOnBlock(String renderString, EnumFacing side, EnumFacing orientation, double x, double y, double z, float size, double posx, double posy, int color) {
        this.renderTextOnBlock(renderString, side, orientation, x, y, z, size, posx, posy, 0F, color);
    }

    protected void renderTextOnBlock(String renderString, EnumFacing side, EnumFacing orientation, double x, double y, double z, float size, double posx, double posy, float angle, int color) {
        if (renderString == null || renderString.equals("")) {
            return;
        }

        int stringWidth = renderFont.getStringWidth(renderString);

        GL11.glPushMatrix();

        this.alignRendering(side, orientation, x, y, z);
        this.moveRendering(size, posx, posy, -0.001);

        GL11.glRotatef(angle, 0.0f, 0.0f, 1.0f);

        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_LIGHTING);

        this.renderFont.drawSplitString(renderString, -44, -7, 90, color);

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    protected void alignRendering(EnumFacing side, EnumFacing orientation, double x, double y, double z) {
        GL11.glTranslated(x + 0.5F, y + 0.5F, z + 0.5F);     // We align the rendering on the center of the block
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(this.getRotationYForSide(side, orientation), 0.0F, 1.0F, 0.0F); // We rotate it so it face the right face
        GL11.glRotatef(this.getRotationXForSide(side), 1.0F, 0.0F, 0.0F);
        GL11.glTranslated(-0.5F, -0.5F, -0.5f);
    }

    protected void moveRendering(float size, double posX, double posY, double posz) {
        GL11.glTranslated(0, 0, posz);
        GL11.glScalef(scale, scale, -0.0001f);              // We flatten the rendering and scale it to the right size
        GL11.glTranslated(posX, posY, 0);          // Finally, we translate the icon itself to the correct position
        GL11.glScalef(size, size, 1.0f);
    }

    protected float getRotationYForSide(EnumFacing side, EnumFacing orientation) {
        int sideRotation[] = {orientRotation[orientation.ordinal()], orientRotation[orientation.ordinal()], 0, 2, 3, 1};
        return sideRotation[side.ordinal()] * 90F;
    }

    protected float getRotationXForSide(EnumFacing side) {
        return sideRotation[side.ordinal()] * 90F;
    }

    protected void saveBoundTexture() {
        boundTexIndex = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
    }

    protected void loadBoundTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, boundTexIndex);
    }

    protected int[][] saveGLState(int[] bitsToSave) {
        if (bitsToSave == null) {
            return null;
        }

        int[][] savedGLState = new int[bitsToSave.length][2];
        int count = 0;

        for (int glBit : bitsToSave) {
            savedGLState[count][0] = glBit;
            savedGLState[count++][1] = GL11.glIsEnabled(glBit) ? 1 : 0;
        }
        return savedGLState;
    }

    protected int[][] modifyGLState(int[] bitsToDisable, int[] bitsToEnable) {
        return modifyGLState(bitsToDisable, bitsToEnable, null);
    }

    protected int[][] modifyGLState(int[] bitsToDisable, int[] bitsToEnable, int[] bitsToSave) {
        if (bitsToDisable == null && bitsToEnable == null && bitsToSave == null) {
            return null;
        }

        int[][] savedGLState = new int[(bitsToDisable != null ? bitsToDisable.length : 0) + (bitsToEnable != null ? bitsToEnable.length : 0) + (bitsToSave != null ? bitsToSave.length : 0)][2];
        int count = 0;

        if (bitsToDisable != null) {
            for (int glBit : bitsToDisable) {
                savedGLState[count][0] = glBit;
                savedGLState[count++][1] = GL11.glIsEnabled(glBit) ? 1 : 0;
                GL11.glDisable(glBit);
            }
        }
        if (bitsToEnable != null) {
            for (int glBit : bitsToEnable) {
                savedGLState[count][0] = glBit;
                savedGLState[count++][1] = GL11.glIsEnabled(glBit) ? 1 : 0;
                GL11.glEnable(glBit);
            }
        }
        if (bitsToSave != null) {
            for (int glBit : bitsToSave) {
                savedGLState[count][0] = glBit;
                savedGLState[count++][1] = GL11.glIsEnabled(glBit) ? 1 : 0;
            }
        }

        return savedGLState;
    }

    protected void restoreGlState(int[][] savedGLState) {
        if (savedGLState == null) {
            return;
        }

        for (int[] glBit : savedGLState) {
            if (glBit[1] == 1)
                GL11.glEnable(glBit[0]);
            else
                GL11.glDisable(glBit[0]);
        }
    }
}
