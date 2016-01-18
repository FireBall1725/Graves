package com.fireball1725.graves.tileentity;

import com.fireball1725.graves.tileentity.inventory.InternalInventory;
import com.fireball1725.graves.tileentity.inventory.InventoryOperation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;

import java.util.List;
import java.util.UUID;

public class TileEntityGraveStone extends TileEntityInventoryBase {
    private InternalInventory internalInventory = new InternalInventory(this, 100);
    private UUID playerUUID = null;

    public void setGraveItems(List<EntityItem> itemsList, EntityPlayer player) {
        int i = 0;
        for (EntityItem item : itemsList) {
            internalInventory.setInventorySlotContents(i, item.getEntityItem());
            i++;
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
}
