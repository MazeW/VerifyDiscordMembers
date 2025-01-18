package lv.maze.verifyDiscordMembers.listeners;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import lv.maze.verifyDiscordMembers.handlers.PlayerVerificationHandler;
import net.ess3.api.IUser;
import net.essentialsx.api.v2.events.discordlink.DiscordLinkStatusChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class DiscordLinkListener implements Listener {

    private final VerifyDiscordMembers plugin;
    private final PlayerVerificationHandler handler;

    public DiscordLinkListener(VerifyDiscordMembers plugin) {
        this.plugin = plugin;
        this.handler = new PlayerVerificationHandler(plugin);
    }

    @EventHandler
    public void onDiscordLinkStatusChange(final DiscordLinkStatusChangeEvent event) {
        if (!event.isLinked()) {
            return; // Handle unlink logic if needed
        }

        IUser user = event.getUser();
        if (user == null) {
            plugin.getLogger().warning("Could not retrieve user for linked account.");
            return;
        }

        UUID uuid = user.getUUID();
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            handler.handleOnlinePlayer(player);
        } else {
            Bukkit.getScheduler().runTask(plugin, () -> {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                if (!offlinePlayer.hasPlayedBefore()) {
                    plugin.getLogger().warning("User (" + uuid + ") could not be found! Their account linking was not complete.");
                } else {
                    handler.handleOfflinePlayer(offlinePlayer);
                }
            });
        }
    }
}
