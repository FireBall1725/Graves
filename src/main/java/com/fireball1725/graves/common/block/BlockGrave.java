package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.entity.capabilities.GraveCapability;
import com.fireball1725.graves.common.entity.capabilities.IGraveCapability;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.fireball1725.graves.common.util.TileTools;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
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

public class BlockGrave extends BlockBase
{
	public static final PropertyBool RENDER = PropertyBool.create("render");

	protected BlockGrave()
	{
		super(Material.rock);
		this.setDefaultState(blockState.getBaseState().withProperty(RENDER, true));
		this.setHardness(1.5F);
		this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setTileEntity(TileEntityGrave.class);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack)
	{
		super.onBlockPlacedBy(world, blockPos, state, placer, itemStack);
		if(placer instanceof EntityPlayer)
		{
			TileEntityGrave grave = TileTools.getTileEntity(world, blockPos, TileEntityGrave.class);
			if(grave != null)
			{
				grave.setProfile(((EntityPlayer) placer).getGameProfile());
				grave.setGhostDefeated(true);
				grave.markDirty();
			}
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
	{
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntityGrave headStone = TileTools.getTileEntity(world, pos, TileEntityGrave.class);
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
		if(worldIn.isRemote)
		{ return true; }

		if(heldItem == null || (heldItem.getItem() instanceof ItemBlock && ForgeRegistries.BLOCKS.getKey(((ItemBlock) heldItem.getItem()).block).getResourceDomain().equals("chiselsandbits")))
		{
			final IGraveCapability grave = playerIn.getCapability(GraveCapability.GRAVE_CAPABILITY, null);
			if(grave != null)
			{
				grave.setGraveItemStack(heldItem);
				TileEntity te = worldIn.getTileEntity(pos);
				if(te instanceof TileEntityGrave)
				{
					((TileEntityGrave) te).setDisplayStack(heldItem);
					te.markDirty();
					worldIn.notifyBlockUpdate(pos, state, ((TileEntityGrave) te).getBlockState(), 3);
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
		return new BlockStateContainer(this, RENDER);
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
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		if(te instanceof TileEntityGrave)
		{
			TileEntityGrave headStone = (TileEntityGrave) te;
			return state.withProperty(RENDER, headStone.getDisplayStack() == null);
		}
		return state;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_)
	{
		addCollisionBoxToList(pos, mask, list, getBoundingBox(state, worldIn, pos));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return new AxisAlignedBB(0.0625f, 0f, 0.062f, 0.9375f, 1f, 0.9375f);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState();
	}

	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion)
	{
	}

	@Override
	public int getLightValue(IBlockState state)
	{
		return 15;
	}
}
