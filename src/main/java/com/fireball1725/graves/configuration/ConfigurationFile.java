package com.fireball1725.graves.configuration;

import com.fireball1725.graves.helpers.ConfigurationHelper;
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

    public static void loadConfiguration() {
        // General Configuration
        configuration.setCategoryLanguageKey("general", "config.general");

        // Zombie Configuration
        configuration.setCategoryLanguageKey("zombie", "config.zombie");

        ConfigZombie.configZombieEnabled = ConfigurationHelper.getBoolean(configuration, "Zombie Enabled", "zombie.general", true, "Is the grave zombie enabled?");

        ConfigZombie.configZombieSpawnChanceEasy = ConfigurationHelper.getInt(configuration, "Easy Mode Spawn Chance", "zombie.spawnchance", 40, "Spawn chance grave zombie will spawn in easy game mode");
        ConfigZombie.configZombieSpawnChanceNormal = ConfigurationHelper.getInt(configuration, "Normal Mode Spawn Chance", "zombie.spawnchance", 40, "Spawn chance grave zombie will spawn in normal game mode");
        ConfigZombie.configZombieSpawnChanceHard = ConfigurationHelper.getInt(configuration, "Normal Mode Spawn Chance", "zombie.spawnchance", 40, "Spawn chance grave zombie will spawn in hard game mode");
        ConfigZombie.configZombieSpawnChanceHardcore = ConfigurationHelper.getInt(configuration, "Normal Mode Spawn Chance", "zombie.spawnchance", 100, "Spawn chance grave zombie will spawn in hardcore game mode");

        ConfigZombie.configZombieArmorEnabled = ConfigurationHelper.getBoolean(configuration, "Zombie Armor Enabled", "zombie.armor", true, "Is there a chance the grave zombie will spawn with armor");

        ConfigZombie.configZombieArmorChanceEasyNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.armor.easy", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceEasyWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.armor.easy", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceEasyLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.armor.easy", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceEasyIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.armor.easy", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceEasyGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.armor.easy", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceEasyDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.armor.easy", 6, "Chance the grave zombie will spawn with the diamond armor kit");

        ConfigZombie.configZombieArmorChanceNormalNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.armor.normal", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceNormalWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.armor.normal", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceNormalLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.armor.normal", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceNormalIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.armor.normal", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceNormalGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.armor.normal", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceNormalDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.armor.normal", 6, "Chance the grave zombie will spawn with the diamond armor kit");

        ConfigZombie.configZombieArmorChanceHardNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.armor.hard", 30, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceHardWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.armor.hard", 20, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceHardLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.armor.hard", 20, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceHardIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.armor.hard", 16, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceHardGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.armor.hard", 8, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceHardDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.armor.hard", 6, "Chance the grave zombie will spawn with the diamond armor kit");

        ConfigZombie.configZombieArmorChanceHardCoreNone = ConfigurationHelper.getInt(configuration, "No Armor", "zombie.armor.hardcore", 0, "Chance the grave zombie will spawn with no armor");
        ConfigZombie.configZombieArmorChanceHardCoreWoodSword = ConfigurationHelper.getInt(configuration, "Wood Sword", "zombie.armor.hardcore", 0, "Chance the grave zombie will spawn with a wooden sword only");
        ConfigZombie.configZombieArmorChanceHardCoreLeatherKit = ConfigurationHelper.getInt(configuration, "Leather Armor Kit", "zombie.armor.hardcore", 0, "Chance the grave zombie will spawn with the leather armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreIronKit = ConfigurationHelper.getInt(configuration, "Iron Armor Kit", "zombie.armor.hardcore", 0, "Chance the grave zombie will spawn with the iron armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreGoldKit = ConfigurationHelper.getInt(configuration, "Gold Armor Kit", "zombie.armor.hardcore", 0, "Chance the grave zombie will spawn with the gold armor kit");
        ConfigZombie.configZombieArmorChanceHardCoreDiamondKit = ConfigurationHelper.getInt(configuration, "Diamond Armor Kit", "zombie.armor.hardcore", 100, "Chance the grave zombie will spawn with the diamond armor kit");

        ConfigZombie.configZombieDefaultSpeed = ConfigurationHelper.getDouble(configuration, "Default Zombie Speed", "zombie.default", 0.50, "Grave zombie's default speed");
        ConfigZombie.configZombieDefaultBaseDamage = ConfigurationHelper.getDouble(configuration, "Default Zombie Damage", "zombie.default", 6, "Grave zombie's default base damage");
        ConfigZombie.configZombieDefaultHealth = ConfigurationHelper.getInt(configuration, "Default Zombie Health", "zombie.default", 20, "Default amount of health for the grave zombie (2 = 1 heart)");
        ConfigZombie.configZombieDefaultFollowRange = ConfigurationHelper.getDouble(configuration, "Default Zombie Follow Range", "zombie.default", 100, "Default follow range for grave zombie");

        ConfigZombie.configZombieShowBossBar = ConfigurationHelper.getBoolean(configuration, "Show boss bar", "zombie.general", true, "Show the boss bar");

        // Zombie Ritual Configuration
        configuration.setCategoryLanguageKey("zombieritural", "config.zombieritual");

        configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        loadConfiguration();
    }
}
