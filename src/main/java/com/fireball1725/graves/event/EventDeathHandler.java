package com.fireball1725.graves.event;

import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.helpers.SafeBlockReplacer;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventDeathHandler {
    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onPlayerDrops(PlayerDropsEvent event) {
        World world = event.entityPlayer.worldObj;
        if (world.isRemote)
            return;

        if (event.isCanceled()) {
            LogHelper.warn(">>>");
            return;
        }

        // Check to see if the gamerule keep inventory is enabled
        final GameRules gameRules = world.getGameRules();
        if (gameRules.hasRule("keepInventory")) {
            if (gameRules.getBoolean("keepInventory"))
                return;
        }

        final List<EntityItem> itemsList = event.drops;

        // If there are no items, then cancel spawning a grave
        if (itemsList.isEmpty())
            return;

        //for (EntityItem item : itemsList) {
        //    LogHelper.info(">>> " + item.toString());
        //}


        BlockPos safePos = SafeBlockReplacer.GetSafeGraveSite(world, event.entityPlayer.getPosition());

        world.setBlockState(safePos, Blocks.BLOCK_GRAVESTONE.block.getDefaultState());
        TileEntityGraveStone graveStoneTileEntity = TileTools.getTileEntity(world, safePos, TileEntityGraveStone.class);
        graveStoneTileEntity.setGraveItems(itemsList, event.entityPlayer);



        event.drops.clear();
    }
}
