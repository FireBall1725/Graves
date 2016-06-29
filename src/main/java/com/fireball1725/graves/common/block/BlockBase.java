package com.fireball1725.graves.common.block;

import com.fireball1725.graves.common.reference.ModInfo;
import com.fireball1725.graves.common.tileentity.TileEntityBase;
import com.fireball1725.graves.common.tileentity.TileEntityInventoryBase;
import com.fireball1725.graves.common.util.TileTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.ArrayList;
import java.util.List;

public class BlockBase extends BlockContainer {
    protected boolean isInventory = false;
    protected boolean hasSubtypes = false;
    private Class<? extends TileEntity> tileEntityType = null;

    protected BlockBase(Material material) {
        super(material);

		//        setStepSound(SoundType.STONE);
		setHardness(2.2F);
        setResistance(5.0F);
        //setHarvestLevel("pickaxe", 0);


    }

    @Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		if (hasBlockTileEntity()) {
            try {
				return tileEntityType.newInstance();
			} catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private boolean hasBlockTileEntity() {
        return this.tileEntityType != null;
    }

    protected void setTileEntity(Class<? extends TileEntity> c) {
        String tileName = "tileentity." + ModInfo.MOD_ID + "." + c.getSimpleName();

        GameRegistry.registerTileEntity(this.tileEntityType = c, tileName);
        this.isInventory = IInventory.class.isAssignableFrom(c);
        setTileProvider(hasBlockTileEntity());
    }

    private void setTileProvider(boolean b) {
		ReflectionHelper.setPrivateValue(Block.class, this, Boolean.valueOf(b), "isTileProvider");
	}

    public Class<? extends TileEntity> getTileEntityClass() {
        return this.tileEntityType;
    }

	//    @Override
	//    public void breakBlock(World world, BlockPos blockPos, IBlockState blockState) {
	//        dropInventory(world, blockPos);
	//
	//        super.breakBlock(world, blockPos, blockState);
	//    }
	//
	//    protected void dropInventory(World world, BlockPos blockPos) {
	//        for (ItemStack itemStack : getDrops(world, blockPos, null, 0)) {
	//
	//            if (itemStack != null && itemStack.stackSize > 0) {
	//                Random rand = new Random();
	//
	//                float dX = rand.nextFloat() * 0.8F + 0.1F;
	//                float dY = rand.nextFloat() * 0.8F + 0.1F;
	//                float dZ = rand.nextFloat() * 0.8F + 0.1F;
	//
	//                EntityItem entityItem = new EntityItem(world, blockPos.getX() + dX, blockPos.getY() + dY, blockPos.getZ() + dZ, itemStack.copy());
	//
	//                if (itemStack.hasTagCompound()) {
	//                    entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
	//                }
	//
	//                float factor = 0.05F;
	//                entityItem.motionX = rand.nextGaussian() * factor;
	//                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
	//                entityItem.motionZ = rand.nextGaussian() * factor;
	//                world.spawnEntityInWorld(entityItem);
	//                itemStack.stackSize = 0;
	//            }
	//        }
	//    }

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
	{
		TileEntityInventoryBase base = TileTools.getTileEntity(world, pos, TileEntityInventoryBase.class);
		if(base != null)
		{
			List<ItemStack> drops = new ArrayList<ItemStack>();
			IInventory iInventory = base.getInternalInventory();
			for(int i = 0; i < iInventory.getSizeInventory(); i++)
				drops.add(iInventory.getStackInSlot(i));
			return drops;
		}
		return super.getDrops(world, pos, state, fortune);
	}

	@Override
	public String getUnlocalizedName() {
        String blockName = getUnwrappedUnlocalizedName(super.getUnlocalizedName());

        String test = String.format("tile.%s", blockName);
        return test.toLowerCase();
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        super.onBlockPlacedBy(world, blockPos, state, placer, itemStack);

        if (itemStack.hasDisplayName()) {
            TileEntityBase tileEntityBase = TileTools.getTileEntity(world, blockPos, TileEntityBase.class);
			if(tileEntityBase != null)
			{
				tileEntityBase.setCustomName(itemStack.getDisplayName());
			}
		}
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
	{
		return !(entity instanceof EntityDragon) && super.canEntityDestroy(state, world, pos, entity);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
}
