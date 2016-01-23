package com.fireball1725.graves.helpers;

import com.mojang.authlib.GameProfile;

public interface IDeadPlayerEntity {
    GameProfile getProfile();
    String getUsername();
    void setUsername(String name);

    double getInterpolatedCapeX(float partialTickTime);
    double getInterpolatedCapeY(float partialTickTime);
    double getInterpolatedCapeZ(float partialTickTime);
}
