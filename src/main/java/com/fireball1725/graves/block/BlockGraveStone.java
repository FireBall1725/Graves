package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockGraveStone extends BlockBase {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool HASLID = PropertyBool.create("hasLid");

    public BlockGraveStone() {
        super(Material.cloth);
        setDefaultState(blockState.getBaseState().withProperty(HASLID, true).withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(1.0F);
        this.setResistance(10000.0F);
        this.setTileEntity(TileEntityGraveStone.class);
    }

    @Override
    public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity entity) {
        return false;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

//    @Override
//    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
//        TileEntityGraveStone tileEntityGraveStone = TileTools.getTileEntity(world, blockPos, TileEntityGraveStone.class);
//        if (tileEntityGraveStone != null) {
//            LogHelper.info(">>> S " + tileEntityGraveStone.getHasLid());
//            if (tileEntityGraveStone.getHasLid()) {
//                tileEntityGraveStone.setHasLid(false);
//                world.setBlockState(blockPos, blockState.withProperty(HASLID, false));
//                tileEntityGraveStone.markForUpdate();
//                LogHelper.info(">>> E " + tileEntityGraveStone.getHasLid());
//            } else {
//                super.breakBlock(world, blockPos, blockState);
//            }
//        }
//    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HASLID, FACING);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityGraveStone) {
            TileEntityGraveStone graveStone = (TileEntityGraveStone) tileEntity;
            return state.withProperty(HASLID, graveStone.getHasLid());
        }
        return state.withProperty(HASLID, false);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }

    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        IBlockState actualState = getActualState(state, worldIn, pos);
        boolean hasLid = actualState.getValue(BlockGraveStone.HASLID);
        if (hasLid) {
            setBlockBounds(0, 0, 0, 1, .1425f, 1);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        } else {
            setBlockBounds(0, 0, 0, 0, 0, 0);
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        return AxisAlignedBB.fromBounds(0, 0, 0, 1, .1425f, 1).offset(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }
}
