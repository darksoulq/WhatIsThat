package me.darksoul.whatIsThat.display;

import me.darksoul.whatIsThat.WAILAListener;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WAILAManager {
    private static final Map<String, InfoDisplay> displays = new HashMap<>();

    public static void addDisplay(InfoDisplay display) {
        displays.put(display.getId(), display);
    }

    public static void setBar(Player player, String text) {
        if (!WAILAListener.isHidden()) {
            if (!displays.isEmpty() && displays.containsKey(WAILAListener.getPlayerConfig(player).getString("type"))) {
                displays.get(WAILAListener.getPlayerConfig(player).getString("type")).setBar(player, text);
            }
        } else {
            removeBar(player, WAILAListener.getPlayerConfig(player).getString("type"));
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
