package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveSlave;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;
import java.util.Random;

public class BlockGraveSlave extends BlockBase {
    public BlockGraveSlave() {
        super(Material.cloth);
        setStepSound(Block.soundTypeStone);
        setHardness(1.0F);
        setResistance(10000.0F);
        setTileEntity(TileEntityGraveSlave.class);
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        AxisAlignedBB selectionBox;
        if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone) {
            selectionBox = AxisAlignedBB.fromBounds(0f, 0f, 0f, 1f, 1f, 1f);
        } else {
            selectionBox = AxisAlignedBB.fromBounds(0, 0, 0, 1, .1425f, 1);
        }
        return selectionBox.offset(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        TileEntityGraveSlave graveSlave = TileTools.getTileEntity(worldIn, pos, TileEntityGraveSlave.class);
        if (graveSlave != null) {
            IBlockState masterState = worldIn.getBlockState(graveSlave.getMasterBlock());
            IBlockState actualState = masterState.getBlock().getActualState(masterState, worldIn, graveSlave.getMasterBlock());
            //			LogHelper.info(">>> " + actualState);
            if (masterState.getBlock() instanceof BlockGraveStone) {
                boolean hasLid = actualState.getValue(BlockGraveStone.HASLID);
                if (worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveSlave || worldIn.getBlockState(pos.up()).getBlock() instanceof BlockGraveStone) {
                    setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f);
                } else {
                    if (hasLid) {
                        setBlockBounds(0, 0, 0, 1, .1425f, 1);
                        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
                    } else {
                        setBlockBounds(0, 0, 0, 0, 0, 0);
                    }
                    super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
                }
            }
        }
    }

    @Override
    public boolean addLandingEffects(WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return super.addLandingEffects(worldObj, blockPosition, iblockstate, entity, numberOfParticles);
    }

    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return super.addHitEffects(worldObj, target, effectRenderer);
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        return super.addDestroyEffects(world, pos, effectRenderer);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullBlock() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }
}
