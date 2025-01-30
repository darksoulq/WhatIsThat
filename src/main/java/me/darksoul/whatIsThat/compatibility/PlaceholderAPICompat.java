package me.darksoul.whatIsThat.compatibility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPICompat {
    private static boolean isWITPAPIInstalled;

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
                    return WAILAListener.getLookingAt(player.getPlayer());
                } else if (identifier.equalsIgnoreCase("looking_at_prefix")) {
                    return WAILAListener.getLookingAtPrefix(player.getPlayer());
                } else if (identifier.equalsIgnoreCase("looking_at_suffix")) {
                    return WAILAListener.getLookingAtSuffix(player.getPlayer());
                } else if (identifier.equalsIgnoreCase("looking_at_info")) {
                    return WAILAListener.getLookingAtInfo(player.getPlayer());
                }
                return null;
            }
        }.register();
    }
    public static boolean checkWITPAPI() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
        isWITPAPIInstalled = pl != null && pl.isEnabled();
        if (isWITPAPIInstalled) {
            setup();
        }
        return isWITPAPIInstalled;
    }

    public static boolean getIsWITPAPIInstalled() {
        return isWITPAPIInstalled;
    }
}
