package com.fireball1725.graves.common.creativetab;

import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.reference.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModCreativeTabs {
    public static final CreativeTabs tabGraves = new CreativeTabs(ModInfo.MOD_ID) {
        @Override
        public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.BLOCK_GRAVE_HEADSTONE.block);
		}
	};
}
