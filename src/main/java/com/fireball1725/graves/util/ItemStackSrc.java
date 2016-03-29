package com.fireball1725.graves.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackSrc {
	private final Item item;
	private final Block block;
	private final int damage;

    public ItemStackSrc(Item i, int dmg) {
        this.block = null;
        this.item = i;
        this.damage = dmg;
    }

    public ItemStackSrc(Block b, int dmg) {
        this.block = b;
        this.item = null;
        this.damage = dmg;
    }

    public ItemStack stack(int i) {
        if (this.block != null) {
            return new ItemStack(this.block, i, this.damage);
        }
        return new ItemStack(this.item, i, this.damage);
    }
}
