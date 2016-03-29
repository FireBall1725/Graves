package com.fireball1725.graves.tileentity;

import com.fireball1725.graves.block.BlockGraveSlave;
import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.configuration.ConfigZombie;
import com.fireball1725.graves.entity.EntityPlayerZombie;
import com.fireball1725.graves.helpers.LogHelper;
import com.fireball1725.graves.tileentity.inventory.InternalDynamicInventory;
import com.fireball1725.graves.tileentity.inventory.InventoryOperation;
import com.fireball1725.graves.util.TileTools;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumDifficulty;

import java.util.List;
import java.util.Random;

public class TileEntityGraveStone extends TileEntityInventoryBase
{
	private boolean hasLid = true;
	private InternalDynamicInventory internalInventory = new InternalDynamicInventory(this);
    private GameProfile playerProfile;

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
		worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), getBlockState(), getBlockState().withProperty(BlockGraveStone.HASLID, hasLid), 3);
	}

    public GameProfile getPlayerProfile() {
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
    }

    public void breakBlocks() {
		IBlockState masterState = getBlockState();
		Block block1, block2;
        block1 = worldObj.getBlockState(pos.down()).getBlock();
        block2 = worldObj.getBlockState(pos.down().offset(masterState.getValue(BlockGraveStone.FACING))).getBlock();

        if (block1 != null && block2 != null) {
            IBlockState state1, state2;
            state1 = worldObj.getBlockState(pos.down());
            state2 = worldObj.getBlockState(pos.down().offset(masterState.getValue(BlockGraveStone.FACING)));

            ItemStack item1 = new ItemStack(block1.getItemDropped(state1, new Random(1), 0), 1, block1.damageDropped(state1));
            ItemStack item2 = new ItemStack(block2.getItemDropped(state2, new Random(1), 0), 1, block2.damageDropped(state2));
            worldObj.setBlockToAir(pos.down());
            worldObj.setBlockToAir(pos.down().offset(masterState.getValue(BlockGraveStone.FACING)));
			internalInventory.addInventorySlotContents(item1);
			internalInventory.addInventorySlotContents(item2);
		}

        // Adding slaves
        EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockGraveStone.FACING);
        IBlockState state;
        TileEntityGraveSlave tileEntityGraveSlave;
        state = Blocks.BLOCK_GRAVESTONE_SLAVE.block.getDefaultState();

		worldObj.setBlockState(pos.down(), BlockGraveSlave.getActualStatePre(state, worldObj, pos.down(), pos));

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down(), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

		worldObj.setBlockState(pos.down().offset(facing), BlockGraveSlave.getActualStatePre(state, worldObj, pos.down().offset(facing), pos));

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down().offset(facing), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

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
	public IChatComponent getDisplayName()
	{
		return null;
	}

	public void breakLid(EntityPlayer player)
	{
		IBlockState state = getBlockState();
		if(state.getValue(BlockGraveStone.HASLID))
		{
			LogHelper.info("breaking lid");
			setHasLid(false);
			updateSlaves();
			markForUpdate();
			markDirty();

			spawnPlayerZombie(player);
		}
	}

	private void updateSlaves()
	{
		for(BlockPos sPos : getSlaves())
		{
			IBlockState state = worldObj.getBlockState(sPos);
			worldObj.markAndNotifyBlock(sPos, worldObj.getChunkFromBlockCoords(pos), state, state.getBlock().getActualState(state, worldObj, sPos), 3);
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
			EntityPlayerZombie playerZombie = new EntityPlayerZombie(worldObj);

			playerZombie.setProfile(getPlayerProfile());

			playerZombie.setLocationAndAngles(pos.getX(), pos.down().getY(), pos.getZ(), getBlockState().getValue(BlockGraveStone.FACING).getHorizontalIndex() * 90f, 0f);
			playerZombie.onInitialSpawn(worldObj.getDifficultyForLocation(new BlockPos(playerZombie)), null);
			playerZombie.setPlayer(player);
			playerZombie.setGraveMaster(pos);
			//                    nbtTagCompound.setIntArray("MasterGrave", new int[]{graveStone.getPos().getX(), graveStone.getPos().getY(), graveStone.getPos().getZ()});

			worldObj.spawnEntityInWorld(playerZombie);
			//                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundHelper.getRegisteredSoundEvent("graves:graveZombieSpawn"), SoundCategory.HOSTILE, 1, 1, true);
		}

	}

	public EnumFacing getFacing()
	{
		return getBlockState().getValue(BlockGraveStone.FACING);
	}

	private BlockPos[] getSlaves()
	{
		EnumFacing facing = getFacing();
		return new BlockPos[] {pos.down(), pos.offset(facing).down(), pos.offset(facing)};
	}
}
