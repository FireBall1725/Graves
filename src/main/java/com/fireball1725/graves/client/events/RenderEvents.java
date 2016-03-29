package com.fireball1725.graves.client.events;

import com.fireball1725.graves.block.BlockGraveSlave;
import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class RenderEvents implements IResourceManagerReloadListener
{

	private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];

	@SubscribeEvent
	public void renderExtraBlockBreak(RenderWorldLastEvent event) {
		PlayerControllerMP controllerMP = Minecraft.getMinecraft().playerController;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = player.worldObj;

		List<BlockPos> blocks = new ArrayList<BlockPos>();

		// extra-blockbreak animation
		if(controllerMP.isHittingBlock) {
			if(controllerMP.currentBlock != null)
			{
				Block block = world.getBlockState(controllerMP.currentBlock).getBlock();
				if (block instanceof BlockGraveSlave || block instanceof BlockGraveStone)
				{
					BlockPos pos = controllerMP.currentBlock;
					if(block instanceof BlockGraveSlave)
					{
						TileEntityGraveSlave slave = TileTools.getTileEntity(world, pos, TileEntityGraveSlave.class);
						if(slave == null || slave.getMasterBlock() == null)
							return;
						pos = slave.getMasterBlock();
					}
					if(pos == null)
						return;
					TileEntityGraveStone master = TileTools.getTileEntity(world, pos, TileEntityGraveStone.class);
					if (master == null) return;
					blocks.add(pos);
					blocks.add(pos.offset(master.getBlockState().getValue(BlockGraveStone.FACING)));
					blocks.add(pos.down());
					blocks.add(pos.down().offset(master.getBlockState().getValue(BlockGraveStone.FACING)));

					drawBlockDamageTexture(Tessellator.getInstance(),
							Tessellator.getInstance().getWorldRenderer(),
							player,
							event.partialTicks,
							world,
							blocks);
				}
			}
		}
	}

	// RenderGlobal.drawBlockDamageTexture
	public void drawBlockDamageTexture(Tessellator tessellatorIn, WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, World world, List<BlockPos> blocks)
	{
		double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
		double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
		double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;

		TextureManager renderEngine = Minecraft.getMinecraft().renderEngine;
		int progress = (int)(Minecraft.getMinecraft().playerController.curBlockDamageMP * 10f) - 1; // 0-10

		if(progress < 0)
			return;

		renderEngine.bindTexture(TextureMap.locationBlocksTexture);
		//preRenderDamagedBlocks BEGIN
		GlStateManager.pushMatrix();
		GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
		GlStateManager.enableBlend();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
		GlStateManager.doPolygonOffset(-3.0F, -3.0F);
		GlStateManager.enablePolygonOffset();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableAlpha();
		//preRenderDamagedBlocks END

		worldRendererIn.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
		worldRendererIn.setTranslation(-d0, -d1, -d2);
//		worldRendererIn.markDirty();

		for(BlockPos blockpos : blocks) {
			Block block = world.getBlockState(blockpos).getBlock();
			TileEntity te = world.getTileEntity(blockpos);
			boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest
					|| block instanceof BlockSign || block instanceof BlockSkull;
			if (!hasBreak) hasBreak = te != null && te.canRenderBreaking();

			if (!hasBreak)
			{
				IBlockState iblockstate = world.getBlockState(blockpos);

				if(iblockstate.getBlock().getMaterial() != Material.air)
				{
					TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[progress];
					BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
					blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, world);
				}
			}
		}

		tessellatorIn.draw();
		worldRendererIn.setTranslation(0.0D, 0.0D, 0.0D);
		// postRenderDamagedBlocks BEGIN
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0.0F, 0.0F);
		GlStateManager.disablePolygonOffset();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
		// postRenderDamagedBlocks END
	}

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		TextureMap texturemap = Minecraft.getMinecraft().getTextureMapBlocks();

		for (int i = 0; i < this.destroyBlockIcons.length; ++i)
		{
			this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
		}
	}
}
