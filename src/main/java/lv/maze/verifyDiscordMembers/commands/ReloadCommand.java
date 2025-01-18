package lv.maze.verifyDiscordMembers.commands;

import lv.maze.verifyDiscordMembers.VerifyDiscordMembers;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

public class ReloadCommand implements CommandExecutor {

    private final VerifyDiscordMembers plugin;

    public ReloadCommand(VerifyDiscordMembers plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("VerifyDiscordMembers.reload")) {
            return false;
        }

        plugin.reloadConfig();
        sender.sendMessage(MessageFormat.format("{0}[VerifyDiscordMembers] {1}Configuration reloaded successfully!", ChatColor.GREEN, ChatColor.GRAY));
        plugin.getLogger().info("Configuration reloaded by " + sender.getName());
        return true;
    }
}
