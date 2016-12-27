package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.integration.ChiselsAndBits;
import com.fireball1725.graves.common.integration.IsLoaded;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.fireball1725.graves.common.util.Headstones;
import com.fireball1725.graves.common.util.TileTools;
import com.mojang.authlib.GameProfile;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class BlockGrave extends BlockBase
{
    public static final AxisAlignedBB DEFAULTBB = new AxisAlignedBB(0.0625f, 0f, 0.062f, 0.9375f, 1f, 0.9375f);
    public static final int DEFAULTLL = 15;
    public static final PropertyBool RENDER = PropertyBool.create("render");
    public static final PropertyBool WORLDGEN = PropertyBool.create("worldgen");

	protected BlockGrave()
	{
		super(Material.ROCK);
        this.setDefaultState(blockState.getBaseState().withProperty(RENDER, true).withProperty(WORLDGEN, false));
        this.setHardness(1.5F);
        this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 0);
		this.setTileEntity(TileEntityGrave.class);
	}

    public IBlockState getWorldGenState() {
        return getDefaultState().withProperty(WORLDGEN, true);
    }

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        if (world.isRemote) return;
        if(placer instanceof EntityPlayer)
		{
            EntityPlayer player = (EntityPlayer) placer;
            TileEntityGrave grave = TileTools.getTileEntity(world, blockPos, TileEntityGrave.class);
			if(grave != null)
			{
                if (grave.getDisplayStack().getItem() != Item.getItemFromBlock(net.minecraft.init.Blocks.AIR))
                    state = state.withProperty(RENDER, false);

                if (itemStack != ItemStack.EMPTY && itemStack.hasDisplayName()) {
                    GameProfile profile = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().getGameProfileForUsername(itemStack.getDisplayName());
                    if (profile != null) {
                        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerProfileCache().addEntry(profile);
                        grave.setProfile(profile);
                    }
                } else {
                    grave.setProfile(((EntityPlayer) placer).getGameProfile());
                }
                grave.setGhostDefeated(true);
                world.markChunkDirty(blockPos, grave);
                world.markBlockRangeForRenderUpdate(blockPos, blockPos);
                world.checkLight(blockPos);
            }
		}
        super.onBlockPlacedBy(world, blockPos, state, placer, itemStack);
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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.isRemote) {
            return true;
        }

        ItemStack heldItem = playerIn.getHeldItem(hand);
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGrave) {
            TileEntityGrave grave = (TileEntityGrave) te;
            if (grave.getProfile().getId().equals(playerIn.getUniqueID())) {
                if (IsLoaded.CHISELSANDBITS) {
                    if (heldItem.getItem() != Items.AIR && !ChiselsAndBits.isItemBlockChiseled(heldItem)) return false;
                    Headstones.get(worldIn).setHeadstone(playerIn, heldItem);
                    worldIn.notifyBlockUpdate(pos, state, state.withProperty(RENDER, heldItem.getItem() == Items.AIR), 3);
                    grave.markDirty();
                    worldIn.markChunkDirty(pos, grave);
                    worldIn.markBlockRangeForRenderUpdate(pos, pos);
                    worldIn.checkLight(pos);
                    return true;
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
    }

	@Override
	protected BlockStateContainer createBlockState()
	{
        return new BlockStateContainer(this, RENDER, WORLDGEN);
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
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileEntityGrave) {
            TileEntityGrave grave = (TileEntityGrave) te;
            return state.withProperty(RENDER, grave.getDisplayStack().getItem() == Item.getItemFromBlock(net.minecraft.init.Blocks.AIR));
        }
		return state;
	}

	@Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_) {
        TileEntityGrave te = TileTools.getTileEntity(worldIn, pos, TileEntityGrave.class);
        boolean flag = state.getValue(RENDER) && !IsLoaded.CHISELSANDBITS || te == null;
        if (flag) {
            addCollisionBoxToList(pos, mask, list, getBoundingBox(state, worldIn, pos));
        } else {
            ChiselsAndBits.getCollisionBoxes(te.getDisplayStack(), DEFAULTBB).forEach((bb) -> addCollisionBoxToList(pos, mask, list, bb));
        }
    }

	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        TileEntityGrave te = TileTools.getTileEntity(source, pos, TileEntityGrave.class);
        if (te == null) return DEFAULTBB;
        ItemStack stack = te.getDisplayStack();
        boolean flag = state.getValue(RENDER) && !IsLoaded.CHISELSANDBITS;
        if (flag)
            return DEFAULTBB;
        else
            return ChiselsAndBits.getBoundingBox(pos, stack, DEFAULTBB);
    }

	@Override
	public int getMetaFromState(IBlockState state)
	{
        return state.getValue(WORLDGEN) ? 1 : 0;
    }

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
        return getDefaultState().withProperty(WORLDGEN, meta == 1);
    }

	@Override
    public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntityGrave te = TileTools.getTileEntity(world, pos, TileEntityGrave.class);
        ItemStack stack = te.getDisplayStack();
        boolean flag = te.getBlockState().getValue(RENDER) && !IsLoaded.CHISELSANDBITS;
        return flag ? DEFAULTLL : ChiselsAndBits.getLightLevel(stack, DEFAULTLL);
    }
}
