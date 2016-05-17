package com.fireball1725.graves.common.tileentity;

import com.fireball1725.graves.common.block.BlockGraveSlave;
import com.fireball1725.graves.common.block.BlockGraveStone;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.configuration.ConfigZombie;
import com.fireball1725.graves.common.entity.EntityPlayerZombie;
import com.fireball1725.graves.common.helpers.LogHelper;
import com.fireball1725.graves.common.structure.ReplaceableBlock;
import com.fireball1725.graves.common.tileentity.inventory.InternalDynamicInventory;
import com.fireball1725.graves.common.tileentity.inventory.InventoryOperation;
import com.fireball1725.graves.common.util.TileTools;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.EnumDifficulty;

import java.util.List;
import java.util.Random;

public class TileEntityGraveStone extends TileEntityInventoryBase
{
	private boolean hasLid = true;
    private InternalDynamicInventory internalInventory = new InternalDynamicInventory(this);
    private GameProfile playerProfile;
	private List<ReplaceableBlock> replaceableBlocks = Lists.newArrayList();

    @Override
    public Packet getDescriptionPacket() {
        LogHelper.info(String.format("Gravestone (%s) at W=%s X=%s Y=%s Z=%s", this.playerProfile == null ? "null" : this.playerProfile.getName(), this.worldObj.getWorldInfo().getWorldName(), this.pos.getX(), this.pos.getY(), this.pos.getZ()));

        return super.getDescriptionPacket();
    }

    public void addGraveItems(List<EntityItem> itemsList) {
        for (EntityItem item : itemsList) {
            internalInventory.addInventorySlotContents(item.getEntityItem());
        }
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

		NBTTagCompound replaceableTag = nbtTagCompound.getCompoundTag("replaceableTag");
		int size = replaceableTag.getInteger("size");
		replaceableBlocks = Lists.newArrayList();
		for (int i = 0; i < size; i++)
		{
			replaceableBlocks.add(ReplaceableBlock.readNBT((NBTTagCompound) replaceableTag.getTag("block:" + i)));
		}
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setBoolean("hasLid", this.hasLid);

        if (playerProfile != null) {
            NBTTagCompound profileTag = new NBTTagCompound();
            NBTUtil.writeGameProfile(profileTag, playerProfile);
            nbtTagCompound.setTag("playerProfile", profileTag);
        }

		NBTTagCompound replaceableTag = new NBTTagCompound();
		replaceableTag.setInteger("size", replaceableBlocks.size());
		for (int i = 0; i < replaceableBlocks.size(); i++)
		{
			replaceableTag.setTag("block:" + i, replaceableBlocks.get(i).writeNBT());
		}
		nbtTagCompound.setTag("replaceableTag", replaceableTag);
    }

    public void breakBlocks() {
        // Adding slaves
        EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockGraveStone.FACING);
        IBlockState state;
        TileEntityGraveSlave tileEntityGraveSlave;
        state = Blocks.BLOCK_GRAVESTONE_SLAVE.block.getDefaultState();

		worldObj.removeTileEntity(pos.down());
		worldObj.setBlockState(pos.down(), BlockGraveSlave.getActualStatePre(state, worldObj, pos.down(), pos));

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down(), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

		worldObj.removeTileEntity(pos.down().offset(facing));
		worldObj.setBlockState(pos.down().offset(facing), BlockGraveSlave.getActualStatePre(state, worldObj, pos.down().offset(facing), pos));

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down().offset(facing), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

		worldObj.removeTileEntity(pos.offset(facing));
		worldObj.setBlockState(pos.offset(facing), BlockGraveSlave.getActualStatePre(state, worldObj, pos.offset(facing), pos));

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.offset(facing), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);
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
			LogHelper.info("breaking lid");
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

	public List<ReplaceableBlock> getReplaceableBlocks()
	{
		return replaceableBlocks;
	}

	public void setReplaceableBlocks(List<ReplaceableBlock> replaceableBlocks)
	{
		this.replaceableBlocks = replaceableBlocks;
	}
}
