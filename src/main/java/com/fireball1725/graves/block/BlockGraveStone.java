package com.fireball1725.graves.block;

import com.fireball1725.graves.tileentity.TileEntityGraveStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockGraveStone extends BlockBase {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool HASLID = PropertyBool.create("haslid");

    public BlockGraveStone() {
        super(Material.cloth);
		setDefaultState(this.blockState.getBaseState().withProperty(HASLID, true).withProperty(FACING, EnumFacing.NORTH));
		setHardness(1F);
		this.setResistance(10000F);
        this.setTileEntity(TileEntityGraveStone.class);
    }

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return false;
	}

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, HASLID, FACING);
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
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_)
	{
		IBlockState actualState = getActualState(state, worldIn, pos);
		boolean hasLid = actualState.getValue(BlockGraveStone.HASLID);
		if (hasLid) {
			addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, 1, .1425f, 1));
		}
		else
		{
			addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0, 0, 0, 0, 0, 0));
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0, 0, 0, 1, .1425f, 1);
	}

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, state, placer, itemStack);

        TileEntityGraveStone graveStoneTileEntity = TileTools.getTileEntity(world, blockPos, TileEntityGraveStone.class);
        graveStoneTileEntity.breakBlocks();
        graveStoneTileEntity.setPlayerProfile(((EntityPlayer) placer).getGameProfile());
    }

    @Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

    @Override
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}

    @Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }
}
