package com.fireball1725.graves.event;

import com.fireball1725.graves.block.BlockGraveStone;
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
        TileEntityGraveStone graveStone = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveStone.class);
        if (graveStone != null) {
            if (graveStone.getHasLid() && !event.getPlayer().capabilities.isCreativeMode) {
                graveStone.setHasLid(false);
                graveStone.markDirty();
                graveStone.markForUpdate();
                //LogHelper.info(">>> breaking lid");
                event.setCanceled(true);
                return;
            } else {
                event.world.setBlockToAir(graveStone.getPos().down());
                event.world.setBlockToAir(graveStone.getPos().down().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)));
                event.world.setBlockToAir(graveStone.getPos().offset(graveStone.getBlockState().getValue(BlockGraveStone.FACING)));
                event.world.setBlockToAir(graveStone.getPos());
            }
        }
        TileEntityGraveSlave graveSlave = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveSlave.class);
        if (graveSlave != null) {
            BlockPos masterBlock = graveSlave.getMasterBlock();
            if (event.world.getTileEntity(masterBlock) == null) {
                return;
            }
            //LogHelper.info(">>> Master Pos: " + masterBlock.toString());
            MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(event.world, masterBlock, event.world.getBlockState(masterBlock), event.getPlayer()));
            graveSlave.markDirty();
            graveSlave.markForUpdate();
            event.setCanceled(true);
        }
    }
}
