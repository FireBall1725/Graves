package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGraveStone extends BlockBase {

    public BlockGraveStone() {
        super(Material.cloth);
        this.setHardness(0.5F);
        this.setResistance(1.0F);
        this.setTileEntity(TileEntityGraveStone.class);
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
        //super.breakBlock(world, blockPos, blockState);

    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }
}
