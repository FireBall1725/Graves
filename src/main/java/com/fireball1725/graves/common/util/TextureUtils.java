package com.fireball1725.graves.common.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class TextureUtils {

    private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");

    public static ResourceLocation getPlayerSkin(GameProfile profile) {
        return getPlayerTexture(profile, MinecraftProfileTexture.Type.SKIN);
    }

    public static ResourceLocation getPlayerCape(GameProfile profile) {
        return getPlayerTexture(profile, MinecraftProfileTexture.Type.CAPE);
    }

    private static ResourceLocation getPlayerTexture(GameProfile profile, MinecraftProfileTexture.Type type) {
        if (profile != null && profile.getName() != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);

            if (map.containsKey(type))
                return minecraft.getSkinManager().loadSkin(map.get(type), type);
        }

        return getDefault(profile, type);
    }

    public static ResourceLocation getDefault(GameProfile profile, MinecraftProfileTexture.Type type) {
        if (type == Type.CAPE)
            return null;
        else {
            String skinType = getPlayerSkinType(profile);
            if ("slim".equals(skinType))
                return TEXTURE_ALEX;
            else
                return TEXTURE_STEVE;
        }
    }

    public static String getPlayerSkinType(GameProfile profile) {
        String type = null;

        if (profile != null && profile.getName() != null) {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
            if (map.containsKey(Type.SKIN))
                type = map.get(Type.SKIN).getMetadata("model");
        }
        if (type == null)
            type = "default";

        return type;
    }
}