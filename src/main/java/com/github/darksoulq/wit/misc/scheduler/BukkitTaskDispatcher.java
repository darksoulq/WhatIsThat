package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class BukkitTaskDispatcher {
    public static ScheduledTask schedule(Plugin plugin, Runnable action, boolean async, long delayTicks, long periodTicks, AbstractScheduledTask abstractTask) {
        Runnable wrapped = abstractTask.getWrappedRunnable(action, periodTicks > 0 ? periodTicks * 50 : 0);
        org.bukkit.scheduler.BukkitTask bukkitTask = periodTicks > 0
            ? (async ? Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, wrapped, delayTicks, periodTicks)
            : Bukkit.getScheduler().runTaskTimer(plugin, wrapped, Math.max(1, delayTicks), periodTicks))
            : (async ? Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, wrapped, delayTicks)
            : Bukkit.getScheduler().runTaskLater(plugin, wrapped, Math.max(1, delayTicks)));

        return new ScheduledTask() {
            @Override public void cancel() { abstractTask.cancel(); bukkitTask.cancel(); }
            @Override public boolean isCancelled() { return abstractTask.isCancelled(); }
            @Override public boolean isRunning() { return abstractTask.isRunning(); }
            @Override public Instant nextExecution() { return abstractTask.nextExecution(); }
            @Override public Optional<Throwable> failure() { return abstractTask.failure(); }
            @Override public CompletionStage<Void> completion() { return abstractTask.completion(); }
        };
    }
}