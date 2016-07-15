package com.fireball1725.graves.client.render;

import com.fireball1725.graves.client.event.EventTick;
import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.helpers.PatreonHelper;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TEGraveSR extends TileEntitySpecialRenderer<TileEntityGrave>
{
	private static Map<String, Map<String, String>> specialText = Maps.newHashMap();

	static
	{
		specialText = PatreonHelper.getSpecialText();
	}

	private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();
	private Minecraft mc = Minecraft.getMinecraft();
	private Random rand = new Random();

	@Override
	public void renderTileEntityAt(TileEntityGrave grave, double x, double y, double z, float partialTicks, int destroyStage)
	{
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + .5, y + .5, z + .5);
		if(getWorld().getBlockState(grave.getPos()).getBlock() instanceof BlockGrave)
		{
			GlStateManager.pushMatrix();
			if(grave.getDisplayStack() != null)
			{
				ItemStack stack = grave.getDisplayStack();
				GlStateManager.pushMatrix();
				GlStateManager.enableLighting();
				GlStateManager.enableRescaleNormal();
				GlStateManager.enableAlpha();
				GlStateManager.alphaFunc(516, 0.1F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
				IBakedModel model = mc.getRenderItem().getItemModelWithOverrides(stack, mc.theWorld, mc.thePlayer);
				mc.getRenderItem().renderItem(stack, model);
				GlStateManager.disableAlpha();
				GlStateManager.disableRescaleNormal();
				GlStateManager.disableLighting();
				GlStateManager.popMatrix();
			}
			else
			{
				if(grave.getProfile() != null)
				{
					GameProfile profile = grave.getProfile();
					IBlockState state = getWorld().getBlockState(grave.getPos().offset(EnumFacing.UP));
					if(!state.getBlock().isFullBlock(state.getActualState(getWorld(), grave.getPos().offset(EnumFacing.UP))))
					{
						GlStateManager.pushMatrix();
						GlStateManager.enableRescaleNormal();
						GlStateManager.enableAlpha();
						GlStateManager.alphaFunc(516, 0.1F);
						GlStateManager.enableBlend();

						GlStateManager.translate(0, .5, 0);
						rand.setSeed(grave.getPos().toLong());

						int dir = rand.nextBoolean() ? 1 : -1;
						GlStateManager.pushMatrix();
						if(grave.getProfile().getId().toString().equals("4f3a8d1e-33c1-44e7-bce8-e683027c7dac"))
						{
							GlStateManager.rotate(EventTick.getTick() * 10, 0, dir, 0);
						}
						else
						{
							GlStateManager.rotate(EventTick.getTick(), 0, dir, 0);
						}
						if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
						{
							GlStateManager.rotate(rand.nextFloat() * 360f, 0, 1, 0);
						}
						GlStateManager.scale(.65, .65, .65);
						GlStateManager.color(1, 1, 1, .8f);
						renderSkull(-.5f, 0, -.5f, grave.getProfile(), destroyStage, partialTicks);
						GlStateManager.popMatrix();

						for (EnumFacing facing : EnumFacing.HORIZONTALS) {
							GlStateManager.pushMatrix();
							GlStateManager.disableLighting();
							GlStateManager.rotate(180, 0, 0, 1);
							GlStateManager.rotate((facing.getHorizontalIndex() * 90), 0, 1, 0);
							GlStateManager.translate(0, .55, -.38);
							GlStateManager.scale(.00625, .00625, .00625);
							String text = "";
							if (specialText.containsKey(profile.getId().toString())) {
								Map<String, String> map = specialText.get(profile.getId().toString());
								text = "\\n" + map.get("text").replace("%n%", String.valueOf(rand.nextInt()));
								boolean patron = map.containsKey("patron") && Boolean.valueOf(map.get("patron"));
								GlStateManager.pushMatrix();
								GlStateManager.translate(0, -12, 0);
								if (true /*map.containsKey("icon") && Boolean.valueOf(map.get("icon"))*/) {
									if (patron)
										GlStateManager.translate(2, 0, 0);
									else
										GlStateManager.translate(-8, 0, 0);
									drawIcon(map.get("name").toLowerCase());
									GlStateManager.translate(-20, 0, 0);
								}
								if (patron) {
									drawPatronIcon();
								}
								GlStateManager.popMatrix();
							}

							drawText(profile.getName() + text);
							GlStateManager.enableLighting();
							GlStateManager.popMatrix();
						}
						GlStateManager.disableBlend();
						GlStateManager.disableAlpha();
						GlStateManager.disableRescaleNormal();
						GlStateManager.popMatrix();
					}
				}
			}
			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
	}

	private void drawIcon(String icon) {
		GlStateManager.color(1, 1, 1);
		bindTexture(new ResourceLocation(ModInfo.MOD_ID, "textures/patrons/" + icon + ".png"));
		Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1, 1, 16, 16, 1, 1);
	}

	private void drawPatronIcon() {
		drawIcon("patreon");
	}

	private void drawText(String text)
	{
		String[] strings = text.split("\\\\n");
		GlStateManager.pushMatrix();
		for (String s : strings)
		{
			GlStateManager.translate(0, mc.fontRendererObj.FONT_HEIGHT, 0);
			int strWidth = mc.fontRendererObj.getStringWidth(s);
			mc.fontRendererObj.drawString(s, -(int) ((float) strWidth / 2f), 0, Color.WHITE.hashCode());
		}
		GlStateManager.popMatrix();
	}

	private void renderSkull(float x, float y, float z, GameProfile profile, int destroyStage, float animateTicks)
	{
		if(destroyStage >= 0)
		{
			bindTexture(DESTROY_STAGES[destroyStage]);
			GlStateManager.matrixMode(5890);
			GlStateManager.pushMatrix();
			GlStateManager.scale(4.0F, 2.0F, 1.0F);
			GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
			GlStateManager.matrixMode(5888);
		}
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

		bindTexture(resourcelocation);

		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5F, y, z + 0.5F);

		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(-1.0F, -1.0F, 1.0F);
		GlStateManager.enableAlpha();
		GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);

		humanoidHead.render(null, animateTicks, 0.0F, 0.0F, 0, 0.0F, 0.0625F);
		GlStateManager.popMatrix();

		if(destroyStage >= 0)
		{
			GlStateManager.matrixMode(5890);
			GlStateManager.popMatrix();
			GlStateManager.matrixMode(5888);
		}
	}
}
