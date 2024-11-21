package me.darksoul.whatIsThat.commands;

import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WAILAManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WITCommand implements CommandExecutor, TabCompleter {

    private static final String COMMAND_HELP = "Usage: /wit [disable|enable]";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            sender.sendMessage(COMMAND_HELP);
            return true;
        }

        if ("disable".equalsIgnoreCase(args[0])) {
            disableBossBar(player);
        } else if ("enable".equalsIgnoreCase(args[0])) {
            enableBossBar(player);
        } else {
            sender.sendMessage(COMMAND_HELP);
            return false;
        }

        return true;
    }

    private void disableBossBar(Player player) {
        File playerFile = new File( WAILAListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("disableBossBar", true);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
            return;
        }

        WAILAManager.removeBossBar(player);
        player.sendMessage("Boss bar disabled.");
    }

    private void enableBossBar(Player player) {
        File playerFile = new File(WAILAListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("disableBossBar", false);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
            return;
        }

        WAILAManager.createBossBar(player);
        player.sendMessage("Boss bar enabled.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("disable", "enable"), suggestions);
        }

        return suggestions;
    }
}
