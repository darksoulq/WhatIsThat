package com.github.darksoulq.wit.misc;

import org.bukkit.block.Block;

public class MiningSession {
    public final Block block;
    public final long startTime;
    public final long durationMs;

    public MiningSession(Block block, long startTime, long durationMs) {
        this.block = block;
        this.startTime = startTime;
        this.durationMs = durationMs;
    }

    public float getProgress() {
        if (durationMs <= 0) return 1f;
        long elapsed = System.currentTimeMillis() - startTime;
        return Math.min(1f, (float) elapsed / durationMs);
    }
}