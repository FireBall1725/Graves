package com.fireball1725.graves.common.event;

import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEvents {
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        World world = event.getWorld();
        if (!(event.getState().getBlock() instanceof BlockGrave)) {
            return;
        }

        TileEntityGrave grave = (TileEntityGrave) world.getTileEntity(event.getPos());

        if (grave == null) {
            return;
        }

        if (!grave.isGhostDefeated() && !event.getPlayer().isCreative() && grave.summonGhost(event.getPlayer())) {
            event.setCanceled(true);
            return;
        }

        grave.replaceItems(event.getPlayer());

        if (!event.getPlayer().isCreative()) {
            grave.dropItems(event.getPlayer());
            if (Blocks.BLOCK_GRAVE.block.canHarvestBlock(world, event.getPos(), event.getPlayer())) {
                world.spawnEntity(new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Blocks.BLOCK_GRAVE.block)));
            }
        }
        ReplaceableBlock block = null;
        if (grave.getOriginalBlock() != null) {
            block = grave.getOriginalBlock().copy();
        }

        world.setBlockToAir(event.getPos());
        world.removeTileEntity(event.getPos());

        if (block != null) {
            block.placeBlock(world);
        }
        event.setCanceled(true);
    }

}
