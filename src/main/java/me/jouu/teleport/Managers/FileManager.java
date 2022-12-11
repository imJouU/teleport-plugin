package me.jouu.teleport.Managers;

import lombok.Getter;
import me.jouu.teleport.Teleport;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileManager {
    public static File playerSettingsFile;
    public static FileConfiguration playerSettingsConfig;

    public FileConfiguration getPlayerSettingsConfig() {
        return playerSettingsConfig;
    }

    public void createFiles() {
        playerSettingsFile = new File(Teleport.getInstance().getDataFolder(), "player-settings.yml");

        if (!playerSettingsFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[WARNING] &fThe configuration file doesn't exist."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&5[EVENT] &fCreating &bplayer-settings.yml &ffile..."));

            Teleport.getInstance().saveResource("player-settings.yml", false);

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[INFO] &fThe file has been created."));
        }

        playerSettingsConfig = new YamlConfiguration();
        try {
            playerSettingsConfig.load(playerSettingsFile);

            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[INFO] &fLoading files..."));
            Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&2[INFO] &fTeleport-Config status: " + (playerSettingsFile.exists() ? "&aLoaded." : "&cNot loaded.")));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile() {
        try {
            playerSettingsConfig.save(playerSettingsFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
