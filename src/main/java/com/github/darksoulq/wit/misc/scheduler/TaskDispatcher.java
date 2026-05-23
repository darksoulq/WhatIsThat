package com.github.darksoulq.wit.misc.scheduler;

import com.github.darksoulq.wit.misc.regional.RegionalCollections;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

import java.util.function.BooleanSupplier;

public class TaskDispatcher {
    private final Plugin plugin;

    public TaskDispatcher(Plugin plugin) {
        this.plugin = plugin;
    }

    public ScheduledTask dispatch(Runnable action, boolean async, Entity entity, Location location, long delay, Clock delayClock, long period, Clock periodClock, BooleanSupplier until, BooleanSupplier whileCond) {
        boolean isAsync = async || delayClock == Clock.REALTIME || periodClock == Clock.REALTIME;
        AbstractScheduledTask abstractTask = new AbstractScheduledTask(until, whileCond, entity);

        long delayTicks = delayClock == Clock.REALTIME ? delay / 50 : delay;
        long periodTicks = periodClock == Clock.REALTIME ? period / 50 : period;
        long delayMillis = delayClock == Clock.REALTIME ? delay : delay * 50;
        long periodMillis = periodClock == Clock.REALTIME ? period : period * 50;

        if (RegionalCollections.IS_FOLIA) {
            return FoliaTaskDispatcher.schedule(plugin, action, isAsync, entity, location, delayTicks, periodTicks, delayMillis, periodMillis, abstractTask);
        } else if (isAsync) {
            return RealtimeTaskDispatcher.schedule(abstractTask.getWrappedRunnable(action, periodMillis), delayMillis, periodMillis, abstractTask);
        } else {
            return BukkitTaskDispatcher.schedule(plugin, action, false, delayTicks, periodTicks, abstractTask);
        }
    }
}