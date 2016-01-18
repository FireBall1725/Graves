package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import net.minecraft.block.material.Material;

public class BlockGraveStone extends BlockBase {

    public BlockGraveStone() {
        super(Material.cloth);
        this.setHardness(0.5F);
        this.setResistance(1.0F);
        this.setTileEntity(TileEntityGraveStone.class);
    }
}
