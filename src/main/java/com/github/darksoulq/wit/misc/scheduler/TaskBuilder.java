package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.BooleanSupplier;

public class TaskBuilder {
    private final Scheduler scheduler;
    private final Runnable action;
    private boolean async = false;
    private Entity entity = null;
    private Location location = null;
    private long delay = 0;
    private Clock delayClock = Clock.TICKS;
    private BooleanSupplier untilCondition = null;
    private BooleanSupplier whileCondition = null;

    public TaskBuilder(Scheduler scheduler, Runnable action) {
        this.scheduler = scheduler;
        this.action = action;
    }

    public TaskBuilder sync() {
        this.async = false;
        return this;
    }

    public TaskBuilder async() {
        this.async = true;
        return this;
    }

    public TaskBuilder entity(Entity entity) {
        this.entity = entity;
        return this;
    }

    public TaskBuilder region(Location location) {
        this.location = location;
        return this;
    }

    public TaskBuilder global() {
        this.entity = null;
        this.location = null;
        return this;
    }

    public TaskBuilder after(long time, TimeUnit unit, Clock clock) {
        this.delayClock = clock;
        this.delay = clock == Clock.REALTIME ? unit.toMillis(time) : unit.toTicks(time);
        if (clock == Clock.REALTIME) this.async = true;
        return this;
    }

    public TaskBuilder after(long time, Clock clock) {
        return after(time, clock.unit(), clock);
    }

    public TaskBuilder repeatUntil(BooleanSupplier condition) {
        this.untilCondition = condition;
        return this;
    }

    public TaskBuilder repeatWhile(BooleanSupplier condition) {
        this.whileCondition = condition;
        return this;
    }

    public ScheduledTask once() {
        return scheduler.getDispatcher().dispatch(action, async, entity, location, delay, delayClock, 0, Clock.TICKS, untilCondition, whileCondition);
    }

    public ScheduledTask repeatEvery(long time, TimeUnit unit, Clock clock) {
        long period = clock == Clock.REALTIME ? unit.toMillis(time) : unit.toTicks(time);
        if (clock == Clock.REALTIME) this.async = true;
        return scheduler.getDispatcher().dispatch(action, async, entity, location, delay, delayClock, period, clock, untilCondition, whileCondition);
    }
    
    public ScheduledTask repeatEvery(long time, Clock clock) {
        return repeatEvery(time, clock.unit(), clock);
    }
}