package me.jouu.teleport.Managers;

import me.jouu.teleport.Teleport;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerSettings {
    public static String findPlayer(Player player, Player target, String list) {
        FileConfiguration fileConfiguration = Teleport.getFileManager().getPlayerSettingsConfig();

        ConfigurationSection section = fileConfiguration.getConfigurationSection(player.getUniqueId().toString());

        for (String blocked : section.getStringList(list)) if (blocked.equals(target.getUniqueId().toString())) return blocked;

        return null;
    }

    public static void blockPlayer(Player player, Player target) {
        FileConfiguration fileConfiguration = Teleport.getFileManager().getPlayerSettingsConfig();

        ConfigurationSection section = fileConfiguration.getConfigurationSection(player.getUniqueId().toString());

        List<String> players = section.getStringList("blocked");
        players.add(target.getUniqueId().toString());

        section.set("blocked", players);

        Teleport.getFileManager().saveFile();
    }

    public static void whitelistPlayer(Player player, Player target) {
        FileConfiguration fileConfiguration = Teleport.getFileManager().getPlayerSettingsConfig();

        ConfigurationSection section = fileConfiguration.getConfigurationSection(player.getUniqueId().toString());

        List<String> players = section.getStringList("whitelist");
        players.add(target.getUniqueId().toString());

        section.set("whitelist", players);

        Teleport.getFileManager().saveFile();
    }

    public static void toggleTeleportRequests(Player player) {
        FileConfiguration fileConfiguration = Teleport.getFileManager().getPlayerSettingsConfig();

        ConfigurationSection section = fileConfiguration.getConfigurationSection(player.getUniqueId().toString());

        boolean reqStatus = section.getBoolean("active");

        section.set("active", !reqStatus);

        Teleport.getFileManager().saveFile();
    }

    public static boolean getReqStatus(Player player) {
        FileConfiguration fileConfiguration = Teleport.getFileManager().getPlayerSettingsConfig();

        ConfigurationSection section = fileConfiguration.getConfigurationSection(player.getUniqueId().toString());

        return section.getBoolean("active");
    }
}
