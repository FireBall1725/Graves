package com.fireball1725.graves.common.helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class ItemHelper
{
	public static boolean doesItemHaveEnchant(ItemStack stack, String enchant)
	{
		if(stack != null && stack.getTagCompound() != null && stack.getTagCompound().hasKey("ench"))
		{
			NBTTagList tagList = stack.getTagCompound().getTagList("ench", 10);
			for(int i = 0; i < tagList.tagCount(); i++)
			{
				NBTTagCompound tag = tagList.getCompoundTagAt(i);
				if(tag.getString("id").equals(enchant))
				{
					return true;
				}
			}
		}

		return false;
	}

	public static boolean doesItemHaveEnchant(ItemStack stack, Enchantment enchantment)
	{
		return doesItemHaveEnchant(stack, enchantment.getName());
	}
}
