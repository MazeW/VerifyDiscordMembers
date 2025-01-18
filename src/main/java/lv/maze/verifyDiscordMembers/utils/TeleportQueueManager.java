package lv.maze.verifyDiscordMembers.utils;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class TeleportQueueManager {

    private final File teleportQueueFile;
    private FileConfiguration teleportQueue;

    public TeleportQueueManager(VerifyDiscordMembers plugin) {
        this.teleportQueueFile = new File(plugin.getDataFolder(), "teleportQueue.yml");
        if (!teleportQueueFile.exists()) {
            try {
                boolean makeDir = teleportQueueFile.getParentFile().mkdirs();
                if (!makeDir) {
                    throw new IOException("Could not create directory");
                }
                boolean newFile = teleportQueueFile.createNewFile();
                if (!newFile) {
                    throw new IOException("File not created");
                }
            } catch (IOException e) {
                plugin.getLogger().warning("Failed to create teleportQueue.yml: " + e.getMessage());
            }
        }
        this.teleportQueue = YamlConfiguration.loadConfiguration(teleportQueueFile);
    }

    public boolean isTeleportQueued(UUID uuid) {
        return getQueue().contains(uuid.toString());
    }

    public void removeFromQueue(UUID uuid) {
        teleportQueue.set(uuid.toString(), null);
        save();
    }
    public FileConfiguration getQueue() {
        teleportQueue = YamlConfiguration.loadConfiguration(teleportQueueFile);
        return teleportQueue;
    }

    public void save() {
        try {
            teleportQueue.save(teleportQueueFile);
        } catch (IOException e) {
            getLogger().warning("Error saving to teleportQueue.yml " + e.getMessage());
        }
    }
}
