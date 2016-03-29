package com.fireball1725.graves.block;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.tileentity.TileEntityHeadStone;
import com.fireball1725.graves.util.TileTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockHeadStone extends BlockBase {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected BlockHeadStone() {
        super(Material.rock);
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setTileEntity(TileEntityHeadStone.class);
    }

    @Override
	public int getRenderType()
	{
		return 3;
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		return willHarvest || super.removedByPlayer(world, pos, player, false);
	}

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityHeadStone headStone = TileTools.getTileEntity(world, pos, TileEntityHeadStone.class);
		if(headStone != null && !headStone.getCustomName().isEmpty())
		{
			final ItemStack itemStack = new ItemStack(this);
			itemStack.setStackDisplayName(headStone.getCustomName());

            ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
            drops.add(itemStack);

            return drops;
        }
        return super.getDrops(world, pos, state, fortune);
    }

	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te)
	{
		super.harvestBlock(worldIn, player, pos, state, te);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(playerIn.capabilities.isCreativeMode)
		{
			playerIn.openGui(Graves.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
	}

    @Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, FACING);
	}

    @Override
	public boolean isOpaqueCube()
	{
		return false;
	}

    @Override
	public boolean isFullBlock()
	{
		return false;
	}

    @Override
	public boolean isFullCube()
	{
		return false;
	}

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
	{
		IBlockState state = worldIn.getBlockState(pos);
		switch(state.getValue(FACING))
		{
			case NORTH:
				setBlockBounds(.1f, 0f, 0f, .9f, .95f, .3f);
				break;
			case SOUTH:
				setBlockBounds(.1f, 0f, .7f, .9f, .95f, 1f);
				break;
			case WEST:
				setBlockBounds(0f, 0f, .1f, .3f, .95f, .9f);
				break;
			case EAST:
				setBlockBounds(.7f, 0f, .1f, 1f, .95f, .9f);
				break;
		}
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		setBlockBoundsBasedOnState(worldIn, pos);
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex();
	}


    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {

    }
}
