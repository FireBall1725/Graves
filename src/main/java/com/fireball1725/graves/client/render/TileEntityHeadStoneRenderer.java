package com.fireball1725.graves.client.render;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.helpers.OpenGLHelper;
import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class TileEntityHeadStoneRenderer extends TileEntityBaseRenderer
{
	public static TileEntityHeadStoneRenderer _instance = null;

	public static TileEntityHeadStoneRenderer instance()
	{
		if(_instance == null)
		{ _instance = new TileEntityHeadStoneRenderer(); }
		return _instance;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		if(te instanceof TileEntityHeadStone)
		{
			this.saveBoundTexture();

			int[][] savedGLState = OpenGLHelper.modifyGLState(new int[] {GL11.GL_BLEND, GL11.GL_LIGHTING}, null);
			TileEntityHeadStone headStone = (TileEntityHeadStone) te;

			renderTextOnHeadstone(headStone.getPlayerName(), headStone.getBlockState().getValue(BlockGraveStone.FACING), x, y, z, .125f, -.275f, -.001f, 0.02f, 0);
			renderMultiLineTextOnHeadstone(headStone.getEulogy(), headStone.getBlockState().getValue(BlockGraveStone.FACING), x, y, z, 0f, -1.225f, .03f, 0.02f, 0);

			OpenGLHelper.restoreGLState(savedGLState);
		}
	}

	public void renderMultiLineTextOnHeadstone(String text, EnumFacing orientation, double x, double y, double z, float xOffset, float yOffset, float zOffset, float scale, int color)
	{
		int stringWidth = mc.fontRendererObj.getStringWidth(text);
		if(stringWidth == 0)
		{ stringWidth = 1; }
		float center = .5f / (float) stringWidth;

		int rotationIndex = 0;
		switch(orientation)
		{
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
		GlStateManager.translate(xOffset + (stringWidth * center) / 2, yOffset, zOffset - 1);

		GlStateManager.pushMatrix();
		GlStateManager.scale(.0075f, .0075f, 0);

		GlStateManager.pushMatrix();
		GlStateManager.disableLighting();

		mc.fontRendererObj.drawSplitString(text, 0, 0, 68, Color.white.hashCode());

		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}

	public void renderTextOnHeadstone(String text, EnumFacing orientation, double x, double y, double z, float xOffset, float yOffset, float zOffset, float scale, int color)
	{
		int stringWidth = mc.fontRendererObj.getStringWidth(text);
		if(stringWidth == 0)
		{ stringWidth = 1; }
		float center = .75f / (float) stringWidth;

		int rotationIndex = 0;
		switch(orientation)
		{
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
		GlStateManager.translate(xOffset, yOffset, zOffset - 1);

		GlStateManager.pushMatrix();
		GlStateManager.scale(center, center, 0);

		GlStateManager.pushMatrix();
		//		GlStateManager.disableLighting();

		mc.fontRendererObj.drawString(text, 0, 0, Color.white.hashCode());

		//		GlStateManager.enableLighting();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();

		GlStateManager.popMatrix();
	}
}
