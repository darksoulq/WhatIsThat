package me.darksoul.wit.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.darksoul.wit.Handlers;
import me.darksoul.wit.Information;
import me.darksoul.wit.WIT;
import me.darksoul.wit.WITListener;
import me.darksoul.wit.api.API;
import me.darksoul.wit.display.InfoDisplay;
import me.darksoul.wit.display.WAILAManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class WCommands {
    public static LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("wit")
                .then(Commands.literal("toggle")
                        .requires(sender -> sender.getSender().hasPermission("wit.default"))
                        .executes(WCommands::toggleExecutor)
                )
                .then(Commands.literal("type")
                        .then(Commands.argument("type", StringArgumentType.string())
                                .requires(sender -> sender.getSender().hasPermission("wit.default"))
                                .suggests(WCommands::changeTypeSuggester)
                                .executes(WCommands::changeTypeExecutor))
                )
                .then(Commands.literal("reload")
                        .requires(sender -> sender.getSender().hasPermission("wit.reload"))
                        .executes(WCommands::reloadExecutor));
    }


    private static int toggleExecutor(CommandContext<CommandSourceStack> ctx) {
        CommandSender sender = ctx.getSource().getSender();
        Entity executor = ctx.getSource().getExecutor();

        if (!(executor instanceof Player) && !(sender == executor)) {
            sender.sendMessage("Only a player can run this command");
            return Command.SINGLE_SUCCESS;
        }

        if (WITListener.getPlayerConfig((Player) sender).getBoolean("disableWAILA")) {
            enableBar((Player) sender);
        } else {
            disableBar((Player) sender);
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int changeTypeExecutor(CommandContext<CommandSourceStack> ctx) {
        String type = StringArgumentType.getString(ctx, "type");

        if (!(ctx.getSource().getExecutor() instanceof Player)) {
            ctx.getSource().getSender().sendMessage("Only players can execute this command");
            return Command.SINGLE_SUCCESS;
        }
        if (ctx.getSource().getSender() == ctx.getSource().getExecutor()) {
            setType((Player) ctx.getSource().getSender(), type);
            return Command.SINGLE_SUCCESS;
        }
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> changeTypeSuggester(final CommandContext<CommandSourceStack> ctx,
                                                                      final SuggestionsBuilder builder) {
        WAILAManager.getDisplays().keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static int reloadExecutor(CommandContext<CommandSourceStack> ctx) {
        WITListener.setup();
        Information.reloadValuesFile();
        Handlers.setup();
        API.fireReload();
        ctx.getSource().getSender().sendMessage("§2WIT config reloaded.");
        return Command.SINGLE_SUCCESS;
    }

    private static void setType(Player player, String type) {
        File playerFile = new File(WITListener.getPrefFolder() + "/" + player.getName() + ".yml");
        try {
            if (!playerFile.exists()) {
                playerFile.createNewFile();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);
            WAILAManager.removeBar(player, config.getString("type"));
            config.set("type", type);
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred while saving your settings.");
        }
    }

    private static void disableBar(Player player) {
        File playerFile = new File(WITListener.getPrefFolder() + "/" + player.getName() + ".yml");
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

        WITListener.removePlayer(player);
        WAILAManager.removeBar(player, WITListener.getPlayerConfig(player).getString("type"));
        player.sendMessage("WAILA bar disabled.");
    }

    private static void enableBar(Player player) {
        File playerFile = new File(WITListener.getPrefFolder() + "/" + player.getName() + ".yml");
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

        WITListener.addPlayer(player);
        WAILAManager.setBar(player, Component.text(""));
        player.sendMessage("WAILA bar enabled.");
    }
}
