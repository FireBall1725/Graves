package com.fireball1725.graves.common.item;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemHeadStone extends ItemBlock {
    public ItemHeadStone(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!stack.hasDisplayName()) {
			tooltip.add(ChatFormatting.GREEN + "Rename in anvil to name Head Stone");
		}
	}
}
