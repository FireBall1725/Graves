package com.fireball1725.graves.common.util;

import com.fireball1725.graves.Graves;
import com.fireball1725.graves.common.integration.IsLoaded;
import com.fireball1725.graves.common.network.packets.SyncHeadstonesToClients;
import com.fireball1725.graves.common.reference.ModInfo;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.util.Map;
import java.util.UUID;

public class Headstones extends WorldSavedData {
    private static final String DATA_NAME = ModInfo.MOD_ID + "_headstones";
    private Map<UUID, ItemStack> headstones = Maps.newHashMap();

    public Headstones(String name) {
        super(name);
    }

    public static Headstones get(World world) {
        MapStorage storage = world.getMapStorage();
        Headstones instance = (Headstones) storage.getOrLoadData(Headstones.class, DATA_NAME);

        if (instance == null) {
            instance = new Headstones(DATA_NAME);
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    public void setHeadstone(EntityPlayer player, ItemStack stack) {
        setHeadstone(player.getUniqueID(), stack);
    }

    public void setHeadstone(UUID uuid, ItemStack stack) {
        if (getHeadstone(uuid).isItemEqual(stack) && getHeadstone(uuid).getTagCompound().equals(stack.getTagCompound()))
            return;
        headstones.put(uuid, stack);
        Graves.packetHandler.sendToAll(new SyncHeadstonesToClients(writeToNBT(new NBTTagCompound())));
        markDirty();
    }

    public ItemStack getHeadstone(EntityPlayer player) {
        return getHeadstone(player.getUniqueID());
    }

    public ItemStack getHeadstone(UUID uuid) {
        if (!IsLoaded.CHISELSANDBITS) return ItemStack.EMPTY;
        ItemStack stack = headstones.containsKey(uuid) ? headstones.get(uuid) : ItemStack.EMPTY;
        return stack != null ? stack : ItemStack.EMPTY;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("headstones")) {
            NBTTagList tagList = tag.getTagList("headstones", 10);
            if (tagList.tagCount() < 1) return;
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound headstone = tagList.getCompoundTagAt(i);
                headstones.put(NBTUtil.getUUIDFromTag(headstone.getCompoundTag("uuid")), new ItemStack(headstone.getCompoundTag("stack")));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList tagList = new NBTTagList();
        for (UUID id : headstones.keySet()) {
            NBTTagCompound headstone = new NBTTagCompound();
            headstone.setTag("uuid", NBTUtil.createUUIDTag(id));
            headstone.setTag("stack", headstones.get(id).serializeNBT());
            tagList.appendTag(headstone);
        }
        tag.setTag("headstones", tagList);
        return tag;
    }
}
