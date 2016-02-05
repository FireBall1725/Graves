package com.fireball1725.graves.configuration;

public class ConfigZombie {
    // Is zombie enabled (Default = true)
    public static boolean configZombieEnabled;

    // Chance that zombie will spawn (Default: 40)
    public static int configZombieSpawnChanceEasy;
    public static int configZombieSpawnChanceNormal;
    public static int configZombieSpawnChanceHard;
    public static int configZombieSpawnChanceHardCore;

    // Does zombie have a chance to spawn with armor / weapons (Default: true)
    public static boolean configZombieArmorEnabled;

    // Zombie armor
    public static int configZombieArmorChanceEasyNone;
    public static int configZombieArmorChanceEasyWoodSword;
    public static int configZombieArmorChanceEasyLeatherKit;
    public static int configZombieArmorChanceEasyIronKit;
    public static int configZombieArmorChanceEasyGoldKit;
    public static int configZombieArmorChanceEasyDiamondKit;

    public static int configZombieArmorChanceNormalNone;
    public static int configZombieArmorChanceNormalWoodSword;
    public static int configZombieArmorChanceNormalLeatherKit;
    public static int configZombieArmorChanceNormalIronKit;
    public static int configZombieArmorChanceNormalGoldKit;
    public static int configZombieArmorChanceNormalDiamondKit;

    public static int configZombieArmorChanceHardNone;
    public static int configZombieArmorChanceHardWoodSword;
    public static int configZombieArmorChanceHardLeatherKit;
    public static int configZombieArmorChanceHardIronKit;
    public static int configZombieArmorChanceHardGoldKit;
    public static int configZombieArmorChanceHardDiamondKit;

    public static int configZombieArmorChanceHardCoreNone;
    public static int configZombieArmorChanceHardCoreWoodSword;
    public static int configZombieArmorChanceHardCoreLeatherKit;
    public static int configZombieArmorChanceHardCoreIronKit;
    public static int configZombieArmorChanceHardCoreGoldKit;
    public static int configZombieArmorChanceHardCoreDiamondKit;

    // Zombie base stats
    public static double configZombieDefaultSpeed;
    public static double configZombieDefaultBaseDamage;
    public static int configZombieDefaultHealth;
    public static double configZombieDefaultFollowRange;

    // Show Zombie Boss Bar (Default: true)
    public static boolean configZombieShowBossBar;

    // Enable the relics to decrease the spawn chances (Default: true)
    public static boolean configZombieRelics;

    // Configure how much risk to remove with each relic
    public static int configZombieRelicRiskEasyPercent;
    public static int configZombieRelicRiskNormalPercent;
    public static int configZombieRelicRiskHardPercent;
    public static int configZombieRelicRiskHardCorePercent;

    // Configure if incorrectly placed relics increase zombie spawn chance (Default: true)
    public static boolean configZombieRelicIncorrectAddSpawnChance;

    // Configure if relics can be destroyed when raising the zombie from the dead (Default: true)
    public static boolean configZombieRelicDestroyable;

    // Configure the destroy chance
    public static int configZombieRelicDestroyChanceEasy;
    public static int configZombieRelicDestroyChanceNormal;
    public static int configZombieRelicDestroyChanceHard;
    public static int configZombieRelicDestroyChanceHardCore;

    // Configure if the relics can be duplicated (Default: true)
    public static boolean configZombieRelicDuplicationRecipe;

    // Configure if the player can opt-out of the graves mod (Default: true)
    public static boolean configZombiePlayerOptOut;
}
