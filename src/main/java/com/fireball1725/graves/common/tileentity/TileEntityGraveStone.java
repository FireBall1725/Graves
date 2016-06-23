package com.fireball1725.graves.common.tileentity;

import com.fireball1725.graves.common.block.BlockGraveSlave;
import com.fireball1725.graves.common.block.BlockGraveStone;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.configuration.ConfigZombie;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.inventory.InternalDynamicInventory;
import com.fireball1725.graves.common.tileentity.inventory.InventoryOperation;
import com.fireball1725.graves.common.util.TileTools;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.EnumDifficulty;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class TileEntityGraveStone extends TileEntityInventoryBase
{
	private boolean hasLid = true;
    private InternalDynamicInventory internalInventory = new InternalDynamicInventory(this);
    private GameProfile playerProfile;
	private List<ReplaceableBlock> replaceableBlocks = Lists.newArrayList();
	private ItemStack[] replaceableItems = new ItemStack[14];

	private static List<BlockPos> getSlaves(BlockPos pos, EnumFacing facing)
	{
		List<BlockPos> poses = Lists.newArrayList();
		poses.add(pos.down());
		poses.add(pos.down().offset(facing));
		poses.add(pos.offset(facing));
		return poses;
	}

	public static List<BlockPos> getPositions(BlockPos pos, EnumFacing facing)
	{
		List<BlockPos> poses = getSlaves(pos, facing);
		poses.add(pos);
		poses.add(pos.offset(facing.getOpposite()));
		return poses;
	}

	public void addGraveItems(List<ItemStack> itemsList)
	{
		for(ItemStack stack : itemsList)
		{
			internalInventory.addInventorySlotContents(stack);
		}
	}

	public void addGraveItemsWithReplaceables(InventoryPlayer inventory, List<ItemStack> itemsList)
	{
		replaceableItems = new ItemStack[InventoryPlayer.getHotbarSize() + inventory.armorInventory.length + inventory.offHandInventory.length];
		ItemStack itemStack;
		int placeAt;
		for(int i = 0; i < InventoryPlayer.getHotbarSize(); i++)
		{
			placeAt = i;
			itemStack = inventory.mainInventory[i];
			replaceableItems[placeAt] = itemStack;
		}
		for(int i = 0; i < inventory.armorInventory.length; i++)
		{
			placeAt = i + InventoryPlayer.getHotbarSize();
			itemStack = inventory.armorInventory[i];
			replaceableItems[placeAt] = itemStack;
		}
		for(int i = 0; i < inventory.offHandInventory.length; i++)
		{
			placeAt = i + InventoryPlayer.getHotbarSize() + inventory.armorInventory.length;
			itemStack = inventory.offHandInventory[i];
			replaceableItems[placeAt] = itemStack;
		}
		Iterator<ItemStack> listIterator = itemsList.listIterator();
		listIterator:
		while(listIterator.hasNext())
		{
			ItemStack stack1 = listIterator.next();
			if(stack1 != null)
			{
				for(ItemStack stack : replaceableItems)
				{
					if(stack != null)
					{
						if(stack1.isItemEqual(stack))
						{
							if(stack1.hasTagCompound() && stack.hasTagCompound())
							{
								if(stack1.getTagCompound().equals(stack.getTagCompound()))
								{
									listIterator.remove();
									continue listIterator;
								}
							}
							else
							{
								listIterator.remove();
								continue listIterator;
							}
						}
					}
				}
			}
		}
		addGraveItems(itemsList);
	}

    public boolean getHasLid() {
        return hasLid;
    }

    public void setHasLid(boolean hasLid) {
        this.hasLid = hasLid;
		worldObj.notifyBlockUpdate(pos, getBlockState(), getBlockState().withProperty(BlockGraveStone.HASLID, false), 3);
	}

    private GameProfile getPlayerProfile() {
        return playerProfile;
    }

    public void setPlayerProfile(GameProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        this.hasLid = nbtTagCompound.getBoolean("hasLid");
        this.playerProfile = NBTUtil.readGameProfileFromNBT(nbtTagCompound.getCompoundTag("playerProfile"));

		NBTTagCompound replaceableTag = nbtTagCompound.getCompoundTag("replaceableBlocks");
		int size = replaceableTag.getInteger("size");
		replaceableBlocks = Lists.newArrayList();
		for (int i = 0; i < size; i++)
		{
			replaceableBlocks.add(ReplaceableBlock.readNBT((NBTTagCompound) replaceableTag.getTag("block:" + i)));
		}
		NBTTagCompound tag = nbtTagCompound.getCompoundTag("replaceableItems");
		replaceableItems = new ItemStack[tag.getInteger("size")];
		for(int i = 0; i < replaceableItems.length; i++)
		{
			if(tag.hasKey("item:" + i))
			{ replaceableItems[i] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("item:" + i)); }
		}
	}

    @Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
	{

        nbtTagCompound.setBoolean("hasLid", this.hasLid);

        if (playerProfile != null) {
            NBTTagCompound profileTag = new NBTTagCompound();
            NBTUtil.writeGameProfile(profileTag, playerProfile);
            nbtTagCompound.setTag("playerProfile", profileTag);
        }

		NBTTagCompound replaceableBlocksTag = new NBTTagCompound();
		replaceableBlocksTag.setInteger("size", replaceableBlocks.size());
		for (int i = 0; i < replaceableBlocks.size(); i++)
		{
			replaceableBlocksTag.setTag("block:" + i, replaceableBlocks.get(i).writeNBT());
		}
		nbtTagCompound.setTag("replaceableBlocks", replaceableBlocksTag);
		NBTTagCompound replaceableItemsTag = new NBTTagCompound();
		replaceableItemsTag.setInteger("size", replaceableItems.length);
		for(int i = 0; i < replaceableItems.length; i++)
		{
			if(replaceableItems[i] != null)
			{ replaceableItemsTag.setTag("item:" + i, replaceableItems[i].writeToNBT(new NBTTagCompound())); }
		}
		nbtTagCompound.setTag("replaceableItems", replaceableItemsTag);
		return super.writeToNBT(nbtTagCompound);
	}

    public void breakBlocks() {
        // Adding slaves
		IBlockState defSlaveState = Blocks.BLOCK_GRAVESTONE_SLAVE.block.getDefaultState();
		IBlockState state = worldObj.getBlockState(pos);
		EnumFacing facing = state.getBlock().getActualState(state, worldObj, pos).getValue(BlockGraveStone.FACING);
		TileEntityGraveSlave tileEntityGraveSlave;

		for(BlockPos slavePos : getSlaves(pos, facing))
		{
			worldObj.removeTileEntity(slavePos);
			worldObj.setBlockState(slavePos, BlockGraveSlave.getActualStatePre(defSlaveState, worldObj, slavePos, pos));

			tileEntityGraveSlave = TileTools.getTileEntity(worldObj, slavePos, TileEntityGraveSlave.class);
			tileEntityGraveSlave.setMasterBlock(pos);
		}
		// End of adding slaves

    }

	@Override
    public IInventory getInternalInventory() {
        return this.internalInventory;
    }

    @Override
    public void saveChanges() {

    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {
        return new int[0];
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return null;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
	public ITextComponent getDisplayName()
	{
		return null;
	}

	public void breakLid(EntityPlayer player)
	{
		IBlockState state = getBlockState();
		if(state.getValue(BlockGraveStone.HASLID))
		{
			hasLid = false;
			worldObj.notifyBlockUpdate(pos, state, state.withProperty(BlockGraveStone.HASLID, false), 3);
			updateSlaves();
			markForUpdate();
			markDirty();

			spawnPlayerZombie(player);
		}
	}

	private void updateSlaves()
	{
		for(BlockPos sPos : getSlaves(pos, getFacing()))
		{
			IBlockState state = worldObj.getBlockState(sPos);
			worldObj.markAndNotifyBlock(sPos, worldObj.getChunkFromBlockCoords(pos), state, state.getActualState(worldObj, sPos), 3);
		}
	}

	private void spawnPlayerZombie(EntityPlayer player)
	{
		boolean spawnPlayerZombie = false;

		//todo: make if player has items, make the chance less
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

                /* Notes :

                    Artifacts:
                    > 4x Artifacts, each one lowers the zombie spawning chance

                 */

		if(spawnChance > 0)
		{
			Random random = new Random();
			int rng = random.nextInt(100);

			if(rng <= spawnChance)
			{ spawnPlayerZombie = true; }
		}

		if(spawnPlayerZombie && ConfigZombie.configZombieEnabled)
		{
			EntityPlayerZombie playerZombie = new EntityPlayerZombie(worldObj, pos);

			playerZombie.setProfile(getPlayerProfile());

			playerZombie.setLocationAndAngles(pos.getX(), pos.down().getY(), pos.getZ(), getBlockState().getValue(BlockGraveStone.FACING).getHorizontalIndex() * 90f, 0f);
			playerZombie.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(playerZombie)), null);
			playerZombie.setPlayer(player);
			//                    nbtTagCompound.setIntArray("MasterGrave", new int[]{graveStone.getPos().getX(), graveStone.getPos().getY(), graveStone.getPos().getZ()});

			worldObj.spawnEntityInWorld(playerZombie);
			//                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundHelper.getRegisteredSoundEvent("graves:graveZombieSpawn"), SoundCategory.HOSTILE, 1, 1, true);
		}

	}

	public EnumFacing getFacing()
	{
		return getBlockState().getValue(BlockGraveStone.FACING);
	}

	public List<ReplaceableBlock> getReplaceableBlocks()
	{
		return replaceableBlocks;
	}

	public void setReplaceableBlocks(List<ReplaceableBlock> replaceableBlocks)
	{
		this.replaceableBlocks = replaceableBlocks;
	}

	public void replaceItems(InventoryPlayer inventory)
	{
		System.arraycopy(replaceableItems, 0, inventory.mainInventory, 0, InventoryPlayer.getHotbarSize());
		System.arraycopy(replaceableItems, InventoryPlayer.getHotbarSize(), inventory.armorInventory, 0, inventory.armorInventory.length);
		System.arraycopy(replaceableItems, InventoryPlayer.getHotbarSize() + inventory.armorInventory.length, inventory.offHandInventory, 0, inventory.offHandInventory.length);
		inventory.markDirty();
	}
}
