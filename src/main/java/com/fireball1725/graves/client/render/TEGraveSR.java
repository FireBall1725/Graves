package com.fireball1725.graves.client.render;

import com.fireball1725.graves.client.event.ClientEvents;
import com.fireball1725.graves.common.helpers.PatreonHelper;
import com.fireball1725.graves.common.integration.IsLoaded;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelHumanoidHead;
import net.minecraft.client.model.ModelSkeletonHead;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class TEGraveSR extends TileEntitySpecialRenderer<TileEntityGrave> implements IResourceManagerReloadListener
{
    private final ModelSkeletonHead humanoidHead = new ModelHumanoidHead();
    private Minecraft mc = Minecraft.getMinecraft();
	private Random rand = new Random();

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
        PatreonHelper.getSpecialText();
    }

	@Override
    public void renderTileEntityAt(TileEntityGrave grave, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + .5, y + .5, z + .5);
        GlStateManager.pushMatrix();
        if (grave.getDisplayStack().getItem() != Item.getItemFromBlock(Blocks.AIR) && IsLoaded.CHISELSANDBITS) {
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
            IBakedModel model = mc.getRenderItem().getItemModelWithOverrides(stack, mc.world, mc.player);
            mc.getRenderItem().renderItem(stack, model);
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
        } else {
            if (grave.getProfile() == null) {
                GlStateManager.popMatrix();
                GlStateManager.popMatrix();
                return;
            }

            GameProfile profile = grave.getProfile();
            String text = "";
            boolean patron = false, icon = false;
            rand.setSeed(grave.getPos().toLong());
            if (PatreonHelper.specialText.containsKey(profile.getId().toString())) {
                Map<String, String> map = PatreonHelper.specialText.get(profile.getId().toString());
                patron = map.containsKey("patron") && Boolean.valueOf(map.get("patron"));
                icon = IsLoaded.FUSIONSPATRONS && map.containsKey("icon") && Boolean.valueOf(map.get("icon"));
                text = "\\n" + map.get("text").replace("%n%", String.valueOf(rand.nextInt(Integer.MAX_VALUE)));
            }

            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();

            GlStateManager.translate(0, .5, 0);

            int dir = rand.nextBoolean() ? 1 : -1;
            GlStateManager.pushMatrix();
            if (profile.getId().toString().equals("4f3a8d1e-33c1-44e7-bce8-e683027c7dac")) {
                GlStateManager.rotate(ClientEvents.getTick() * 10, 0, dir, 0);
            } else {
                GlStateManager.rotate(ClientEvents.getTick(), 0, dir, 0);
            }
            GlStateManager.rotate(rand.nextFloat() * 360f, 0, 1, 0);

            GlStateManager.scale(.65, .65, .65);
            GlStateManager.color(1, 1, 1, .8f);
            renderSkull(-.5f, .0001f, -.5f, grave.getProfile(), destroyStage, partialTicks);
            GlStateManager.popMatrix();

            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                GlStateManager.pushMatrix();
                GlStateManager.disableLighting();
                GlStateManager.rotate(180, 0, 0, 1);
                GlStateManager.rotate((facing.getHorizontalIndex() * 90), 0, 1, 0);
                GlStateManager.translate(0, .55, -.38);
                GlStateManager.scale(.00625, .00625, .00625);
                GlStateManager.pushMatrix();
                GlStateManager.translate(-16, -18, 0);
                if (patron && icon)
                    GlStateManager.translate(-18, 0, 0);
                if (patron) {
                    drawPatronIcon();
                }
                if (patron && icon)
                    GlStateManager.translate(34, 0, 0);
                if (icon) {
                    drawIcon(profile.getId().toString());
                }
                GlStateManager.popMatrix();
                if (Minecraft.getMinecraft().player.getPosition().distanceSq(grave.getPos()) <= 100D)
                    drawText(ChatFormatting.UNDERLINE + profile.getName() + ChatFormatting.RESET + text);
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
            }
            GlStateManager.disableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

	private void drawIcon(String icon) {
		GlStateManager.color(1, 1, 1);
        bindTexture(new ResourceLocation("fusionlordspatrons:textures/patrons/" + icon + ".png"));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1, 1, 28, 28, 1, 1);
    }

	private void drawPatronIcon() {
        GlStateManager.color(1, 1, 1);
        bindTexture(new ResourceLocation(ModInfo.MOD_ID, "textures/patron.png"));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0, 0, 1, 1, 28, 28, 1, 1);
    }

	private void drawText(String text)
	{
		String[] strings = text.split("\\\\n");
		GlStateManager.pushMatrix();
		for (String s : strings)
		{
			GlStateManager.translate(0, mc.fontRendererObj.FONT_HEIGHT + 1, 0);
			int strWidth = mc.fontRendererObj.getStringWidth(s);
			mc.fontRendererObj.drawString(s, -(int) ((float) strWidth / 2f), 0, Color.WHITE.hashCode());
		}
		GlStateManager.popMatrix();
	}

    @SuppressWarnings("all")
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
