package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class FoliaTaskDispatcher {
    public static ScheduledTask schedule(Plugin plugin, Runnable action, boolean async, Entity entity, Location location, long delayTicks, long periodTicks, long delayMillis, long periodMillis, AbstractScheduledTask abstractTask) {
        Runnable wrapped = abstractTask.getWrappedRunnable(action, periodMillis);
        io.papermc.paper.threadedregions.scheduler.ScheduledTask foliaTask;

        if (async) {
            foliaTask = periodMillis > 0
                ? Bukkit.getAsyncScheduler().runAtFixedRate(plugin, t -> wrapped.run(), delayMillis, periodMillis, TimeUnit.MILLISECONDS)
                : Bukkit.getAsyncScheduler().runDelayed(plugin, t -> wrapped.run(), delayMillis, TimeUnit.MILLISECONDS);
        } else if (entity != null) {
            foliaTask = periodTicks > 0
                ? entity.getScheduler().runAtFixedRate(plugin, t -> wrapped.run(), null, Math.max(1, delayTicks), periodTicks)
                : entity.getScheduler().runDelayed(plugin, t -> wrapped.run(), null, Math.max(1, delayTicks));
        } else if (location != null) {
            foliaTask = periodTicks > 0
                ? Bukkit.getRegionScheduler().runAtFixedRate(plugin, location, t -> wrapped.run(), Math.max(1, delayTicks), periodTicks)
                : Bukkit.getRegionScheduler().runDelayed(plugin, location, t -> wrapped.run(), Math.max(1, delayTicks));
        } else {
            foliaTask = periodTicks > 0
                ? Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, t -> wrapped.run(), Math.max(1, delayTicks), periodTicks)
                : Bukkit.getGlobalRegionScheduler().runDelayed(plugin, t -> wrapped.run(), Math.max(1, delayTicks));
        }

        return new ScheduledTask() {
            @Override public void cancel() { abstractTask.cancel(); if (foliaTask != null) foliaTask.cancel(); }
            @Override public boolean isCancelled() { return abstractTask.isCancelled(); }
            @Override public boolean isRunning() { return abstractTask.isRunning(); }
            @Override public Instant nextExecution() { return abstractTask.nextExecution(); }
            @Override public Optional<Throwable> failure() { return abstractTask.failure(); }
            @Override public CompletionStage<Void> completion() { return abstractTask.completion(); }
        };
    }
}