package com.github.darksoulq.wit.display;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.game.ClientboundBossEventPacket;
import net.minecraft.server.level.ServerBossEvent;
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

        if (bar == null) {
            bar = new ServerBossEvent(vanillaText, BossEvent.BossBarColor.WHITE, BossEvent.BossBarOverlay.PROGRESS);
            bar.setProgress(1.0f);
            playerBossBars.put(player.getUniqueId(), bar);
            ((CraftPlayer) player).getHandle().connection.send(ClientboundBossEventPacket.createAddPacket(bar));
            return;
        }

        if (!bar.getName().equals(vanillaText)) {
            bar.setName(vanillaText);
            ((CraftPlayer) player).getHandle().connection.send(ClientboundBossEventPacket.createUpdateNamePacket(bar));
        }
    }

    @Override
    public void setProgress(Player player, float value) {
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        if (bar != null && bar.getProgress() != value) {
            bar.setProgress(value);
            ((CraftPlayer) player).getHandle().connection.send(ClientboundBossEventPacket.createUpdateProgressPacket(bar));
        }
    }

    @Override
    public void removeBar(Player player) {
        ServerBossEvent bar = playerBossBars.remove(player.getUniqueId());
        if (bar != null) {
            ((CraftPlayer) player).getHandle().connection.send(ClientboundBossEventPacket.createRemovePacket(bar.getId()));
        }
    }

    @Override
    public boolean isEmpty(Player player) {
        ServerBossEvent bar = playerBossBars.get(player.getUniqueId());
        if (bar == null) return true;
        return bar.getName().getString().isEmpty();
    }
}