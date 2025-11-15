package com.github.darksoulq.wit.display;

import com.github.darksoulq.wit.WITListener;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WAILAManager {
    private static final Map<String, InfoDisplay> displays = new HashMap<>();

    public static void addDisplay(InfoDisplay display) {
        displays.put(display.getId(), display);
    }

    public static void setBar(Player player, Component text) {
        if (!WITListener.isHidden()) {
            if (!displays.isEmpty() && displays.containsKey(WITListener.getPlayerConfig(player).getString("type"))) {
                displays.get(WITListener.getPlayerConfig(player).getString("type")).setBar(player, text);
            }
        } else {
            removeBar(player, WITListener.getPlayerConfig(player).getString("type"));
        }
    }

    public static void setProgress(Player player, float value) {
        InfoDisplay display = displays.get(WITListener.getPlayerConfig(player).getString("type"));
        if (display != null) {
            display.setProgress(player, value);
        }
    }

    public static void removeBar(Player player, String type) {
        if (!displays.isEmpty() && displays.containsKey(type)) {
            displays.get(type).removeBar(player);
        }
    }

    public static Map<String, InfoDisplay> getDisplays() {
        return displays;
    }
}
