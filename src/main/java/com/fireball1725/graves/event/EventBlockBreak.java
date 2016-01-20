package com.fireball1725.graves.event;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBlockBreak {
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
		if(event.getPlayer().capabilities.isCreativeMode)
		{ return; }
		TileEntityGraveStone graveStone = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveStone.class);
        if (graveStone != null) {
            if (graveStone.getHasLid()) {
                graveStone.setHasLid(false);
                graveStone.markDirty();
                graveStone.markForUpdate();

                event.setCanceled(true);
            }
        }
    }
}
