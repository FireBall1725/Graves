package com.fireball1725.graves.common.block;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;

public enum Blocks {
	BLOCK_GRAVE("grave", new BlockGrave(), CreativeTabs.tabDecorations),
	Block_GRAVE_LEGACY("gravestone", new BlockGraveStone());

    private static boolean registered = false;
    public final Block block;
    private final String internalName;
    private final Class<? extends ItemBlock> itemBlockClass;
    private final CreativeTabs creativeTabs;

    Blocks(String internalName, Block block) {
        this(internalName, block, ItemBlock.class, null);
    }

    Blocks(String internalName, Block block, CreativeTabs creativeTabs) {
        this(internalName, block, ItemBlock.class, creativeTabs);
    }

    Blocks(String internalName, Block block, Class<? extends ItemBlock> itemBlockClass) {
        this(internalName, block, itemBlockClass, null);
    }

    Blocks(String internalName, Block block, Class<? extends ItemBlock> itemBlockClass, CreativeTabs creativeTabs) {
        this.internalName = internalName;
        this.block = block;
        this.itemBlockClass = itemBlockClass;
        this.creativeTabs = creativeTabs;
    }

    public static void registerAll() {
        if (registered)
            return;
        for (Blocks b : Blocks.values())
            b.register();
        registered = true;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getStatName() {
		return I18n.translateToLocal(block.getUnlocalizedName().replace("tileentity.", "block."));
	}

    private void register() {
        GameRegistry.registerBlock(block.setCreativeTab(creativeTabs).setUnlocalizedName(internalName), itemBlockClass, internalName);
    }
}
