package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.entity.Entity;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BooleanSupplier;

public class AbstractScheduledTask {
    protected final CompletableFuture<Void> completion = new CompletableFuture<>();
    protected volatile boolean cancelled = false;
    protected volatile boolean running = false;
    protected volatile Throwable failure = null;
    private volatile long nextExecutionMillis = -1;

    private final BooleanSupplier until;
    private final BooleanSupplier whileCond;
    private final Entity entity;

    public AbstractScheduledTask(BooleanSupplier until, BooleanSupplier whileCond, Entity entity) {
        this.until = until;
        this.whileCond = whileCond;
        this.entity = entity;
    }

    public Runnable getWrappedRunnable(Runnable action, long periodMillis) {
        return () -> {
            if (cancelled) return;
            if (entity != null && !entity.isValid()) {
                cancel();
                return;
            }
            if (until != null && until.getAsBoolean()) {
                cancel();
                return;
            }
            if (whileCond != null && !whileCond.getAsBoolean()) {
                cancel();
                return;
            }

            running = true;
            try {
                action.run();
                if (periodMillis > 0) {
                    nextExecutionMillis = System.currentTimeMillis() + periodMillis;
                } else {
                    cancel();
                }
            } catch (Throwable t) {
                failure = t;
                cancel();
                completion.completeExceptionally(t);
            }
            running = false;
        };
    }

    public void cancel() {
        if (cancelled) return;
        cancelled = true;
        if (!completion.isDone()) completion.complete(null);
    }

    public boolean isCancelled() { return cancelled; }
    public boolean isRunning() { return running; }

    public Instant nextExecution() {
        return nextExecutionMillis == -1 ? Instant.now() : Instant.ofEpochMilli(nextExecutionMillis);
    }

    public Optional<Throwable> failure() { return Optional.ofNullable(failure); }
    public CompletionStage<Void> completion() { return completion; }
}