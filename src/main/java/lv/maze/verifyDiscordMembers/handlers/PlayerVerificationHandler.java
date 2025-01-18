package lv.maze.verifyDiscordMembers.handlers;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import lv.maze.verifyDiscordMembers.utils.TeleportQueueManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerVerificationHandler {

    private final VerifyDiscordMembers plugin;
    private final TeleportQueueManager teleportQueueManager;

    public PlayerVerificationHandler(VerifyDiscordMembers plugin) {
        this.plugin = plugin;
        this.teleportQueueManager = new TeleportQueueManager(plugin);
    }

    public void handleOnlinePlayer(Player player) {
        plugin.getLogger().info("Player " + player.getName() + " is online, handling verification.");
        executeOnVerify(player.getName());
        player.sendMessage(formatMessage("messages.linked-success", player.getName()));

        if (plugin.getConfig().getBoolean("teleport-to-spawn", true)) {
            Location spawnLocation = player.getWorld().getSpawnLocation();
            player.teleport(spawnLocation);
            player.sendMessage(formatMessage("messages.teleported-online", player.getName()));
        }
    }

    public void handleOfflinePlayer(OfflinePlayer offlinePlayer) {
        plugin.getLogger().info("Player " + offlinePlayer.getName() + " is offline, handling verification.");
        executeOnVerify(offlinePlayer.getName());
        if (plugin.getConfig().getBoolean("teleport-to-spawn", true)) {
            queueTeleportForOfflinePlayer(offlinePlayer);

        }
    }

    private void queueTeleportForOfflinePlayer(OfflinePlayer offlinePlayer) {
        teleportQueueManager.getQueue().set(offlinePlayer.getUniqueId().toString(), true);
        teleportQueueManager.save();
        plugin.getLogger().info("Teleport queued for offline player: " + offlinePlayer.getName());
    }

    private void executeOnVerify(String player) {
        List<String> commands = plugin.getConfig().getStringList("commands");
        for (String command : commands) {
            String formatted = command.replace("{player}", player);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formatted);
            plugin.getLogger().info("Executed \"" + formatted + "\" for player " + player);
        }
    }

    public String formatMessage(String key, String username) {
        String message = plugin.getConfig().getString(key);
        if (message == null) {
            plugin.getLogger().info("The key " + key + " was not found in the configuration file, returned as empty.");
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&',message.replace("{player}", username));
    }
}
