package me.darksoul.whatIsThat;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class WAILAManager {
    private static final Map<Player, BossBar> playerBossBars = new HashMap<>();

    public static void createBossBar(Player player) {
        if (playerBossBars.containsKey(player)) return;

        BossBar bossBar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);
        bossBar.setVisible(false);
        bossBar.addPlayer(player);
        playerBossBars.put(player, bossBar);
    }

    public static void removeBossBar(Player player) {
        BossBar bossBar = playerBossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }

    public static void updateBossBar(Player player, String text) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar == null) {
            return;
        };

        if (text == null || text.isEmpty()) {
            bossBar.setVisible(false);
        } else {
            bossBar.setTitle(text);
            bossBar.setVisible(true);
        }
    }
}
