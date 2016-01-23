package com.fireball1725.graves.proxy;

import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.client.render.TileEntityHeadStoneRenderer;
import com.fireball1725.graves.entity.Entities;
import com.fireball1725.graves.reference.ModInfo;
import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {
    // Client side only

    @Override
    public void registerBlocks() {
        super.registerBlocks();

        OBJLoader.instance.addDomain(ModInfo.MOD_ID);
        Item graveItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVESTONE.block);
        ModelLoader.setCustomModelResourceLocation(graveItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":gravestone", "inventory"));
        Item headstoneItem = Item.getItemFromBlock(Blocks.BLOCK_GRAVE_HEADSTONE.block);
        ModelLoader.setCustomModelResourceLocation(headstoneItem, 0, new ModelResourceLocation(ModInfo.MOD_ID + ":headstone", "inventory"));
    }

    @Override
    public void registerEntities() {
        super.registerEntities();
        Entities.registerEntitiesRenderers();
    }

    @Override
    public void registerRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeadStone.class, TileEntityHeadStoneRenderer.instance());
    }
}
