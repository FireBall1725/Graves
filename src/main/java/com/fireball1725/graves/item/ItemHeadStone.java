package com.fireball1725.graves.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class ItemHeadStone extends ItemBlock {
    public ItemHeadStone(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (!stack.hasDisplayName()) {
            tooltip.add(EnumChatFormatting.GREEN + "Rename in anvil to name Head Stone");
        }
    }
}
