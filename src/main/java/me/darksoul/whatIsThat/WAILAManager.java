package me.darksoul.whatIsThat;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WAILAManager {
    private static final Map<Player, BossBar> playerBossBars = new HashMap<>();

    public static void setBar(Player player, String type, String text) {
        if (!WAILAListener.isHidden()) {
            if ("bossbar".equalsIgnoreCase(type)) {
                setBossBar(player, text);
            } else if ("actionbar".equalsIgnoreCase(type)) {
                setActionBar(player, text);
            }
        } else {
            removeBar(player, type);
        }
    }
    private static void setBossBar(Player player, String text) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
            bossBar.addPlayer(player);
            playerBossBars.put(player, bossBar);
        }

        if (text == null || text.isEmpty()) {
            bossBar.setVisible(false);
        } else if (!text.equals(bossBar.getTitle())) {
            bossBar.setTitle(text);
            bossBar.setVisible(true);
        }
    }
    private static void setActionBar(Player player, String text) {
        if (text == null || text.isEmpty()) {
            player.sendActionBar(Component.text(""));
        } else {
            player.sendActionBar(Component.text(text));
        }
    }

    public static void removeBar(Player player, String type) {
        if ("bossbar".equalsIgnoreCase(type)) {
            removeBossBar(player);
        } else if ("actionbar".equalsIgnoreCase(type)) {
            removeActionBar(player);
        }
    }
    private static void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }
    private static void removeActionBar(Player player) {
        player.sendActionBar(Component.text(""));
    }
}
