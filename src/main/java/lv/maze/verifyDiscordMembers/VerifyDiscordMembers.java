package lv.maze.verifyDiscordMembers;

import lv.maze.verifyDiscordMembers.commands.ReloadCommand;
import lv.maze.verifyDiscordMembers.listeners.DiscordLinkListener;
import lv.maze.verifyDiscordMembers.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class VerifyDiscordMembers extends JavaPlugin {


    @Override
    public void onEnable() {
        if (!checkDependencies()) return;

        saveDefaultConfig();
        registerListeners();
        registerCommands();
        getLogger().info("VerifyDiscordMembers plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("VerifyDiscordMembers disabled!");
    }

    private boolean checkDependencies() {
        if (Bukkit.getPluginManager().getPlugin("EssentialsDiscordLink") == null || !Bukkit.getPluginManager().isPluginEnabled("EssentialsDiscordLink")) {
            getLogger().severe("EssentialsDiscordLink is disabled or missing. Make sure it is properly configured!");
            Bukkit.getPluginManager().disablePlugin(this);
            return false;
        }
        return true;
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new DiscordLinkListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("reload")).setExecutor(new ReloadCommand(this));
    }
}
