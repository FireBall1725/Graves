package com.fireball1725.graves.event;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBlockBreak {
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        TileEntityGraveStone graveStone = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveStone.class);
        if (graveStone != null) {
			if(event.state.getBlock().getActualState(event.state, event.world, event.pos).getValue(BlockGraveStone.HASLID))
			{


                graveStone.setHasLid(false);
				event.world.setBlockState(event.pos, event.state.withProperty(BlockGraveStone.HASLID, false));
				//                graveStone.markForUpdate();
				event.world.markBlockForUpdate(event.pos);
				LogHelper.info("test");
				//				event.world.forceBlockUpdateTick(event.state.getBlock(), event.pos, event.world.rand);


                event.setCanceled(true);
            }
        }
    }
}
