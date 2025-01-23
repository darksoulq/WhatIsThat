package me.darksoul.whatIsThat.commands;

import me.darksoul.whatIsThat.*;
import me.darksoul.whatIsThat.misc.ConfigUtils;
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
import java.util.stream.Stream;

public class WITCommand implements CommandExecutor, TabCompleter {

    private static final String COMMAND_HELP = "Usage: /wit [type <bossbar|actionbar>|disable|enable]";

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
        } else if ("type".equalsIgnoreCase(args[0])) {
            if (args.length < 2 || Stream.of("bossbar", "actionbar").noneMatch(args[1]::equalsIgnoreCase)) {
                sender.sendMessage(COMMAND_HELP);
                return false;
            }
            if (args[1].equalsIgnoreCase("actionbar")) {
                WAILAManager.removeBar(player, "bossbar");
            } else {
                WAILAManager.removeBar(player, "actionbar");
            }
            setType(player, args[1]);
        } else if ("reload".equalsIgnoreCase(args[0])) {
            if (player.hasPermission("wit.reload")) {
                WAILAListener.setup();
                ConfigUtils.loadVanillaTranslation();
                Information.reloadValuesFile();
                Handlers.setup();
                WhatIsThat.resetHooks();
                sender.sendMessage("§2WIT config reloaded.");
            } else {
                sender.sendMessage("§cYou do not have permission to run this command.");
            }
        } else {
            sender.sendMessage(COMMAND_HELP);
            return false;
        }

        return true;
    }

    private void disableBossBar(Player player) {
        File playerFile = new File(WAILAListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("disableWAILA", true);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
            return;
        }

        WAILAListener.removePlayer(player);
        WAILAManager.removeBar(player, "bossbar");
        player.sendMessage("WAILA bar disabled.");
    }
    private void setType(Player player, String type) {
        File playerFile = new File(WAILAListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("type", type);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
        }
    }
    private void enableBossBar(Player player) {
        File playerFile = new File(WAILAListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            config.set("disableWAILA", false);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
            return;
        }

        WAILAListener.addPlayer(player);
        WAILAManager.setBar(player, "bossbar", "");
        player.sendMessage("WAILA bar enabled.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], List.of("disable", "enable", "type", "reload"), suggestions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("type")) {
                StringUtil.copyPartialMatches(args[1], List.of("bossbar", "actionbar"), suggestions);
            }
        }

        return suggestions;
    }
}
