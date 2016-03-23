package com.fireball1725.graves.util.iterators;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

public class InventoryIterator implements Iterator<ItemStack> {
	private final IInventory inventory;
	private final int size;

	private int i = -1;

    public InventoryIterator(IInventory inventory) {
        this.inventory = inventory;
        this.size = this.inventory.getSizeInventory();
    }

    @Override
    public boolean hasNext() {
		return this.i + 1 < this.size;
	}

    @Override
    public ItemStack next() {
		return inventory.getStackInSlot(this.i++);
	}

    @Override
    public void remove() {
        throw new RuntimeException("I'm sorry Dave, I can't let you do that...");
        // haha...
    }

}
