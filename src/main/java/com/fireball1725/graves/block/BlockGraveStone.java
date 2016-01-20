package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
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
        this.setHardness(0.5F);
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

//    @Override
//    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
//        if (worldIn.isRemote) {
//            return true;
//        }
//        if (getActualState(state, worldIn, pos).getValue(HASLID)) {
//            TileEntityGraveStone tileEntityGraveStone = TileTools.getTileEntity(worldIn, pos, TileEntityGraveStone.class);
//            if (tileEntityGraveStone != null) {
//                worldIn.setBlockState(pos, state.withProperty(BlockGraveStone.HASLID, false));
//                worldIn.markBlockForUpdate(pos);
//                LogHelper.info("activated and changed 21:32:11");
//            }
//        }
//        return false;
//    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, HASLID, FACING);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntityGraveStone graveStone = TileTools.getTileEntity(worldIn, pos, TileEntityGraveStone.class);
        if (graveStone != null) {
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
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        super.setBlockBoundsBasedOnState(worldIn, pos);
        IBlockState actualState = getActualState(worldIn.getBlockState(pos), worldIn, pos);
        if (actualState.getValue(HASLID)) {
            //			setBlockBounds(pos.getX(), pos.getY() - 1f, pos.getZ(), 2f, 1.25f, 1);
        }
    }

    @Override
    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        //		list.clear();
        IBlockState actualState = getActualState(worldIn.getBlockState(pos), worldIn, pos);
        if (actualState.getValue(HASLID)) {
            //			list.add()
            //			setBlockBounds(pos.getX(), pos.getY() - 1f, pos.getZ(), pos.getX() + 2f, pos.getY() + .25f, pos.getZ() + 1);
        }
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }


}
