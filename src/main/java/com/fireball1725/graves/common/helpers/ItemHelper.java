package com.fireball1725.graves.common.helpers;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

public class ItemHelper
{
	public static boolean doesItemHaveEnchant(String enchantment, ItemStack stack)
	{
		return doesItemHaveEnchant(Enchantment.getEnchantmentByLocation(enchantment), stack);
	}

	public static boolean doesItemHaveEnchant(Enchantment enchantment, ItemStack stack)
	{
		return EnchantmentHelper.getEnchantmentLevel(enchantment, stack) > 0;
	}
}
