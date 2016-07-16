package com.fireball1725.graves.common.world;

import com.fireball1725.graves.client.render.TEGraveSR;
import com.fireball1725.graves.common.block.BlockGrave;
import com.fireball1725.graves.common.block.Blocks;
import com.fireball1725.graves.common.tileentity.TileEntityGrave;
import com.mojang.authlib.GameProfile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

public class WorldGeneration implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (random.nextInt(16) == 1) {
            BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos((chunkX * 16) + 16 * random.nextDouble(), 0, (chunkZ * 16) + 16 * random.nextDouble()));
            if (!world.getBlockState(pos).getMaterial().isLiquid()) {
                world.setBlockState(pos, ((BlockGrave) Blocks.BLOCK_GRAVE.block).getWorldGenState());
                TileEntity te = world.getTileEntity(pos);
                if (te != null && te instanceof TileEntityGrave) {
                    TileEntityGrave grave = (TileEntityGrave) te;
                    Iterator iterator = TEGraveSR.specialText.keySet().iterator();
                    int idx = random.nextInt(TEGraveSR.specialText.keySet().size());
                    while (iterator.hasNext()) {
                        String uuid = iterator.next().toString();
                        if (idx-- == 0) {
                            grave.setProfile(new GameProfile(UUID.fromString(uuid), TEGraveSR.specialText.get(uuid).get("name")));
                            LootTable table = world.getLootTableManager().getLootTableFromLocation(LootTableList.CHESTS_SIMPLE_DUNGEON);
                            grave.addItems(table.generateLootForPools(random, new LootContext.Builder((WorldServer) world).build()));
                            break;
                        }
                    }
                }
            }
        }
    }
}
