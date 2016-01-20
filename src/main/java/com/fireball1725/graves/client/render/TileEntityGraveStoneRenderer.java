package com.fireball1725.graves.client.render;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityGraveStoneRenderer extends TileEntityBaseRenderer {
    public static TileEntityGraveStoneRenderer _instance = null;

    public static TileEntityGraveStoneRenderer instance() {
        if (_instance == null)
            _instance = new TileEntityGraveStoneRenderer();
        return _instance;
    }


    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (te instanceof TileEntityGraveStone) {
            this.saveBoundTexture();

            int[][] savedGLState = modifyGLState(new int[]{GL11.GL_BLEND, GL11.GL_LIGHTING}, null);
            TileEntityGraveStone graveStone = (TileEntityGraveStone) te;

			//            this.renderTextOnBlock(graveStone.getPlayerName(), graveStone.getBlockState().getValue(BlockGraveStone.FACING), graveStone.getBlockState().getValue(BlockGraveStone.FACING).getOpposite(), x - 1f, y - .2f,  z - .2f, 2.5F, 128.0F, 35.0F, 0.0F, 0);

			renderTextOnHeadstotne(graveStone.getPlayerName(), graveStone.getBlockState().getValue(BlockGraveStone.FACING), x, y, z, .5f, -1.15f, .03f);
			renderTextOnHeadstotne("R.I.P", graveStone.getBlockState().getValue(BlockGraveStone.FACING), x, y, z, .5f, -1f, .03f);


            this.restoreGlState(savedGLState);
            this.loadBoundTexture();
        }
    }

	public void renderTextOnHeadstotne(String text, EnumFacing orientation, double x, double y, double z, float xOffset, float yOffset, float zOffset)
	{
		int stringWidth = mc.fontRendererObj.getStringWidth(text);

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

		//		LogHelper.info(">>> " + rotationIndex);

		bindTexture(new ResourceLocation("textures/font/ascii.png"));
		float scale = .005f;

		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180f, 1f, 0f, 0f);
		GlStateManager.rotate(90f * rotationIndex, 0f, 1f, 0f);

		GlStateManager.pushMatrix();
		GlStateManager.translate(xOffset - (stringWidth / 200f) / 2f, yOffset, zOffset);

		GlStateManager.pushMatrix();
		GlStateManager.scale(scale, scale, 0);

		GlStateManager.pushMatrix();
		//		GlStateManager.disableDepth();
		GlStateManager.disableLighting();

		mc.fontRendererObj.drawString(text, 0, 0, 0);

		GlStateManager.enableLighting();
		//		GlStateManager.enableDepth();
		GlStateManager.popMatrix();

		GlStateManager.scale(scale, scale, 0);
		GlStateManager.popMatrix();

		//		GlStateManager.rotate(180f, -1f, 0f, 0f);
		//		GlStateManager.rotate(90f * rotationIndex, 0f, -1f, 0f);
		GlStateManager.popMatrix();
		GlStateManager.popMatrix();

		//		GlStateManager.translate(xOffset, yOffset, zOffset);
		GlStateManager.translate(-x, -y, -z);
		GlStateManager.popMatrix();
	}
}
