package com.fireball1725.graves.common.event;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.fireball1725.graves.common.util.TileTools;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Events
{
	private Map<UUID, InventoryPlayer> inventories = Maps.newHashMap();

	/**
	 * @param mod    Your @Mod.instance object.
	 * @param player The player we are setting the gps for.
	 * @param pos    The destination position.
	 * @param text   The short description of the destination
	 */

	private static void sendTomTomPos(Object mod, EntityPlayer player, BlockPos pos, String text)
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setLong("location", pos.toLong());
		tag.setLong("uuid-most", player.getUniqueID().getMostSignificantBits());
		tag.setLong("uuid-least", player.getUniqueID().getLeastSignificantBits());
		tag.setString("text", text);
		FMLInterModComms.sendRuntimeMessage(mod, "tomtom", "setPointer", tag);
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
	public void onLivingDeath(LivingDeathEvent event)
	{
		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			InventoryPlayer inv = new InventoryPlayer(player);
			inv.copyInventory(player.inventory);
			inventories.put(player.getPersistentID(), inv);
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW, receiveCanceled = true)
	public void onPlayerDrops(PlayerDropsEvent event)
	{
		World world = event.getEntityPlayer().worldObj;
		if(world.isRemote)
		{ return; }

		if(event.isCanceled())
		{
			Graves.logger.warn(">>>");
			return;
		}

		// Check to see if the gamerule keep inventory is enabled
		final GameRules gameRules = world.getGameRules();
		if(gameRules.hasRule("keepInventory"))
		{
			if(gameRules.getBoolean("keepInventory"))
			{ return; }
		}

		EntityPlayer player = event.getEntityPlayer();
		List<ItemStack> itemsList = Lists.newArrayList();
		for(EntityItem entityItem : event.getDrops())
		{
			itemsList.add(entityItem.getEntityItem());
		}

		// If there are no items, then cancel spawning a grave
		if(itemsList.isEmpty())
		{ return; }

		boolean spawnGrave = true;

		if(event.getEntityLiving() instanceof EntityPlayer)
		{
			if(event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayerZombie)
			{
				EntityPlayerZombie zombie = (EntityPlayerZombie) event.getSource().getEntity();
				BlockPos gravePos = zombie.getGravePos();
				TileEntityGrave graveStone = TileTools.getTileEntity(event.getEntityLiving().worldObj, gravePos, TileEntityGrave.class);
				if (graveStone != null)
				{
					graveStone.addItems(itemsList);
					spawnGrave = false;
					//					LogHelper.info(">>> : Killed by zombie added drops to grave");
				}
			}
		}

		if(spawnGrave)
		{
			BlockPos pos = player.getPosition();
			IBlockState state = Blocks.BLOCK_GRAVE.block.getDefaultState();

			if(pos.getY() <= 2)
			{ pos = new BlockPos(pos.getX(), 3, pos.getZ()); }

			if(pos.getY() >= 254)
			{ pos = new BlockPos(pos.getX(), 253, pos.getZ()); }

			ReplaceableBlock replaceableBlock;
			NBTTagCompound tag = null;
			if(world.getTileEntity(pos) != null)
			{
				tag = new NBTTagCompound();
				world.getTileEntity(pos).writeToNBT(tag);
			}
			replaceableBlock = new ReplaceableBlock(world.getBlockState(pos), pos, tag);

			world.setBlockState(pos, state);

			TileEntityGrave graveStoneTileEntity = TileTools.getTileEntity(world, pos, TileEntityGrave.class);
			if(graveStoneTileEntity.getWorld() == null)
			{ graveStoneTileEntity.setWorldObj(world); }
			graveStoneTileEntity.addGraveItemsWithHotbar(inventories.remove(player.getPersistentID()), itemsList);
			graveStoneTileEntity.setOriginalBlock(replaceableBlock);
			graveStoneTileEntity.setProfile(player.getGameProfile());

			sendTomTomPos(Graves.instance, player, pos, "Grave this way!");
		}

		event.getDrops().clear();
	}

	@SubscribeEvent
	public void onBreakBlock(BlockEvent.BreakEvent event)
	{
		World world = event.getWorld();
		if(!(event.getState().getBlock() instanceof BlockGrave))
		{ return; }

		TileEntityGrave grave = (TileEntityGrave) world.getTileEntity(event.getPos());

		if(grave == null)
		{ return; }

		if(!grave.isGhostDefeated() && !event.getPlayer().isCreative())
		{
			grave.summonGhost(event.getPlayer());
			event.setCanceled(true);
			return;
		}

		grave.replaceItems(event.getPlayer());

		if(!event.getPlayer().isCreative())
		{
			grave.dropItems(event.getPlayer());
			if(Blocks.BLOCK_GRAVE.block.canHarvestBlock(world, event.getPos(), event.getPlayer()))
			{ world.spawnEntityInWorld(new EntityItem(world, event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Blocks.BLOCK_GRAVE.block))); }
		}
		ReplaceableBlock block = null;
		if(grave.getOriginalBlock() != null)
		{ block = grave.getOriginalBlock().copy(); }

		world.setBlockToAir(event.getPos());
		world.removeTileEntity(event.getPos());

		if(block != null)
		{ block.placeBlock(world); }
		event.setCanceled(true);
	}
}
