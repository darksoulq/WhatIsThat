package com.github.darksoulq.wit.misc.scheduler;

import java.util.LinkedList;
import java.util.Queue;

public class SequenceBuilder {
    private final Scheduler scheduler;
    private final Queue<Runnable> steps = new LinkedList<>();

    public SequenceBuilder(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public SequenceBuilder wait(long time, TimeUnit unit, Clock clock) {
        steps.add(() -> scheduler.schedule(this::next).after(time, unit, clock).once());
        return this;
    }

    public SequenceBuilder wait(long time, Clock clock) {
        steps.add(() -> scheduler.schedule(this::next).after(time, clock).once());
        return this;
    }

    public SequenceBuilder run(Runnable task) {
        steps.add(() -> {
            task.run();
            next();
        });
        return this;
    }

    private void next() {
        Runnable step = steps.poll();
        if (step != null) {
            step.run();
        }
    }

    public void start() {
        next();
    }
}