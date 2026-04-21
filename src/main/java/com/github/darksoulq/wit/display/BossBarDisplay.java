package com.github.darksoulq.wit.display;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
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

    @Override
    public void setBar(Player player, Component text) {
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