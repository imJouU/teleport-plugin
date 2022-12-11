package me.jouu.teleport.Managers;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Request {
    private final @Getter UUID uuid;
    private final @Getter Player requester;
    private final @Getter Player target;
    private final @Getter BukkitRunnable runnable;

    public Request(UUID uuid, Player requester, Player target, BukkitRunnable runnable) {
        this.uuid = uuid;
        this.requester = requester;
        this.target = target;
        this.runnable = runnable;
    }
}
