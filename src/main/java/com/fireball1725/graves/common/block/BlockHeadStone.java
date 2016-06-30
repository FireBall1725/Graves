package com.fireball1725.graves.common.block;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.chiselsandbits.GraveCapability;
import com.fireball1725.graves.common.tileentity.TileEntityHeadStone;
import com.fireball1725.graves.common.util.TileTools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class BlockHeadStone extends BlockBase {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool RENDER = PropertyBool.create("render");

    protected BlockHeadStone() {
        super(Material.rock);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(RENDER, true));
		this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.setTileEntity(TileEntityHeadStone.class);
    }

    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
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
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		super.harvestBlock(worldIn, player, pos, state, te, stack);
		worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if(playerIn.capabilities.isCreativeMode)
		{
			playerIn.openGui(Graves.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		if(worldIn.isRemote)
		{ return true; }

		if(heldItem == null || (heldItem.getItem() instanceof ItemBlock && ForgeRegistries.BLOCKS.getKey(Block.getBlockFromItem(heldItem.getItem())).getResourceDomain().equals("chiselsandbits")))
		{
			final GraveCapability.IGraveCapability grave = playerIn.getCapability(GraveCapability.GRAVE_CAP, null);
			if(grave != null)
			{
				grave.setGraveItemStack(heldItem);
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof TileEntityHeadStone)
				{
					((TileEntityHeadStone) te).setDisplayStack(heldItem);
					te.markDirty();
					worldIn.notifyBlockUpdate(pos, state, ((TileEntityHeadStone) te).getBlockState(), 3);
					worldIn.notifyBlockOfStateChange(pos, state.getBlock());
					worldIn.markChunkDirty(pos, te);
				}
				return true;
			}
		}
		return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
	}

    @Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, RENDER);
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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityHeadStone)
		{
			TileEntityHeadStone headStone = (TileEntityHeadStone) te;
			return state.withProperty(RENDER, headStone.getDisplayStack() == null);
		}
		return state;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_)
	{
		IBlockState astate = worldIn.getBlockState(pos);
		switch(astate.getValue(FACING))
		{
			case NORTH:
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(.1f, 0f, 0f, .9f, .95f, .3f));
				break;
			case SOUTH:
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(.1f, 0f, .7f, .9f, .95f, 1f));
				break;
			case WEST:
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(0f, 0f, .1f, .3f, .95f, .9f));
				break;
			case EAST:
				addCollisionBoxToList(pos, mask, list, new AxisAlignedBB(.7f, 0f, .1f, 1f, .95f, .9f));
				break;
		}
    }

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		IBlockState astate = source.getBlockState(pos);
		if(astate.getBlock() instanceof BlockHeadStone)
		{
			switch(astate.getValue(FACING))
			{
				case NORTH:
					return new AxisAlignedBB(.1f, 0f, 0f, .9f, .95f, .3f);
				case SOUTH:
					return new AxisAlignedBB(.1f, 0f, .7f, .9f, .95f, 1f);
				case WEST:
					return new AxisAlignedBB(0f, 0f, .1f, .3f, .95f, .9f);
				case EAST:
					return new AxisAlignedBB(.7f, 0f, .1f, 1f, .95f, .9f);
			}
		}
		return null;
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
