package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityGraveStone;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockGraveLegacy extends BlockContainer
{
	public BlockGraveLegacy()
	{
		super(Material.ROCK);

		String tileName = "tileentity." + ModInfo.MOD_ID + "." + TileEntityGraveStone.class.getSimpleName();
		GameRegistry.registerTileEntity(TileEntityGraveStone.class, tileName);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileEntityGraveStone();
	}
}
