package me.jouu.teleport.Commands;

import me.jouu.teleport.Managers.PlayerSettings;
import me.jouu.teleport.Managers.Request;
import me.jouu.teleport.Managers.RequestManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class Teleport implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command is only available for players.");

            return false;
        }

        /**
         * COMMAND      arg-1       arg-2
         *
         * /teleport    <player>                    // Length == 1
         * /teleport    myrequests                  // Length == 1
         * /teleport    toggle                      // Length == 1
         * /teleport    accept      <player>        // Length == 2
         * /teleport    deny        <player>        // Length == 2
         * /teleport    block       <player>        // Length == 2
         * /teleport    whitelist   <player>        // Length == 2
         */

        if (strings.length == 0) {
            commandSender.sendMessage("");
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            commandSender.sendMessage("");
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/teleport &b(player) &7- &fUse..."));
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/teleport accept &b(player) &7- &fUse..."));
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/teleport deny &b(player) &7- &fUse..."));
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/teleport myrequests &7- &fUse..."));
            commandSender.sendMessage("");
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            commandSender.sendMessage("");

            return false;
        }

        Player requester = (Player) commandSender;

        // me falta este comando <3
        if (strings[0].equalsIgnoreCase("myrequests")) {
            requester.sendMessage(":D");

            return true;
        }

        if (strings[0].equalsIgnoreCase("toggle")) {
            PlayerSettings.toggleTeleportRequests(requester);

            boolean reqStatus = PlayerSettings.getReqStatus(requester);

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fYour teleport requests status has been updated!"));
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest: " + (reqStatus ? "&aACTIVE&f." : "&CNOT ACTIVE&f.")));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");

            return true;
        }

        if (strings[0].equalsIgnoreCase("accept")) {
            if (strings.length == 1) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArguments missing... (Player)"));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUsage: &e/teleport accept &b(player)"));
                requester.sendMessage("");

                return false;
            }

            // Player clicked the message (JSON // Teleport ID method)
            if (strings[1].length() == 36) {
                UUID reqId = UUID.fromString(strings[1]);

                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Looking for request..."));

                Request request = RequestManager.findReqById(reqId);
                if (request == null) {
                    requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis teleport request could not be found."));
                    requester.sendMessage("");

                    return false;
                }
                requester.sendMessage("");

                Player target = request.getRequester();

                request.getRunnable().cancel();
                RequestManager.deleteRequest(request);

                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aCONFIRMED&f."));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + target.getName() + "&r &fwill teleport to you in &b3 &fseconds..."));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");

                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + requester.getName() + "&r &fhas &aCONFIRMED &fyour teleport request."));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8Teleporting in 3 seconds..."));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        target.teleport(requester);

                        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleported!"));
                    }
                }.runTaskLater(me.jouu.teleport.Teleport.getInstance(), 60L);

                return true;
            }

            Player target = Bukkit.getPlayerExact(strings[1]);
            if (target == null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not online."));
                requester.sendMessage("");

                return false;
            }

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Looking for request..."));

            Request request = RequestManager.findReqByRequesterAndTarget(target, requester);
            if (request == null) {
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis teleport request could not be found."));
                requester.sendMessage("");

                return false;
            }

            request.getRunnable().cancel();
            RequestManager.deleteRequest(request);

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aCONFIRMED&f."));
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + target.getName() + "&r &fwill teleport to you in &b3 &fseconds..."));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");

            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + requester.getName() + "&r &fhas &aCONFIRMED &fyour teleport request."));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8Teleporting in 3 seconds..."));
            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            target.sendMessage("");

            new BukkitRunnable() {
                @Override
                public void run() {
                    target.teleport(requester);

                    target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aTeleported!"));
                }
            }.runTaskLater(me.jouu.teleport.Teleport.getInstance(), 60L);

            return true;
        }

        if (strings[0].equalsIgnoreCase("deny")) {
            if (strings.length == 1) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArguments missing... (Player)"));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUsage: &e/teleport deny &b(player)"));
                requester.sendMessage("");

                return false;
            }

            // Player clicked the message (JSON // Teleport ID method)
            if (strings[1].length() == 36) {
                UUID reqId = UUID.fromString(strings[1]);

                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Looking for request..."));

                Request request = RequestManager.findReqById(reqId);
                if (request == null) {
                    requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis teleport request could not be found."));
                    requester.sendMessage("");

                    return false;
                }
                requester.sendMessage("");

                Player target = request.getRequester();

                request.getRunnable().cancel();
                RequestManager.deleteRequest(request);

                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aDENIED&f."));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");

                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aDENIED&f."));
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + target.getName() + "&r &fhas denied your teleport request."));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");

                return true;
            }

            Player target = Bukkit.getPlayerExact(strings[1]);
            if (target == null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not online."));
                requester.sendMessage("");

                return false;
            }

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Looking for request..."));

            Request request = RequestManager.findReqByRequesterAndTarget(target, requester);
            if (request == null) {
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis teleport request could not be found."));
                requester.sendMessage("");

                return false;
            }

            request.getRunnable().cancel();
            RequestManager.deleteRequest(request);

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aDENIED&f."));
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            requester.sendMessage("");

            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fRequest &aDENIED&f."));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &e" + target.getName() + "&r &fhas denied your teleport request."));
            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
            target.sendMessage("");

            return true;
        }

        if (strings[0].equalsIgnoreCase("block")) {
            if (strings.length == 1) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArguments missing... (Player)"));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUsage: &e/teleport block &b(player)"));
                requester.sendMessage("");

                return false;
            }

            Player target = Bukkit.getPlayerExact(strings[1]);
            if (target == null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not online."));
                requester.sendMessage("");

                return false;
            }

            if (PlayerSettings.findPlayer(requester, target, "blocked") != null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is already blocked."));
                requester.sendMessage("");

                return false;
            }

            PlayerSettings.blockPlayer(requester, target);

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlayer &cBLOCKED!"));
            requester.sendMessage("");

            return true;
        }

        if (strings[0].equalsIgnoreCase("whitelist")) {
            if (strings.length == 1) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cArguments missing... (Player)"));
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fUsage: &e/teleport block &b(player)"));
                requester.sendMessage("");

                return false;
            }

            Player target = Bukkit.getPlayerExact(strings[1]);
            if (target == null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not online."));
                requester.sendMessage("");

                return false;
            }

            if (PlayerSettings.findPlayer(requester, target, "whitelist") != null) {
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is already on the whitelist."));
                requester.sendMessage("");

                return false;
            }

            PlayerSettings.whitelistPlayer(requester, target);

            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&fPlayer &aADDED &fto whitelist!"));
            requester.sendMessage("");

            return true;
        }

        Player target = Bukkit.getPlayerExact(strings[0]);
        if (target == null) {
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player is not online."));
            requester.sendMessage("");

            return false;
        }

        Request request = RequestManager.findReqByRequesterAndTarget(target, requester);
        if (request != null) {
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have already sent a teleport request to this player."));
            requester.sendMessage("");

            return false;
        }

        if (PlayerSettings.findPlayer(target, requester, "blocked") != null) {
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot send a teleport request to this player because he/she has blocked you."));
            requester.sendMessage("");

            return false;
        }

        if (!PlayerSettings.getReqStatus(target) && PlayerSettings.findPlayer(target, requester, "whitelist") != null) {
            requester.sendMessage("");
            requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot send a teleport request to this player due he/she has disabled teleport requests."));
            requester.sendMessage("");

            return false;
        }

        UUID reqId = UUID.randomUUID();

        createNewRequest(reqId, requester, target, new BukkitRunnable() {
            @Override
            public void run() {
                RequestManager.deleteRequest(RequestManager.findReqByRequesterAndTarget(requester, target));

                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &c&nTeleport request CANCELLED&r&c!"));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fThe request sent to &e" + target.getName() + "&r &fhas been cancelled."));
                requester.sendMessage("");
                requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                requester.sendMessage("");

                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &c&nTeleport request CANCELLED&r&c!"));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fThe request sent by &e" + requester.getName() + "&r &fhas been cancelled."));
                target.sendMessage("");
                target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
                target.sendMessage("");
            }
        });

        requester.sendMessage("");
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8Sending request..."));

        return true;
    }

    private void createNewRequest(UUID reqId, Player requester, Player target, BukkitRunnable runnable) {
        Request request = new Request(reqId, requester, target, runnable);

        RequestManager.addNewRequest(request);

        request.getRunnable().runTaskLater(me.jouu.teleport.Teleport.getInstance(), 1200L);

        sendRequesterMessage(requester, target);
        sendTargetMessage(reqId, target, requester);
    }

    private void sendRequesterMessage(Player requester, Player target) {
        requester.sendMessage("");
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
        requester.sendMessage("");
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &a&nTeleport request sent&r&a!"));
        requester.sendMessage("");
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fYou have sent a teleport request to &b" + target.getName()));
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8Please wait until the player confirms the teleport request..."));
        requester.sendMessage("");
        requester.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
        requester.sendMessage("");
    }

    private void sendTargetMessage(UUID reqId, Player target, Player requester) {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "[{\"text\": \" &fClick \"},{\"text\": \"&a&lACCEPT\",\"clickEvent\": {\"action\": \"run_command\",\"value\": \"/teleport accept " + reqId + "\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": \"&aClick to accept the request.\"}},{\"text\": \"&r &fto confirm the request or click on \"},{\"text\": \"&c&lDENY\",\"clickEvent\": {\"action\": \"run_command\",\"value\": \"/teleport deny " + reqId + "\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": \"&cClick to deny the request.\"}},{\"text\": \"&r &fto deny it.\"}]"));
        PacketPlayOutChat chat = new PacketPlayOutChat(comp);

        target.sendMessage("");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
        target.sendMessage("");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &a&nNew teleport request&r&a!"));
        target.sendMessage("");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &fYou have received a teleport request from &b" + requester.getName() + "&f."));
        target.sendMessage("");
        ((CraftPlayer) target).getHandle().playerConnection.sendPacket(chat);
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', " &8You have 60 seconds to accept it before it is cancelled."));
        target.sendMessage("");
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&m-----------------------------------------------------&r"));
        target.sendMessage("");
    }
}
