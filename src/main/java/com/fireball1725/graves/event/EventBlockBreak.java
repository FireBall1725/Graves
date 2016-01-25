package com.fireball1725.graves.event;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.entity.EntityPlayerZombie;
import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class EventBlockBreak {
    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        TileEntityGraveStone graveStone = TileTools.getTileEntity(event.world, event.pos, TileEntityGraveStone.class);
        if (graveStone != null) {
            if (graveStone.getHasLid() && !event.getPlayer().capabilities.isCreativeMode) {
                graveStone.setHasLid(false);
                graveStone.markDirty();
                graveStone.markForUpdate();

                boolean spawnPlayerZombie = false;

                //todo: make if player has items, make the chance less
                int spawnChance = 40;

                /* Notes :

                    Artifacts:
                    > 4x Artifacts, each one lowers the zombie spawning chance

                 */

                if (spawnChance > 0) {
                    Random random = new Random();
                    int rng = random.nextInt(100);

                    if (rng <= spawnChance)
                        spawnPlayerZombie = true;
                }

                if (spawnPlayerZombie) {
                    EntityPlayerZombie playerZombie = new EntityPlayerZombie(event.world);

                    playerZombie.setUsername(graveStone.getPlayerProfile().getName());
                    playerZombie.setProfile(graveStone.getPlayerProfile());

                    playerZombie.setLocationAndAngles(event.pos.getX(), event.pos.down().getY(), event.pos.getZ(), graveStone.getBlockState().getValue(BlockGraveStone.FACING).getHorizontalIndex() * 90f, 0f);
                    playerZombie.onInitialSpawn(event.world.getDifficultyForLocation(new BlockPos(playerZombie)), null);

                    playerZombie.setPlayer(event.getPlayer());

                    NBTTagCompound nbtTagCompound = event.getPlayer().getEntityData();
                    nbtTagCompound.setIntArray("MasterGrave", new int[]{graveStone.getPos().getX(), graveStone.getPos().getY(), graveStone.getPos().getZ()});

                    event.world.spawnEntityInWorld(playerZombie);
                }

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
            MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(event.world, masterBlock, event.world.getBlockState(masterBlock), event.getPlayer()));
            graveSlave.markDirty();
            graveSlave.markForUpdate();
            event.setCanceled(true);
        }
    }
}
