package com.fireball1725.graves.tileentity;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.tileentity.inventory.InternalInventory;
import com.fireball1725.graves.tileentity.inventory.InventoryOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.Random;

public class TileEntityGraveStone extends TileEntityInventoryBase {
    protected boolean hasLid = true;
    private InternalInventory internalInventory = new InternalInventory(this, 100);
    private String playerName = "";

    public void setGraveItems(List<EntityItem> itemsList, EntityPlayer player) {
        this.playerName = player.getDisplayName().getFormattedText();

        int i = 0;
        for (EntityItem item : itemsList) {
            internalInventory.setInventorySlotContents(i, item.getEntityItem());
            i++;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        this.hasLid = nbtTagCompound.getBoolean("hasLid");
        this.playerName = nbtTagCompound.getString("playerName");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setBoolean("hasLid", this.hasLid);
        nbtTagCompound.setString("playerName", this.playerName);
    }

    public void breakBlocks() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        Block block;

        block = worldObj.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
        if (block != null) {
            Item block1 = block.getItemDropped(worldObj.getBlockState(new BlockPos(x, y - 1, z)), new Random(1), 0);
            worldObj.setBlockToAir(new BlockPos(x, y - 1, z));
            internalInventory.setInventorySlotContents(80, new ItemStack(block1));
        }
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
    public IChatComponent getDisplayName() {
        return null;
    }

    public boolean getHasLid() {
        return hasLid;
    }

    public void setHasLid(boolean hasLid) {
        this.hasLid = hasLid;
        worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockGraveStone.HASLID, false));
        worldObj.markBlockForUpdate(pos);
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
