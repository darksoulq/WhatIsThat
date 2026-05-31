package com.github.darksoulq.wit.display;

import com.github.darksoulq.wit.api.Info;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarDisplay extends InfoDisplay {
    public static final Map<UUID, ServerBossEvent> playerBossBars = new ConcurrentHashMap<>();

    public BossBarDisplay() {
        super("bossbar");
    }

    private BossEvent.BossBarColor mapToBossBarColor(TextColor color) {
        if (color == null) return BossEvent.BossBarColor.WHITE;

        int hex = color.value();
        if (hex == NamedTextColor.RED.value() || hex == NamedTextColor.DARK_RED.value()) return BossEvent.BossBarColor.RED;
        if (hex == NamedTextColor.BLUE.value() || hex == NamedTextColor.DARK_BLUE.value() || hex == NamedTextColor.AQUA.value()) return BossEvent.BossBarColor.BLUE;
        if (hex == NamedTextColor.GREEN.value() || hex == NamedTextColor.DARK_GREEN.value()) return BossEvent.BossBarColor.GREEN;
        if (hex == NamedTextColor.YELLOW.value() || hex == NamedTextColor.GOLD.value()) return BossEvent.BossBarColor.YELLOW;
        if (hex == NamedTextColor.LIGHT_PURPLE.value() || hex == NamedTextColor.DARK_PURPLE.value()) return BossEvent.BossBarColor.PURPLE;

        return BossEvent.BossBarColor.WHITE;
    }

    @Override
    public void setBar(Player player, Info info) {
        Component text = info.getCombined();
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        net.minecraft.network.chat.Component vanillaText = PaperAdventure.asVanilla(text);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        if (bar == null) {
            bar = new ServerBossEvent(UUID.randomUUID(), vanillaText, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
            bar.setProgress(1.0f);
            playerBossBars.put(player.getUniqueId(), bar);

            bar.addPlayer(serverPlayer);
            return;
        }

        bar.setName(vanillaText);
    }

    @Override
    public void setProgress(Player player, float value) {
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        if (bar != null) {
            bar.setProgress(value);
        }
    }

    @Override
    public void setColor(Player player, TextColor color) {
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        if (bar != null) {
            BossEvent.BossBarColor finalColor = mapToBossBarColor(color);
            if (bar.getColor() != finalColor) {
                bar.setColor(finalColor);
            }
        }
    }

    @Override
    public void removeBar(Player player) {
        ServerBossEvent bar = playerBossBars.remove(player.getUniqueId());
        if (bar != null) {
            ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            bar.removePlayer(serverPlayer);
        }
    }

    @Override
    public boolean isEmpty(Player player) {
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        if (bar == null) return true;
        return bar.getName().getString().isEmpty();
    }
}