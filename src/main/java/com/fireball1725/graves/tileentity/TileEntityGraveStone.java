package com.fireball1725.graves.tileentity;

import com.fireball1725.graves.block.BlockGraveStone;
import com.fireball1725.graves.block.Blocks;
import com.fireball1725.graves.tileentity.inventory.InternalInventory;
import com.fireball1725.graves.tileentity.inventory.InventoryOperation;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.Random;

public class TileEntityGraveStone extends TileEntityInventoryBase {
    protected boolean hasLid = true;
    private InternalInventory internalInventory = new InternalInventory(this, 100);

    public void setGraveItems(List<EntityItem> itemsList, EntityPlayer player) {

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
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        nbtTagCompound.setBoolean("hasLid", this.hasLid);
    }

    public void breakBlocks() {
        IBlockState masterState = worldObj.getBlockState(pos);
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
            internalInventory.setInventorySlotContents(80, item1);
            internalInventory.setInventorySlotContents(81, item2);
        }

        // Adding slaves
        EnumFacing facing = worldObj.getBlockState(pos).getValue(BlockGraveStone.FACING);
        IBlockState state;
        TileEntityGraveSlave tileEntityGraveSlave;
        state = Blocks.BLOCK_GRAVESTONE_SLAVE.block.getDefaultState();

        worldObj.setBlockState(pos.down(), state);

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down(), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

        worldObj.setBlockState(pos.offset(facing), state);

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.offset(facing), TileEntityGraveSlave.class);
        tileEntityGraveSlave.setMasterBlock(pos);

        worldObj.setBlockState(pos.down().offset(facing), state);

        tileEntityGraveSlave = TileTools.getTileEntity(worldObj, pos.down().offset(facing), TileEntityGraveSlave.class);
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
}
