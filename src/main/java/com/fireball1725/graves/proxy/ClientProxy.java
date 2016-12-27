package com.fireball1725.graves.proxy;

import com.fireball1725.graves.client.event.ClientEvents;
import com.fireball1725.graves.client.render.TEGraveSR;
import com.fireball1725.graves.client.render.entity.EntityRenderer;
import com.fireball1725.graves.client.render.entity.RenderPlayerZombie;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().world;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
	{
		super.preInit(event);
		//Register Block Rendering
		OBJLoader.INSTANCE.addDomain(ModInfo.MOD_ID);
		Item graveItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVE.block);
		if(graveItem != null)
		{
			ModelLoader.setCustomModelResourceLocation(graveItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":grave", "inventory"));
		}
		//Register Entity Rendering
		EntityRenderer.registerEntityRenderer(EntityPlayerZombie.class, RenderPlayerZombie.class);
		EntityPlayerZombie.registerSounds();

		//Register TileEntity Rendering
		TEGraveSR tesr = new TEGraveSR();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGrave.class, tesr);
		//Register Event
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(tesr);
    }

	@Override
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

	@Override
	public void postInit(FMLPostInitializationEvent event)
	{
		super.postInit(event);
	}

    @Override
    public void openGui(int guiID) {
        GuiScreen screen = null;
        switch (guiID) {
        }
        if (screen != null)
            Minecraft.getMinecraft().displayGuiScreen(screen);
    }
}
