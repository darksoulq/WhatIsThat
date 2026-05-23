package com.github.darksoulq.wit.misc.scheduler;

import org.bukkit.Bukkit;

public enum Clock {

    REALTIME {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }

        @Override
        public TimeUnit unit() {
            return TimeUnit.MILLISECONDS;
        }
    },
    TICKS {
        @Override
        public long now() {
            return Bukkit.getServer().getCurrentTick();
        }

        @Override
        public TimeUnit unit() {
            return TimeUnit.TICKS;
        }
    };

    public abstract long now();

    public abstract TimeUnit unit();
}