package me.jouu.teleport;

import lombok.Getter;
import me.jouu.teleport.Events.PlayerJoin;
import me.jouu.teleport.Managers.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Teleport extends JavaPlugin {
    private static @Getter Teleport instance;
    private static @Getter FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;

        fileManager = new FileManager();
        fileManager.createFiles();

        getServer().getPluginCommand("teleport").setExecutor(new me.jouu.teleport.Commands.Teleport());
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        getServer().getConsoleSender().sendMessage("");
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPlugin activado!"));
        getServer().getConsoleSender().sendMessage("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
