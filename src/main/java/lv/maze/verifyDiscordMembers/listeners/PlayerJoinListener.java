package lv.maze.verifyDiscordMembers.listeners;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import lv.maze.verifyDiscordMembers.handlers.PlayerVerificationHandler;
import lv.maze.verifyDiscordMembers.utils.TeleportQueueManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final VerifyDiscordMembers plugin;
    private final TeleportQueueManager teleportQueueManager;
    private final PlayerVerificationHandler playerVerificationHandler;

    public PlayerJoinListener(VerifyDiscordMembers plugin) {
        this.plugin = plugin;
        this.teleportQueueManager = new TeleportQueueManager(plugin);
        this.playerVerificationHandler = new PlayerVerificationHandler(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (teleportQueueManager.isTeleportQueued(uuid)) {
            Location spawnLocation = player.getWorld().getSpawnLocation();
            player.teleport(spawnLocation);
            player.sendMessage(playerVerificationHandler.formatMessage("messages.teleported-onJoin", player.getName()));
            teleportQueueManager.removeFromQueue(uuid);
            plugin.getLogger().info("Teleported " + player.getName() + " to spawn.");
        }
    }
}
