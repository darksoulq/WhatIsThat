package com.github.darksoulq.wit.misc.scheduler;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.concurrent.TimeUnit;

public class RealtimeTaskDispatcher {
    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(4);

    public static ScheduledTask schedule(Runnable action, long delayMillis, long periodMillis, AbstractScheduledTask abstractTask) {
        Runnable wrapped = abstractTask.getWrappedRunnable(action, periodMillis);
        ScheduledFuture<?> future = periodMillis > 0
            ? EXECUTOR.scheduleAtFixedRate(wrapped, delayMillis, periodMillis, TimeUnit.MILLISECONDS)
            : EXECUTOR.schedule(wrapped, delayMillis, TimeUnit.MILLISECONDS);

        return new ScheduledTask() {
            @Override public void cancel() { abstractTask.cancel(); future.cancel(false); }
            @Override public boolean isCancelled() { return abstractTask.isCancelled(); }
            @Override public boolean isRunning() { return abstractTask.isRunning(); }
            @Override public Instant nextExecution() { return abstractTask.nextExecution(); }
            @Override public Optional<Throwable> failure() { return abstractTask.failure(); }
            @Override public CompletionStage<Void> completion() { return abstractTask.completion(); }
        };
    }
}