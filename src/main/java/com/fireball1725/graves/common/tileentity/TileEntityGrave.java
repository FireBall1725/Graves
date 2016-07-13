package com.fireball1725.graves.common.tileentity;

import com.fireball1725.graves.common.configuration.ConfigZombie;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.helpers.ItemHelper;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;

import java.util.List;
import java.util.ListIterator;

public class TileEntityGrave extends TileEntityBase
{
	private ItemStack displayStack = null;
	private List<ItemStack> items;
	private ItemStack[] hotbar = new ItemStack[InventoryPlayer.getHotbarSize()];
	private GameProfile profile;
	private ReplaceableBlock originalBlock;
	private boolean ghostDefeated;

	public TileEntityGrave()
	{
		super();
	}

	public ItemStack getDisplayStack()
	{
		return displayStack;
	}

	public void setDisplayStack(ItemStack displayStack)
	{
		this.displayStack = displayStack;
	}

	public GameProfile getProfile()
	{
		return profile;
	}

	public void setProfile(GameProfile profile)
	{
		this.profile = TileEntitySkull.updateGameprofile(profile);
	}

	public void replaceItems(EntityPlayer player)
	{
		InventoryPlayer inventory = player.inventory;
		List<ItemStack> remaining = Lists.newArrayList();
		for(int i = 0; i < inventory.mainInventory.length; i++)
		{
			if(i >= hotbar.length)
			{ break; }
			ItemStack currentItem = inventory.mainInventory[i];
			ItemStack replaceItem = hotbar[i];
			if(InventoryPlayer.isHotbar(i) && currentItem == null && replaceItem != null)
			{
				inventory.mainInventory[i] = replaceItem;
			}
			else
			{
				remaining.add(replaceItem);
			}
		}
		for(ItemStack remainingStack : remaining)
		{
			if(!inventory.addItemStackToInventory(remainingStack) && remainingStack != null && remainingStack.stackSize >= 1)
			{
				EntityItem entityItem = new EntityItem(worldObj, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), remainingStack);
				entityItem.motionX = 0;
				entityItem.motionY = 0;
				entityItem.motionZ = 0;
				worldObj.spawnEntityInWorld(entityItem);
			}
		}
		inventory.markDirty();
	}

	public void summonGhost(EntityPlayer player)
	{
		boolean spawnPlayerZombie = false;

		int spawnChance = 40;

		boolean hardcoreEnabled = worldObj.getWorldInfo().isHardcoreModeEnabled();
		EnumDifficulty gameDifficulty = worldObj.getDifficulty();

		switch(gameDifficulty)
		{
			case EASY:
				spawnChance = ConfigZombie.configZombieSpawnChanceEasy;
				break;

			case NORMAL:
				spawnChance = ConfigZombie.configZombieSpawnChanceNormal;
				break;

			case HARD:
				spawnChance = ConfigZombie.configZombieSpawnChanceHard;
				break;
		}

		if(hardcoreEnabled)
		{
			spawnChance = ConfigZombie.configZombieSpawnChanceHardCore;
		}

		if(spawnChance > 0)
		{
			int rng = worldObj.rand.nextInt(100);

			if(rng <= spawnChance)
			{ spawnPlayerZombie = true; }
		}

		if(spawnPlayerZombie && ConfigZombie.configZombieEnabled)
		{
			EntityPlayerZombie playerZombie = new EntityPlayerZombie(worldObj, pos);

			playerZombie.setProfile(profile);

			playerZombie.setLocationAndAngles(pos.getX(), pos.down().getY(), pos.getZ(), player.getHorizontalFacing().getOpposite().getHorizontalIndex() * 90f, 0f);
			playerZombie.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(playerZombie)), null);
			playerZombie.setPlayer(player);
			worldObj.spawnEntityInWorld(playerZombie);
		}
		else
		{
			setGhostDefeated(true);
		}

	}

	public boolean isGhostDefeated()
	{
		return ghostDefeated;
	}

	public void setGhostDefeated(boolean ghostDefeated)
	{
		this.ghostDefeated = ghostDefeated;
	}

	public ReplaceableBlock getOriginalBlock()
	{
		return originalBlock;
	}

	public void setOriginalBlock(ReplaceableBlock originalBlock)
	{
		this.originalBlock = originalBlock;
	}

	public void addGraveItemsWithHotbar(InventoryPlayer inventory, List<ItemStack> items)
	{
		System.arraycopy(inventory.mainInventory, 0, hotbar, 0, InventoryPlayer.getHotbarSize());

		rItems:
		for(ItemStack stack : hotbar)
		{
			ListIterator<ItemStack> iterator = items.listIterator();
			ItemStack stack1;
			while(iterator.hasNext())
			{
				stack1 = iterator.next();

				if(ItemHelper.doesItemHaveEnchant("enderio:soulBound", stack1))
				{
					iterator.remove();
					continue;
				}

				if(areItemEqual(stack, stack1))
				{
					iterator.remove();
					continue rItems;
				}
			}
		}
		for(int i = 0; i < hotbar.length; i++)
		{
			ItemStack stack = hotbar[i];
			if(ItemHelper.doesItemHaveEnchant("enderio:soulBound", stack))
			{
				hotbar[i] = null;
			}
		}
		addItems(items);
	}

	private boolean areItemEqual(ItemStack stack, ItemStack stack1)
	{
		boolean flag = ItemStack.areItemsEqual(stack, stack1);
		if(stack != null && stack1 != null)
		{
			if((stack.hasTagCompound() && !stack1.hasTagCompound()) || (!stack.hasTagCompound() && stack1.hasTagCompound()))
			{
				return false;
			}
			if(stack.hasTagCompound() && stack1.hasTagCompound())
			{
				return flag && stack.getTagCompound().equals(stack1.getTagCompound());
			}
		}
		return flag;
	}

	public void addItems(List<ItemStack> items)
	{
		this.items = Lists.newArrayList();
		this.items.addAll(items);
	}

	public void dropItems(EntityPlayer player)
	{
		if(items == null || items.isEmpty())
		{ return; }
		for(ItemStack stack : items)
		{
			if(!player.inventory.addItemStackToInventory(stack))
			{
				EntityItem entityItem = new EntityItem(worldObj, player.posX, player.posY, player.posZ, stack);
				entityItem.motionX = 0;
				entityItem.motionY = 0;
				entityItem.motionZ = 0;
				worldObj.spawnEntityInWorld(entityItem);
			}
		}
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), serializeNBT());
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity sPacketUpdateTileEntity)
	{
		deserializeNBT(sPacketUpdateTileEntity.getNbtCompound());
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{
		super.writeToNBT(nbtTagCompound);
		//Save Display Stack
		if(displayStack != null)
		{ nbtTagCompound.setTag("displayStack", displayStack.serializeNBT()); }
		nbtTagCompound.setBoolean("isGhostDefeated", isGhostDefeated());
		//Save Profile
		if(profile != null)
		{
			NBTTagCompound profileTag = new NBTTagCompound();
			NBTUtil.writeGameProfile(profileTag, profile);
			nbtTagCompound.setTag("profileTag", profileTag);
		}
		//Save Items
		if(items != null && !items.isEmpty())
		{
			NBTTagCompound itemsTag = new NBTTagCompound();
			itemsTag.setInteger("itemCount", items.size());
			for(int i = 0; i < items.size(); i++)
			{
				if(items.get(i) != null)
				{ itemsTag.setTag(String.valueOf(i), items.get(i).serializeNBT()); }
			}
			nbtTagCompound.setTag("itemsTag", itemsTag);
		}
		//Save Hotbar
		if(hotbar.length > 1)
		{
			NBTTagCompound hotbarTag = new NBTTagCompound();
			hotbarTag.setInteger("itemCount", hotbar.length);
			for(int i = 0; i < hotbar.length; i++)
				if(hotbar[i] != null)
				{ hotbarTag.setTag(String.valueOf(i), hotbar[i].serializeNBT()); }
			nbtTagCompound.setTag("hotbarTag", hotbarTag);
		}
		return nbtTagCompound;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound);
		displayStack = null;
		if(nbtTagCompound.hasKey("displayStack"))
		{ displayStack = ItemStack.loadItemStackFromNBT(nbtTagCompound.getCompoundTag("displayStack")); }
		if(nbtTagCompound.hasKey("isGhostDefeated"))
		{ ghostDefeated = nbtTagCompound.getBoolean("isGhostDefeated"); }
		if(nbtTagCompound.hasKey("profileTag"))
		{ setProfile(NBTUtil.readGameProfileFromNBT(nbtTagCompound.getCompoundTag("profileTag"))); }
		if(nbtTagCompound.hasKey("itemsTag"))
		{
			items = Lists.newArrayList();
			NBTTagCompound itemsTag = nbtTagCompound.getCompoundTag("itemsTag");
			for(int i = 0; i < itemsTag.getInteger("itemCount"); i++)
			{
				items.add(ItemStack.loadItemStackFromNBT(itemsTag.getCompoundTag(String.valueOf(i))));
			}
		}
		if(nbtTagCompound.hasKey("hotbarTag"))
		{
			NBTTagCompound hotbarTag = nbtTagCompound.getCompoundTag("hotbarTag");
			hotbar = new ItemStack[hotbarTag.getInteger("itemCount")];
			for(int i = 0; i < hotbar.length; i++)
			{
				if(hotbarTag.hasKey(String.valueOf(i)))
				{
					hotbar[i] = ItemStack.loadItemStackFromNBT(hotbarTag.getCompoundTag(String.valueOf(i)));
				}
			}
		}

		{ //Legacy Support
			if(nbtTagCompound.hasKey("hasLid"))
			{ ghostDefeated = nbtTagCompound.getBoolean("hasLid"); }
			if(nbtTagCompound.hasKey("playerProfile"))
			{ profile = NBTUtil.readGameProfileFromNBT(nbtTagCompound.getCompoundTag("playerProfile")); }
			if(nbtTagCompound.hasKey("replaceableBlocks"))
			{
				NBTTagCompound replaceableTag = nbtTagCompound.getCompoundTag("replaceableBlocks");
				int size = replaceableTag.getInteger("size");
				for(int i = 0; i < size; i++)
				{
					ReplaceableBlock block = ReplaceableBlock.readNBT((NBTTagCompound) replaceableTag.getTag("block:" + i));
					if(block != null)
					{
						if(block.getPos().equals(getPos()))
						{
							originalBlock = block;
						}
						else
						{
							worldObj.setBlockToAir(block.getPos());
							block.placeBlock(worldObj);
						}
					}
				}
			}
			if(nbtTagCompound.hasKey("replaceableItems"))
			{
				ItemStack[] replaceableItems;
				NBTTagCompound tag = nbtTagCompound.getCompoundTag("replaceableItems");
				replaceableItems = new ItemStack[tag.getInteger("size")];
				for(int i = 0; i < replaceableItems.length; i++)
				{
					if(tag.hasKey("item:" + i))
					{
						replaceableItems[i] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item:" + i));
					}
				}
			}
			int invSize = 0;
			if(nbtTagCompound.hasKey("inventorySize"))
			{ invSize = nbtTagCompound.getInteger("inventorySize"); }
			if(nbtTagCompound.hasKey("Items"))
			{
				items = Lists.newArrayList();
				NBTTagCompound tagCompound = nbtTagCompound.getCompoundTag("Items");
				for(int i = 0; i < invSize; i++)
				{
					NBTTagCompound item = tagCompound.getCompoundTag("item" + i);
					items.add(ItemStack.loadItemStackFromNBT(item));
				}
			}
		} // End of Legacy Support
	}
}
