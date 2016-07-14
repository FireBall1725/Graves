package com.fireball1725.graves.client.render;

import com.fireball1725.graves.client.event.EventTick;
import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
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
	private static Map<String, String> specialText = Maps.newHashMap();

	static
	{
		specialText.put("4f3a8d1e-33c1-44e7-bce8-e683027c7dac", "R.I.P XyCraft!");                // Soaryn
		specialText.put("eb8ef7b9-3199-4a50-99e8-76a48baa6fdf", "Now with more grass!");        // GlassPelican
		specialText.put("a2987599-a85d-47e8-a241-4eb20610adf3", "He was N00B!");                // Techno
		specialText.put("bbb87dbe-690f-4205-bdc5-72ffb8ebc29d", "DireDerp%n%");                    // DireWolf20
		specialText.put("ed2d2e2c-654c-4e85-aa99-300f13561515", "You are dead!");                // MolecularPhylo
		specialText.put("d094e16a-5b8c-4e93-9e8e-2cd6a11d2993", "NEWT");                        // ScottyBug
		specialText.put("1454841b-ba54-4071-80c0-5b51c2f2d25d", "Clukington");                    // Wyld
		specialText.put("0192723f-b3dc-495a-959f-52c53fa63bff", "#PoolParty2016\\n#TooSoon?");    // Pahimar
		specialText.put("c2024e2a-dd76-4bc9-9ea3-b771f18f23b6", "Dire's wolf 20...16");            // TloveTech
		specialText.put("e6b5c088-0680-44df-9e1b-9bf11792291b", "ɯnɹƃ");                        // Grumm
		specialText.put("0765249a-5c6b-4703-a4c6-3f9d867258d3", "RacecaR");                        // racing_pro
		specialText.put("48a16fc8-bc1f-4e72-84e9-7ec73b7d8ea1", "But what does it mean????");    // TTFTCUTS
		specialText.put("892017ed-e9a0-4f25-9a37-b8811614707d", "Channels are Fun!");            // AlgorithmX2
		specialText.put("d3cf097a-438f-4523-b770-ec11e13ecc32", "<PING>");                        // LexManos
		specialText.put("9a0c55e2-635d-4b7b-aa62-b8fab574af35", "");                            // Rajecent
		specialText.put("92914230-9d52-4333-b3ba-e53d97393b6d", "");                            // Hermyone
		specialText.put("92d45906-7a50-4742-85b6-b079db9dc189", "");                            // bspkrs
		specialText.put("d2839efc-727a-4263-97ce-3c73cdee5013", "");                            // Slowpoke101
		specialText.put("af148380-4ba5-4a3d-a47d-710f710f9265", "");                            // Minalien
		specialText.put("59af7399-5544-4990-81f1-c8f2263b00e5", "");                            // cpw
		specialText.put("95fe0728-e1bd-4989-a980-3d8976aedda9", "");                            // WayOfFlowingTime
		specialText.put("25d4006e-3475-4608-9c15-519b08525c11", "");                            // flamegoat
		specialText.put("98beecaf-555e-4064-9401-b531fb927641", "");                            // Tahg
		specialText.put("e28850e0-25ab-47ff-9317-bc73bbbc98e1", "");                            // Vaygrim
		specialText.put("64e7102f-e199-414f-90d7-ccbdaf82284a", "");                            // NikkiExDee
		specialText.put("0f95811a-b3b6-4dba-ba03-4adfec7cf5ab", "");                            // azanor
		specialText.put("5a8c3be3-2aa4-4e91-8d7f-4e7b0dc8223e", "#BlameAma");                   // amadornes
		specialText.put("6078f46a-bae3-496b-bbdc-dcc25aca75ba", "");                            // boni
		specialText.put("b43d09c7-d356-4319-8d4d-afa6974eb665", "");                            // CrazyPants
		specialText.put("b9a89002-b392-4545-ab4d-5b1ff60c88a6", "");                            // Arkember
		specialText.put("8c826f34-113b-4238-a173-44639c53b6e6", "");                            // Vazkii
		specialText.put("183baa71-8cd0-4acb-9a0c-ff06712430a5", "");                            // NewCastleGeek
		specialText.put("2efa46fa-2948-4d98-b822-fa182d254870", "");                            // LordDusk
		specialText.put("2dea7072-2bfe-4c8b-a819-a9b4444134fc", "Failed so hard!\\nand got so far\\nbut in the end\\nIt doesn't even matter.");                            // OneOldBlockHead

		specialText.put("e43e9766-f903-48e1-818f-d41bb48d80d5", "Everything is " + ChatFormatting.RED + "AWESOME" + ChatFormatting.RESET + "!");                // FireBall1725
		specialText.put("7ff65fdc-2837-4123-adfd-157b37527daf", ChatFormatting.GREEN + "\\☻/\\n" + ChatFormatting.GREEN + "|\\n" + ChatFormatting.GREEN + "/\\");            // FusionLord
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
						if(Minecraft.getMinecraft().gameSettings.fancyGraphics)
						{
							String text = "";
							if(specialText.containsKey(profile.getId().toString()))
							{
								text = "\\n" + specialText.get(profile.getId().toString()).replace("%n%", String.valueOf(rand.nextInt()));
							}
							drawText(profile.getName() + text);
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

	private void drawText(String text)
	{
		String[] strings = text.split("\\\\n");
		GlStateManager.pushMatrix();
		for(EnumFacing facing : EnumFacing.HORIZONTALS)
		{
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.rotate(180, 0, 0, 1);
			GlStateManager.rotate((facing.getHorizontalIndex() * 90), 0, 1, 0);
			GlStateManager.translate(0, .55, -.38);
			GlStateManager.scale(.00625, .00625, .00625);
			for(String s : strings)
			{
				GlStateManager.translate(0, mc.fontRendererObj.FONT_HEIGHT, 0);
				int strWidth = mc.fontRendererObj.getStringWidth(s);
				mc.fontRendererObj.drawString(s, -(int) ((float) strWidth / 2f), 0, Color.WHITE.hashCode());
			}
			GlStateManager.enableLighting();
			GlStateManager.popMatrix();
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
