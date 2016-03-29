package com.fireball1725.graves.event;

import com.fireball1725.graves.block.BlockGraveSlave;
import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventBlockBreak {
    @SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		if(!(event.state.getBlock() instanceof BlockGraveStone || event.state.getBlock() instanceof BlockGraveSlave))
		{ return; }

		TileEntityGraveStone graveStone = null;

		if(event.state.getBlock() instanceof BlockGraveSlave)
		{
			TileEntityGraveSlave slave = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveSlave.class);
			if(slave != null)
			{ graveStone = TileTools.getTileEntity(event.world, slave.getMasterBlock(), TileEntityGraveStone.class); }
		}
		else
		{
			graveStone = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveStone.class);
		}

		if(graveStone == null)
		{ return; }

		if(graveStone.getBlockState().getValue(BlockGraveStone.HASLID))
		{
			graveStone.breakLid(event.getPlayer());
			event.setCanceled(true);
		}
		else
		{
			event.world.destroyBlock(graveStone.getPos().down(), false);
			event.world.destroyBlock(graveStone.getPos().down().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)), false);
			event.world.destroyBlock(graveStone.getPos().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)), false);
			event.world.destroyBlock(graveStone.getPos(), true);
			event.setCanceled(true);
		}
	}
}
