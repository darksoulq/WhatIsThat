package me.darksoul.whatIsThat.compatibility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.darksoul.whatIsThat.WAILAListener;
import me.darksoul.whatIsThat.WhatIsThat;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class WITPAPI extends PlaceholderExpansion {
    private static boolean isWITPAPIInstalled;

    public static boolean checkWITPAPI() {
        Plugin pl = WhatIsThat.getInstance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
        isWITPAPIInstalled = pl != null && pl.isEnabled();
        return isWITPAPIInstalled;
    }

    public static boolean getIsWITPAPIInstalled() {
        return isWITPAPIInstalled;
    }

    @Override
    public String getIdentifier() {
        return "wit";
    }
    @Override
    public String getAuthor() {
        return "DarkSoul";
    }
    @Override
    public String getVersion() {
        return "1.4.4";
    }
    @Override
    public boolean persist() {
        return true;
    }
    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        if (player.getPlayer() == null) {
            return "";
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
}
