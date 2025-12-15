package com.github.darksoulq.wit.compatibility;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.WIT;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
                    Component name = WITListener.getLookingAt(player.getPlayer()).getName();
                    if (name == null) return "";
                    return MiniMessage.miniMessage().serialize(name);
                } else if (identifier.equalsIgnoreCase("looking_at_prefix")) {
                    Component prefix = WITListener.getLookingAt(player.getPlayer()).getPrefix();
                    if (prefix == null) return "";
                    return MiniMessage.miniMessage().serialize(prefix);
                } else if (identifier.equalsIgnoreCase("looking_at_suffix")) {
                    Component suffix = WITListener.getLookingAt(player.getPlayer()).getSuffix();
                    if (suffix == null) return "";;
                    return MiniMessage.miniMessage().serialize(suffix);
                } else if (identifier.equalsIgnoreCase("looking_at_info")) {
                    Component combined = WITListener.getLookingAt(player.getPlayer()).getCombined();
                    if (combined == null) return "";
                    return MiniMessage.miniMessage().serialize(combined);
                } else if (identifier.equalsIgnoreCase("info_type")) {
                    return WITListener.getPlayerConfig(player.getPlayer()).getString("type");
                } else if (identifier.equalsIgnoreCase("info_state")) {
                    return "false".equalsIgnoreCase(WITListener.getPlayerConfig(player.getPlayer()).getString("disableWAILA")) ? "enabled" : "disabled";
                }
                return null;
            }
        }.register();
    }

    private String getContentFromComponent(Component component) {
        if (component instanceof TextComponent text) {
            return text.content();
        }

        return Component.text().append(component).build().content();
    }
    public static void checkWITPAPI() {
        Plugin pl = WIT.instance().getServer().getPluginManager().getPlugin("PlaceholderAPI");
        isInstalled = pl != null && pl.isEnabled();
        if (isInstalled) {
            setup();
        }
    }

    public static boolean isInstalled() {
        return isInstalled;
    }
}
