package com.github.darksoulq.wit.api;

import com.github.darksoulq.wit.misc.MiningSession;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class ProgressProviders {
    public static final Map<UUID, MiningSession> SESSIONS = new ConcurrentHashMap<>();
    private static final List<BiFunction<Block, Player, Float>> PROVIDERS = new ArrayList<>();

    static {
        register((block, player) -> {
            MiningSession session = SESSIONS.get(player.getUniqueId());
            if (session != null && session.block.getLocation().equals(block.getLocation())) {
                return session.getProgress();
            }
            return -1f;
        });
    }

    public static void register(BiFunction<Block, Player, Float> provider) {
        PROVIDERS.add(provider);
    }

    public static float getProgress(Block block, Player player) {
        for (BiFunction<Block, Player, Float> provider : PROVIDERS) {
            Float progress = provider.apply(block, player);
            if (progress != null && progress >= 0f && progress <= 1f) {
                return progress;
            }
        }
        return 0f;
    }
}