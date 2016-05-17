package com.fireball1725.graves.common.event;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.BlockGraveStone;
import com.fireball1725.graves.common.block.BlockHeadStone;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.helpers.LogHelper;
import com.fireball1725.graves.common.helpers.SafeBlockReplacer;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.common.tileentity.TileEntityHeadStone;
import com.fireball1725.graves.common.util.TileTools;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventDeathHandler {
	/**
	 * @param mod    Your @Mod.instance object.
	 * @param player The player we are setting the gps for.
	 * @param pos    The destination position.
	 * @param text   The short description of the destination
	 */

	public static void sendTomTomPos(Object mod, EntityPlayer player, BlockPos pos, String text)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("location", pos.toLong());
		tag.setLong("uuid-most", player.getUniqueID().getMostSignificantBits());
		tag.setLong("uuid-least", player.getUniqueID().getLeastSignificantBits());
		tag.setString("text", text);
		FMLInterModComms.sendRuntimeMessage(mod, "tomtom", "setPointer", tag);
	}

    @SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
    public void onPlayerDrops(PlayerDropsEvent event) {

		World world = event.getEntityPlayer().worldObj;
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

		final List<EntityItem> itemsList = event.getDrops();

        // If there are no items, then cancel spawning a grave
        if (itemsList.isEmpty())
            return;

        boolean spawnGrave = true;

		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayerZombie)
			{
				EntityPlayerZombie zombie = (EntityPlayerZombie) event.getSource().getEntity();
				BlockPos gravePos = zombie.getGraveMaster();
				TileEntityGraveStone graveStone = TileTools.getTileEntity(event.getEntityLiving().worldObj, gravePos, TileEntityGraveStone.class);
				if (graveStone != null)
				{
					graveStone.addGraveItems(itemsList);
					graveStone.setHasLid(true);
					spawnGrave = false;
					//					LogHelper.info(">>> : Killed by zombie added drops to grave");
				}
			}
		}

        if (spawnGrave) {

			EnumFacing playerFacing = event.getEntityPlayer().getHorizontalFacing();
			LogHelper.info(String.format(">>>>>>>>>>  %s, %s, %s, %s", event.getEntityPlayer().getPosition(), event.getEntityPlayer().posX, event.getEntityPlayer().posY, event.getEntityPlayer().posZ));
			BlockPos playerPos = event.getEntityPlayer().getPosition();
			IBlockState state = Blocks.BLOCK_GRAVESTONE.block.getDefaultState().withProperty(BlockGraveStone.FACING, playerFacing);

			List<ReplaceableBlock> blocks = Lists.newArrayList();
			for (BlockPos pos : TileEntityGraveStone.getPositions(playerPos, playerFacing))
			{
				NBTTagCompound tag = null;
				if (world.getTileEntity(pos) != null)
				{
					tag = new NBTTagCompound();
					world.getTileEntity(pos).writeToNBT(tag);
				}
				blocks.add(new ReplaceableBlock(world.getBlockState(pos), pos, tag));
			}

			world.setBlockState(playerPos, state);

            TileEntityGraveStone graveStoneTileEntity = TileTools.getTileEntity(world, playerPos, TileEntityGraveStone.class);
            graveStoneTileEntity.addGraveItems(itemsList);
			graveStoneTileEntity.setReplaceableBlocks(blocks);
            graveStoneTileEntity.breakBlocks();
			graveStoneTileEntity.setPlayerProfile(event.getEntityPlayer().getGameProfile());

            // Adding Headstone
            world.setBlockState(playerPos.offset(playerFacing.getOpposite()), Blocks.BLOCK_GRAVE_HEADSTONE.block.getDefaultState().withProperty(BlockHeadStone.FACING, playerFacing));
            TileEntityHeadStone tileEntityHeadStone = TileTools.getTileEntity(world, playerPos.offset(playerFacing.getOpposite()), TileEntityHeadStone.class);
            if (tileEntityHeadStone != null) {
				tileEntityHeadStone.setCustomName(event.getEntityPlayer().getDisplayName().getFormattedText());
            }
			sendTomTomPos(Graves.instance, event.getEntityPlayer(), playerPos, "Grave this way!");
        }

		event.getDrops().clear();
	}
}
