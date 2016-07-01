package com.fireball1725.graves.client.render;

import com.fireball1725.graves.common.block.BlockHeadStone;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityHeadStone;
import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;

import java.awt.*;

public class TileEntityHeadStoneRenderer extends TileEntityBaseRenderer {
	public static TileEntityHeadStoneRenderer _instance = null;
	public static IBakedModel bakedModel;// = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(Blocks.BLOCK_GRAVE_HEADSTONE.block.getDefaultState());

	public TileEntityHeadStoneRenderer()
	{
		bakeModel(mc.getTextureMapBlocks());
	}

	public static void bakeModel(final TextureMap textureMap)
	{
		try
		{
			OBJModel model = (OBJModel) OBJLoader.INSTANCE.loadModel(new ResourceLocation(ModInfo.MOD_ID, "models/block/headstone.obj"));
			bakedModel = model.bake(model.getDefaultState(), DefaultVertexFormats.BLOCK, new Function<ResourceLocation, TextureAtlasSprite>()
			{
				@Override
				public TextureAtlasSprite apply(ResourceLocation input)
				{
					return textureMap.getAtlasSprite(input.toString());
				}
			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static TileEntityHeadStoneRenderer instance()
	{

		if(_instance == null)
		{
			_instance = new TileEntityHeadStoneRenderer();
		}
		return _instance;
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + .5, y + .5, z + .5);
		if(te instanceof TileEntityHeadStone)
		{
			TileEntityHeadStone headStone = (TileEntityHeadStone) te;
			IBlockState state = headStone.getWorld().getBlockState(te.getPos());
			EnumFacing facing = state.getValue(BlockHeadStone.FACING);
			GlStateManager.pushMatrix();
			int rot = facing.getHorizontalIndex();
			if(facing == EnumFacing.WEST || facing == EnumFacing.EAST)
			{
				GlStateManager.rotate(180, 0, 1, 0);
			}
			GlStateManager.rotate(90 * rot, 0, 1, 0);
			if(headStone.getDisplayStack() != null)
			{
				ItemStack stack = headStone.getDisplayStack();
				IBakedModel model = mc.getRenderItem().getItemModelWithOverrides(stack, null, null);
				GlStateManager.pushMatrix();
				GlStateManager.enableLighting();
				GlStateManager.enableRescaleNormal();
				GlStateManager.enableAlpha();
				GlStateManager.alphaFunc(516, 0.1F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				mc.getRenderItem().renderItem(stack, model);
				GlStateManager.disableAlpha();
				GlStateManager.disableRescaleNormal();
				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
			else
			{
				{
					GlStateManager.pushMatrix();
					GlStateManager.enableLighting();
					GlStateManager.enableRescaleNormal();
					GlStateManager.enableAlpha();
					GlStateManager.alphaFunc(516, 0.1F);
					GlStateManager.enableBlend();
					GlStateManager.translate(.5, -.5, .5);
					GlStateManager.rotate(180, 0, 1, 0);
					bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					mc.getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(bakedModel, 1, 1, 1, 1);
					GlStateManager.disableAlpha();
					GlStateManager.disableRescaleNormal();
					GlStateManager.disableLighting();
					GlStateManager.popMatrix();
				}
				GlStateManager.pushMatrix();
				GlStateManager.rotate(180, 0, 1, 0);
				GlStateManager.rotate(180, 0, 0, 1);
				GlStateManager.translate(0, -5 * 0.0625, -7.5 * 0.0625);
				GlStateManager.scale(0.00625, 0.00625, 1);
				renderTextOnHeadstone(headStone.getCustomName().split("\\\\n"), Color.RED.hashCode(), true);
				GlStateManager.popMatrix();
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	public void renderTextOnHeadstone(String[] text, int color, boolean shadow)
	{

		int i = 0;
		for(String s : text)
		{
			int stringWidth = mc.fontRendererObj.getStringWidth(s);
			if(stringWidth == 0)
			{
				stringWidth = 1;
			}
			int xCenter = stringWidth / 2;
			int yCenter = mc.fontRendererObj.FONT_HEIGHT / 2;
			if(i == 7)
			{ GlStateManager.translate(0, 8, -.01); }
			if(shadow)
			{
				mc.fontRendererObj.drawString(s, -xCenter - 1, -yCenter + 1 + (renderFont.FONT_HEIGHT * i + 2), (color & 16579836) >> 2 | color & -16777216);
				GlStateManager.translate(0f, 0f, -.001f);
			}
			mc.fontRendererObj.drawString(s, -xCenter, -yCenter + (renderFont.FONT_HEIGHT * i + 2), color);

			i++;
		}
	}
}
