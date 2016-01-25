package com.fireball1725.graves.client.render;

import com.fireball1725.graves.block.BlockHeadStone;
import com.fireball1725.graves.helpers.OpenGLHelper;
import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TileEntityHeadStoneRenderer extends TileEntityBaseRenderer {
    public static TileEntityHeadStoneRenderer _instance = null;

    public static TileEntityHeadStoneRenderer instance() {
        if (_instance == null) {
            _instance = new TileEntityHeadStoneRenderer();
        }
        return _instance;
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(te instanceof TileEntityHeadStone)
		{
			if(te.getWorld().getBlockState(te.getPos()).getBlock() instanceof BlockHeadStone)
			{
				this.saveBoundTexture();
				int[][] savedGLState = OpenGLHelper.modifyGLState(new int[] {GL11.GL_BLEND, GL11.GL_LIGHTING}, null);
				TileEntityHeadStone headStone = (TileEntityHeadStone) te;
				renderTextOnHeadstone(headStone.getCustomName().split("\\\\n"), headStone.getWorld().getBlockState(te.getPos()).getValue(BlockHeadStone.FACING), x, y, z, 0, 0, .0365f, 1f / 200f, Color.RED.hashCode(), true);

				OpenGLHelper.restoreGLState(savedGLState);
			}
		}
    }

    public void renderTextOnHeadstone(String[] text, EnumFacing orientation, double x, double y, double z, float xOffset, float yOffset, float zOffset, float scale, int color, boolean shadow) {
        int rotationIndex = 0;
        switch (orientation) {
            case NORTH:
                xOffset -= 1;
                zOffset += 1;
                rotationIndex = 2;
                break;
            case SOUTH:
                rotationIndex = 0;
                break;
            case WEST:
                zOffset += 1;
                rotationIndex = 1;
                break;
            case EAST:
                xOffset -= 1;
                rotationIndex = 3;
                break;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(180f, 1f, 0f, 0f);
        GlStateManager.rotate(90f * rotationIndex, 0f, 1f, 0f);

        GlStateManager.pushMatrix();
        GlStateManager.translate(.5 + xOffset, -.815f + yOffset, zOffset - 1);

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);

        GlStateManager.pushMatrix();
        int i = 0;
        for (String s : text) {
            int stringWidth = mc.fontRendererObj.getStringWidth(s);
            if (stringWidth == 0) {
                stringWidth = 1;
            }
            int xCenter = stringWidth / 2;
            int yCenter = mc.fontRendererObj.FONT_HEIGHT / 2;
            if (shadow) {
                mc.fontRendererObj.drawString(s, -xCenter - 1, -yCenter + 1 + (renderFont.FONT_HEIGHT * i + 2), (color & 16579836) >> 2 | color & -16777216);
                GlStateManager.translate(0f, 0f, -.001f);
            }
            mc.fontRendererObj.drawString(s, -xCenter, -yCenter + (renderFont.FONT_HEIGHT * i + 2), color);

            i++;
        }
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }
}
