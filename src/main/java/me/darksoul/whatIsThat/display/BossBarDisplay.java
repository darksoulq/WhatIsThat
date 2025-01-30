package me.darksoul.whatIsThat.display;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBarDisplay extends InfoDisplay {
    private static final Map<Player, BossBar> playerBossBars = new HashMap<>();

    public BossBarDisplay() {
        super("bossbar");
    }

    @Override
    public void setBar(Player player, String text) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(text, BarColor.WHITE, BarStyle.SOLID);
            bossBar.addPlayer(player);
            BossBarDisplay.playerBossBars.put(player, bossBar);
        }

        if (text == null || text.isEmpty()) {
            bossBar.setVisible(false);
        } else if (!text.equals(bossBar.getTitle())) {
            bossBar.setTitle(text);
            bossBar.setVisible(true);
        }
    }

    @Override
    public void removeBar(Player player) {
        BossBar bossBar = BossBarDisplay.playerBossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
    }
}
