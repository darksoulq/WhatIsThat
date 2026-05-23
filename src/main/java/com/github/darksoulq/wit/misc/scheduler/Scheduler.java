package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class Scheduler {
    private final TaskDispatcher dispatcher;

    public Scheduler(Plugin plugin) {
        this.dispatcher = new TaskDispatcher(plugin);
    }

    public TaskBuilder schedule(Runnable task) {
        return new TaskBuilder(this, task);
    }

    public void sequence(Consumer<SequenceBuilder> sequenceConfig) {
        SequenceBuilder builder = new SequenceBuilder(this);
        sequenceConfig.accept(builder);
        builder.start();
    }

    protected TaskDispatcher getDispatcher() {
        return dispatcher;
    }
}