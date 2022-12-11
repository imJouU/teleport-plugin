package me.jouu.teleport.Events;

import me.jouu.teleport.Teleport;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (!(Teleport.getFileManager().getPlayerSettingsConfig().contains(e.getPlayer().getUniqueId().toString()))) {
            ConfigurationSection section = Teleport.getFileManager().getPlayerSettingsConfig().createSection(e.getPlayer().getUniqueId().toString());

            section.set("active", true);
            section.set("blocked", new ArrayList<>());
            section.set("whitelist", new ArrayList<>());

            Teleport.getFileManager().saveFile();
        }
    }
}
