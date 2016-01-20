package com.fireball1725.graves.event;

import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
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
				LogHelper.info(">>> breaking lid");
				event.setCanceled(true);
				return;
			}
        }
		TileEntityGraveSlave graveSlave = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveSlave.class);
		if(graveSlave != null)
		{
			BlockPos masterBlock = graveSlave.getMasterBlock();
			LogHelper.info(">>> Master Pos: " + masterBlock.toString());
			MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(event.world, masterBlock, event.world.getBlockState(masterBlock), event.getPlayer()));
			event.setCanceled(true);
		}
	}
}
