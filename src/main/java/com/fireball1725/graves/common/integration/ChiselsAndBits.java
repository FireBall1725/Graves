package com.fireball1725.graves.common.integration;

import mod.chiselsandbits.chiseledblock.BoxType;
import mod.chiselsandbits.chiseledblock.ItemBlockChiseled;
import mod.chiselsandbits.chiseledblock.NBTBlobConverter;
import mod.chiselsandbits.chiseledblock.TileEntityBlockChiseled;
import mod.chiselsandbits.chiseledblock.data.BitCollisionIterator;
import mod.chiselsandbits.chiseledblock.data.VoxelBlob;
import mod.chiselsandbits.helpers.ModUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ChiselsAndBits {

    private static TileEntityBlockChiseledDummy dummy = new TileEntityBlockChiseledDummy();

    public static boolean isItemBlockChiseled(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemBlockChiseled;
    }

    public static int getLightLevel(ItemStack itemStack, int defaultLL) {
        if (!isItemBlockChiseled(itemStack)) return defaultLL;
        NBTBlobConverter blobConverter = new NBTBlobConverter();
        blobConverter.setBlob(ModUtil.getBlobFromStack(itemStack, null));

        return blobConverter.getLightValue();
    }

    public static AxisAlignedBB getBoundingBox(BlockPos pos, ItemStack itemStack, AxisAlignedBB defaultBB) {
        if (!isItemBlockChiseled(itemStack)) return defaultBB;
        dummy.setBlob(ModUtil.getBlobFromStack(itemStack, null));
        return setBounds(dummy, pos, null, null, false);
    }

    public static Collection<AxisAlignedBB> getCollisionBoxes(ItemStack itemStack, AxisAlignedBB defaultBB) {
        if (!isItemBlockChiseled(itemStack)) return Collections.singleton(defaultBB);
        dummy.setBlob(ModUtil.getBlobFromStack(itemStack, null));
        return dummy.getBoxes(BoxType.COLLISION);
    }

    @Nonnull
    private static AxisAlignedBB setBounds(
            final TileEntityBlockChiseled tec,
            final BlockPos pos,
            final AxisAlignedBB mask,
            final List<AxisAlignedBB> list,
            final boolean includePosition) {
        boolean started = false;

        float minX = 0.0f;
        float minY = 0.0f;
        float minZ = 0.0f;

        float maxX = 1.0f;
        float maxY = 1.0f;
        float maxZ = 1.0f;

        final VoxelBlob vb = tec.getBlob();

        final BitCollisionIterator bi = new BitCollisionIterator();
        while (bi.hasNext()) {
            if (bi.getNext(vb) != 0) {
                if (started) {
                    minX = Math.min(minX, bi.physicalX);
                    minY = Math.min(minY, bi.physicalY);
                    minZ = Math.min(minZ, bi.physicalZ);
                    maxX = Math.max(maxX, bi.physicalX + BitCollisionIterator.One16thf);
                    maxY = Math.max(maxY, bi.physicalYp1);
                    maxZ = Math.max(maxZ, bi.physicalZp1);
                } else {
                    started = true;
                    minX = bi.physicalX;
                    minY = bi.physicalY;
                    minZ = bi.physicalZ;
                    maxX = bi.physicalX + BitCollisionIterator.One16thf;
                    maxY = bi.physicalYp1;
                    maxZ = bi.physicalZp1;
                }
            }

            // VERY hackey collision extraction to do 2 bounding boxes, one
            // for top and one for the bottom.
            if (list != null && started && (bi.y == 8 || bi.y == VoxelBlob.dim_minus_one)) {
                final AxisAlignedBB bb = new AxisAlignedBB(
                        (double) minX + pos.getX(),
                        (double) minY + pos.getY(),
                        (double) minZ + pos.getZ(),
                        (double) maxX + pos.getX(),
                        (double) maxY + pos.getY(),
                        (double) maxZ + pos.getZ());

                if (mask.intersectsWith(bb)) {
                    list.add(bb);
                }

                started = false;
                minX = 0.0f;
                minY = 0.0f;
                minZ = 0.0f;
                maxX = 1.0f;
                maxY = 1.0f;
                maxZ = 1.0f;
            }
        }

        if (includePosition) {
            return new AxisAlignedBB(
                    (double) minX + pos.getX(),
                    (double) minY + pos.getY(),
                    (double) minZ + pos.getZ(),
                    (double) maxX + pos.getX(),
                    (double) maxY + pos.getY(),
                    (double) maxZ + pos.getZ());
        }

        return new AxisAlignedBB(
                minX,
                minY,
                minZ,
                maxX,
                maxY,
                maxZ);
    }

    private static class TileEntityBlockChiseledDummy extends TileEntityBlockChiseled {
    }
}
