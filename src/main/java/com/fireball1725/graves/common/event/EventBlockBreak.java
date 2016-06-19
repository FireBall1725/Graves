package com.fireball1725.graves.common.event;

import com.fireball1725.graves.common.block.BlockGraveSlave;
import com.fireball1725.graves.common.block.BlockGraveStone;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.common.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.common.util.TileTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventBlockBreak {
    @SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		if(!(event.getState().getBlock() instanceof BlockGraveStone || event.getState().getBlock() instanceof BlockGraveSlave))
		{ return; }
		//		if (event.getWorld().isRemote) return;

		TileEntityGraveStone graveStone = null;

		if(event.getState().getBlock() instanceof BlockGraveSlave)
		{
			TileEntityGraveSlave slave = TileTools.getTileEntity(event.getWorld(), event.getPos(), TileEntityGraveSlave.class);
			if(slave != null)
			{ graveStone = TileTools.getTileEntity(event.getWorld(), slave.getMasterBlock(), TileEntityGraveStone.class); }
		}
		else
		{
			graveStone = TileTools.getTileEntity(event.getWorld(), event.getPos(), TileEntityGraveStone.class);
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
			List<ReplaceableBlock> blocks = graveStone.getReplaceableBlocks();
			EntityPlayer player = event.getPlayer();
			player.inventory.dropAllItems();
			graveStone.replaceItems(player.inventory);

			event.getWorld().destroyBlock(graveStone.getPos().down(), false);
			event.getWorld().destroyBlock(graveStone.getPos().down().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)), false);
			event.getWorld().destroyBlock(graveStone.getPos().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)), false);
			event.getWorld().destroyBlock(graveStone.getPos(), true);

			for (ReplaceableBlock block : blocks)
			{
				block.placeBlock(event.getWorld());
			}

			event.setCanceled(true);
		}
	}
}
