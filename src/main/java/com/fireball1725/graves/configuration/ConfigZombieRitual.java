package com.fireball1725.graves.configuration;

public class ConfigZombieRitual {
    // Enable the relics to decrease the spawn chances (Default: true)
    public static boolean configZombieRelics;

    // Configure how much risk to remove with each relic
    public static int configZombieRelicRiskEasyPercent;
    public static int configZombieRelicRiskNormalPercent;
    public static int configZombieRelicRiskHardPercent;
    public static int configZombieRelicRiskHardcorePercent;

    // Configure if incorrectly placed relics increase zombie spawn chance (Default: true)
    public static boolean configZombieRelicIncorrectAddSpawnChance;

    // Configure if relics can be destroyed when raising the zombie from the dead (Default: true)
    public static boolean configZombieRelicDestroyable;

    // Configure the destroy chance
    public static int configZombieRelicDestroyChanceEasy;
    public static int configZombieRelicDestroyChanceNormal;
    public static int configZombieRelicDestroyChanceHard;
    public static int configZombieRelicDestroyChanceHardcore;

    // Configure if the relics can be duplicated (Default: true)
    public static boolean configZombieRelicDuplicationRecipe;

    // Configure if the player can opt-out of the graves mod (Default: true)
    public static boolean configZombiePlayerOptOut;
}
