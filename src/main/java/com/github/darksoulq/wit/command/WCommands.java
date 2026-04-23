package com.github.darksoulq.wit.command;

import com.github.darksoulq.wit.Handlers;
import com.github.darksoulq.wit.Information;
import com.github.darksoulq.wit.WIT;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.API;
import com.github.darksoulq.wit.api.Info;
import com.github.darksoulq.wit.compatibility.MinecraftCompat;
import com.github.darksoulq.wit.display.DisplayManager;
import com.github.darksoulq.wit.misc.ItemGroups;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("wit")
            .then(Commands.literal("toggle")
                .requires(sender -> sender.getSender().hasPermission("wit.default"))
                .executes(WCommands::toggleExecutor))
            .then(Commands.literal("type")
                .then(Commands.argument("type", StringArgumentType.string())
                    .requires(sender -> sender.getSender().hasPermission("wit.default"))
                    .suggests(WCommands::changeTypeSuggester)
                    .executes(WCommands::changeTypeExecutor)))
            .then(Commands.literal("progress")
                .then(Commands.argument("mode", StringArgumentType.string())
                    .requires(sender -> sender.getSender().hasPermission("wit.default"))
                    .suggests(WCommands::progressModeSuggester)
                    .executes(WCommands::changeProgressModeExecutor)))
            .then(Commands.literal("reload")
                .requires(sender -> sender.getSender().hasPermission("wit.reload"))
                .executes(WCommands::reloadExecutor));
    }

    private static int toggleExecutor(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only a player can run this command");
            return Command.SINGLE_SUCCESS;
        }

        WITListener.PlayerSettings settings = WITListener.getSettings(player);
        if (settings.disabled) {
            enableBar(player, settings);
        } else {
            disableBar(player, settings);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int changeTypeExecutor(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage("Only players can execute this command");
            return Command.SINGLE_SUCCESS;
        }

        String type = StringArgumentType.getString(ctx, "type");
        setType(player, type);
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> changeTypeSuggester(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        DisplayManager.getDisplays().keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int changeProgressModeExecutor(CommandContext<CommandSourceStack> ctx) {
        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage("Only players can execute this command");
            return Command.SINGLE_SUCCESS;
        }

        String modeStr = StringArgumentType.getString(ctx, "mode").toUpperCase();
        WITListener.ActionBarProgressMode mode;

        try {
            mode = WITListener.ActionBarProgressMode.valueOf(modeStr);
        } catch (IllegalArgumentException e) {
            player.sendMessage("Invalid mode. Use OFF, BAR, or PERCENT.");
            return Command.SINGLE_SUCCESS;
        }

        WITListener.PlayerSettings settings = WITListener.getSettings(player);
        settings.abProgressMode = mode;

        Bukkit.getScheduler().runTaskAsynchronously(WIT.instance(), () -> {
            File playerFile = new File(WITListener.getPrefFolder(), player.getName() + ".yml");
            try {
                if (!playerFile.exists()) playerFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
                config.set("abProgressMode", mode.name());
                config.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        player.sendMessage("Action bar progress mode set to: " + mode.name());
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> progressModeSuggester(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        for (WITListener.ActionBarProgressMode mode : WITListener.ActionBarProgressMode.values()) {
            builder.suggest(mode.name());
        }
        return builder.buildFuture();
    }

    private static int reloadExecutor(CommandContext<CommandSourceStack> ctx) {
        WITListener.setup();
        MinecraftCompat.setup();
        Information.reloadValuesFile();
        Handlers.setup();
        ItemGroups.reload();
        API.fireReload();
        ctx.getSource().getSender().sendMessage("WIT config reloaded.");
        return Command.SINGLE_SUCCESS;
    }

    private static void setType(Player player, String type) {
        WITListener.PlayerSettings settings = WITListener.getSettings(player);
        DisplayManager.removeBar(player, settings.type);
        settings.type = type;

        Bukkit.getScheduler().runTaskAsynchronously(WIT.instance(), () -> {
            File playerFile = new File(WITListener.getPrefFolder(), player.getName() + ".yml");
            try {
                if (!playerFile.exists()) playerFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
                config.set("type", type);
                config.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        player.sendMessage("WAILA display type set to: " + type);
    }

    private static void disableBar(Player player, WITListener.PlayerSettings settings) {
        settings.disabled = true;
        WITListener.removePlayer(player);
        DisplayManager.removeBar(player, settings.type);
        WITListener.setLookingAt(player, null);

        Bukkit.getScheduler().runTaskAsynchronously(WIT.instance(), () -> {
            File playerFile = new File(WITListener.getPrefFolder(), player.getName() + ".yml");
            try {
                if (!playerFile.exists()) playerFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
                config.set("disableWAILA", true);
                config.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        player.sendMessage("WAILA bar disabled.");
    }

    private static void enableBar(Player player, WITListener.PlayerSettings settings) {
        settings.disabled = false;

        Bukkit.getScheduler().runTaskAsynchronously(WIT.instance(), () -> {
            File playerFile = new File(WITListener.getPrefFolder(), player.getName() + ".yml");
            try {
                if (!playerFile.exists()) playerFile.createNewFile();
                YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
                config.set("disableWAILA", false);
                config.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (!WITListener.DISABLED_WORLDS.contains(player.getWorld().getName())) {
            WITListener.addPlayer(player);
            DisplayManager.setBar(player, new Info());
        }
        player.sendMessage("WAILA bar enabled.");
    }
}