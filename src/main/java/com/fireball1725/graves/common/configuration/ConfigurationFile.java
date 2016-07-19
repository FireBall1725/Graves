package com.fireball1725.graves.common.configuration;

import com.fireball1725.graves.common.helpers.ConfigurationHelper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationFile {
    public static Configuration configuration;

    public static Configuration init(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        return configuration;
    }

    private static void loadConfiguration() {
        // General Configuration
        configuration.setCategoryLanguageKey("general", "general");

        ConfigGeneral.printToChat = ConfigurationHelper.getBoolean(configuration, "Death coordinates in chat", "general", ConfigGeneral.printToChat, "Should death coordinates be put in chat on death?");

        // World Generation Configuration
        configuration.setCategoryLanguageKey("worldgen", "worldgen");

        ConfigWorldGen.showStartupMessage = ConfigurationHelper.getBoolean(configuration, "Show Loot Grave Message", "worldgen", ConfigWorldGen.showStartupMessage, "Should the loot grave message display on world creation?");
        ConfigWorldGen.doWorldGen = ConfigurationHelper.getBoolean(configuration, "WorldGen Enabled", "worldgen", ConfigWorldGen.doWorldGen, "Should graves generate in the world?");
        ConfigWorldGen.genPercentage = ConfigurationHelper.getDouble(configuration, "GenPercentage", "worldgen", ConfigWorldGen.genPercentage, "Percentage chance that a grave will spawn in a chunk.");

        // Zombie Configuration
        configuration.setCategoryLanguageKey("zombie", "config.zombie");

        // General Zombie Configuration
        ConfigZombie.configZombieEnabled = ConfigurationHelper.getBoolean(configuration, "Zombie Enabled", "zombie.general", true, "Is the grave zombie enabled?");
        ConfigZombie.configZombieRelics = ConfigurationHelper.getBoolean(configuration, "Enable Relics", "zombie.general", true, "Enable relics that spawn in loot chests");
        ConfigZombie.configZombieRelicDuplicationRecipe = ConfigurationHelper.getBoolean(configuration, "Enable Relic Duplication", "zombie.general", true, "Allow relics to be duplicated with recipes");
        ConfigZombie.configZombiePlayerOptOut = ConfigurationHelper.getBoolean(configuration, "Allow player to opt-out", "zombie.general", true, "Allow player to preform a ritual to opt-out of graves");
        ConfigZombie.configZombieShowBossBar = ConfigurationHelper.getBoolean(configuration, "Show boss bar", "zombie.general", true, "Show the boss bar");

        // Zombie Difficulty Configuration
        ConfigZombie.configZombieArmorEnabled = ConfigurationHelper.getBoolean(configuration, "Zombie Armor Enabled", "zombie.difficulty", true, "Is there a chance the grave zombie will spawn with armor");
        ConfigZombie.configZombieRelicDestroyable = ConfigurationHelper.getBoolean(configuration, "Relics random destroyable", "zombie.difficulty", true, "Can the relics be randomly destroyed");
        ConfigZombie.configZombieRelicIncorrectAddSpawnChance = ConfigurationHelper.getBoolean(configuration, "Relics hard mode", "zombie.difficulty", true, "Increase the spawn chance of the zombie if relics are placed incorrectly");

        ConfigZombie.configZombieArmorChanceEasyNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.difficulty.easy", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceEasyWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.difficulty.easy", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceEasyLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.difficulty.easy", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceEasyIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.difficulty.easy", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceEasyGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.difficulty.easy", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceEasyDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.difficulty.easy", 6, "Chance the grave zombie will spawn with the diamond armor kit");
        ConfigZombie.configZombieSpawnChanceEasy = ConfigurationHelper.getInt(configuration, "Spawn Chance", "zombie.difficulty.easy", 40, "Spawn chance grave zombie will spawn in easy game mode");
        ConfigZombie.configZombieRelicDestroyChanceEasy = ConfigurationHelper.getInt(configuration, "Relic Destroy Chance", "zombie.difficulty.easy", 10, "Chance the relic will be destroyed");
        ConfigZombie.configZombieRelicRiskEasyPercent = ConfigurationHelper.getInt(configuration, "Relic Reduce Chance", "zombie.difficulty.easy", 10, "The amount of chance to reduce the zombie spawning with a relic placed");

        ConfigZombie.configZombieArmorChanceNormalNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.difficulty.normal", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceNormalWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.difficulty.normal", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceNormalLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.difficulty.normal", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceNormalIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.difficulty.normal", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceNormalGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.difficulty.normal", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceNormalDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.difficulty.normal", 6, "Chance the grave zombie will spawn with the diamond armor kit");
        ConfigZombie.configZombieSpawnChanceNormal = ConfigurationHelper.getInt(configuration, "Spawn Chance", "zombie.difficulty.normal", 40, "Spawn chance grave zombie will spawn in normal game mode");
        ConfigZombie.configZombieRelicDestroyChanceNormal = ConfigurationHelper.getInt(configuration, "Relic Destroy Chance", "zombie.difficulty.normal", 10, "Chance the relic will be destroyed");
        ConfigZombie.configZombieRelicRiskNormalPercent = ConfigurationHelper.getInt(configuration, "Relic Reduce Chance", "zombie.difficulty.normal", 10, "The amount of chance to reduce the zombie spawning with a relic placed");

        ConfigZombie.configZombieArmorChanceHardNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.difficulty.hard", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceHardWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.difficulty.hard", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceHardLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.difficulty.hard", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceHardIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.difficulty.hard", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceHardGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.difficulty.hard", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceHardDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.difficulty.hard", 6, "Chance the grave zombie will spawn with the diamond armor kit");
        ConfigZombie.configZombieSpawnChanceHard = ConfigurationHelper.getInt(configuration, "Spawn Chance", "zombie.difficulty.hard", 40, "Spawn chance grave zombie will spawn in hard game mode");
        ConfigZombie.configZombieRelicDestroyChanceHard = ConfigurationHelper.getInt(configuration, "Relic Destroy Chance", "zombie.difficulty.hard", 10, "Chance the relic will be destroyed");
        ConfigZombie.configZombieRelicRiskHardPercent = ConfigurationHelper.getInt(configuration, "Relic Reduce Chance", "zombie.difficulty.hard", 10, "The amount of chance to reduce the zombie spawning with a relic placed");

        ConfigZombie.configZombieArmorChanceHardCoreNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.difficulty.hardcore", 0, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceHardCoreWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.difficulty.hardcore", 0, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceHardCoreLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.difficulty.hardcore", 0, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.difficulty.hardcore", 0, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.difficulty.hardcore", 0, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.difficulty.hardcore", 100, "Chance the grave zombie will spawn with the diamond armor kit");
        ConfigZombie.configZombieSpawnChanceHardCore = ConfigurationHelper.getInt(configuration, "Spawn Chance", "zombie.difficulty.hardcore", 100, "Spawn chance grave zombie will spawn in hardcore game mode");
        ConfigZombie.configZombieRelicDestroyChanceHardCore = ConfigurationHelper.getInt(configuration, "Relic Destroy Chance", "zombie.difficulty.hardcore", 80, "Chance the relic will be destroyed");
        ConfigZombie.configZombieRelicRiskHardCorePercent = ConfigurationHelper.getInt(configuration, "Relic Reduce Chance", "zombie.difficulty.hardcore", 5, "The amount of chance to reduce the zombie spawning with a relic placed");

        // Zombie Entity Defaults
        ConfigZombie.configZombieDefaultSpeed = ConfigurationHelper.getDouble(configuration, "Default Zombie Speed", "zombie.default", 0.50, "Grave zombie's default speed");
        ConfigZombie.configZombieDefaultBaseDamage = ConfigurationHelper.getDouble(configuration, "Default Zombie Damage", "zombie.default", 6, "Grave zombie's default base damage");
        ConfigZombie.configZombieDefaultHealth = ConfigurationHelper.getInt(configuration, "Default Zombie Health", "zombie.default", 20, "Default amount of health for the grave zombie (2 = 1 heart)");
        ConfigZombie.configZombieDefaultFollowRange = ConfigurationHelper.getDouble(configuration, "Default Zombie Follow Range", "zombie.default", 100, "Default follow range for grave zombie");

        configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        loadConfiguration();
    }
}
