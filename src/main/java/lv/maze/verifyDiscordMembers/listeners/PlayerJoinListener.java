package lv.maze.verifyDiscordMembers.listeners;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import lv.maze.verifyDiscordMembers.utils.TeleportQueueManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final VerifyDiscordMembers plugin;
    private final TeleportQueueManager teleportQueueManager;

    public PlayerJoinListener(VerifyDiscordMembers plugin) {
        this.plugin = plugin;
        this.teleportQueueManager = new TeleportQueueManager(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (teleportQueueManager.isTeleportQueued(uuid)) {
            Location spawnLocation = player.getWorld().getSpawnLocation();
            player.teleport(spawnLocation);
            player.sendMessage(Objects.requireNonNull(plugin.getConfig().getString("messages.teleported-onJoin")));
            teleportQueueManager.removeFromQueue(uuid);
            plugin.getLogger().info("Teleported " + player.getName() + " to spawn.");
        }
    }
}
