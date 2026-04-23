package com.github.darksoulq.wit.display;

import com.github.darksoulq.wit.WITListener;
import com.github.darksoulq.wit.api.Info;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DisplayManager {
    private static final Map<String, InfoDisplay> displays = new HashMap<>();

    public static void addDisplay(InfoDisplay display) {
        displays.put(display.getId(), display);
    }

    public static void setBar(Player player, Info text) {
        String type = WITListener.getSettings(player).type;
        if (!WITListener.isHidden()) {
            InfoDisplay display = displays.get(type);
            if (display != null) {
                display.setBar(player, text);
            }
        } else {
            removeBar(player, type);
        }
    }

    public static void setProgress(Player player, float value) {
        InfoDisplay display = displays.get(WITListener.getSettings(player).type);
        if (display != null) {
            display.setProgress(player, value);
        }
    }

    public static void removeBar(Player player, String type) {
        InfoDisplay display = displays.get(type);
        if (display != null) {
            display.removeBar(player);
        }
    }

    public static Map<String, InfoDisplay> getDisplays() {
        return displays;
    }
}