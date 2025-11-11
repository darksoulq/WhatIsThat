package me.darksoul.wit.display;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class BossBarDisplay extends InfoDisplay {
    public static final Map<Player, BossBar> playerBossBars = new HashMap<>();

    public BossBarDisplay() {
        super("bossbar");
}

    @Override
    public void setBar(Player player, Component text) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar == null) {
            bossBar = BossBar.bossBar(text, 1.0f, BossBar.Color.WHITE, BossBar.Overlay.PROGRESS);
            bossBar.addViewer(player);
            BossBarDisplay.playerBossBars.put(player, bossBar);
        }

        if (text != null && !text.equals(bossBar.name())) {
            bossBar.name(text);
            if (!bossBar.viewers().iterator().hasNext()) {
                bossBar.addViewer(player);
            }
        }
    }

    @Override
    public void setProgress(Player player, float value) {
        BossBar bossBar = playerBossBars.get(player);
        if (bossBar != null) {
            bossBar.progress(value);
        }
    }

    @Override
    public void removeBar(Player player) {
        BossBar bossBar = BossBarDisplay.playerBossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeViewer(player);
        }
    }

    @Override
    public boolean isEmpty(Player player) {
        if (!playerBossBars.containsKey(player)) return true;
        return ((TextComponent) playerBossBars.get(player).name()).content().isEmpty();
    }
}
