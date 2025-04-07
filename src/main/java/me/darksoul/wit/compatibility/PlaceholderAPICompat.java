package me.darksoul.wit.compatibility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.darksoul.wit.WITListener;
import me.darksoul.wit.WIT;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPICompat {
    private static boolean isInstalled;

    private static void setup() {
        new PlaceholderExpansion() {
            @Override
            public @NotNull String getIdentifier() {
                return "wit";
            }
            @Override
            public @NotNull String getAuthor() {
                return "DarkSoul";
            }
            @Override
            public @NotNull String getVersion() {
                return "1.4.4";
            }
            @Override
            public boolean persist() {
                return true;
            }
            @Override
            public String onRequest(OfflinePlayer player, @NotNull String identifier) {
                if (player.getPlayer() == null) {
                    return null;
                }
                if (identifier.equalsIgnoreCase("looking_at")) {
                    return ((TextComponent) WITListener.getLookingAt(player.getPlayer())).content();
                } else if (identifier.equalsIgnoreCase("looking_at_prefix")) {
                    return WITListener.getLookingAtPrefix(player.getPlayer());
                } else if (identifier.equalsIgnoreCase("looking_at_suffix")) {
                    return WITListener.getLookingAtSuffix(player.getPlayer());
                } else if (identifier.equalsIgnoreCase("looking_at_info")) {
                    return WITListener.getLookingAtInfo(player.getPlayer());
                }
                return null;
            }
        }.register();
    }
    public static void checkWITPAPI() {
        Plugin pl = WIT.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
        }
    }

    public static boolean isInstalled() {
        return isInstalled;
    }
}
