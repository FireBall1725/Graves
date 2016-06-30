package com.fireball1725.graves.proxy;

import com.fireball1725.graves.client.events.RenderEvents;
import com.fireball1725.graves.client.render.TileEntityHeadStoneRenderer;
import com.fireball1725.graves.client.render.entity.EntityRenderers;
import com.fireball1725.graves.client.render.entity.RenderPlayerZombie;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityHeadStone;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
    // Client side only

    @Override
    public void registerBlocks() {
        super.registerBlocks();
		OBJLoader.INSTANCE.addDomain(ModInfo.MOD_ID);
		Item graveItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVESTONE.block);
        ModelLoader.setCustomModelResourceLocation(graveItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":gravestone", "inventory"));
		graveItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVESTONE_SLAVE.block);
		ModelLoader.setCustomModelResourceLocation(graveItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":graveslave", "inventory"));
		graveItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVE_HEADSTONE.block);
		ModelLoader.setCustomModelResourceLocation(graveItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":headstone", "inventory"));
    }

	@Override
	public void registerEntities()
	{
		super.registerEntities();
		EntityRenderers.registerEntityRenderer(EntityPlayerZombie.class, RenderPlayerZombie.class);
		EntityPlayerZombie.registerSounds();
	}

	@Override
	@SuppressWarnings("unchecked")
	public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeadStone.class, TileEntityHeadStoneRenderer.instance());
		RenderEvents renderEvents = new RenderEvents();
		MinecraftForge.EVENT_BUS.register(renderEvents);

		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(renderEvents);
    }
}
