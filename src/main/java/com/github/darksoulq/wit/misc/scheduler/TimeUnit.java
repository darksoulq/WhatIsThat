package com.github.darksoulq.wit.misc.scheduler;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

public enum TimeUnit implements TemporalUnit {

    MILLISECONDS(1),
    TICKS(50),
    SECONDS(1000),
    MINUTES(60000),
    HOURS(3600000),
    DAYS(86400000);

    private final long multiplier;
    private final Duration duration;

    TimeUnit(long multiplier) {
        this.multiplier = multiplier;
        this.duration = Duration.ofMillis(multiplier);
    }

    public long toMillis(long amount) {
        return (amount * multiplier);
    }

    public long toTicks(long amount) {
        return (amount * multiplier) / TICKS.multiplier;
    }

    public long toSeconds(long amount) {
        return (amount * multiplier) / SECONDS.multiplier;
    }

    public long toMinutes(long amount) {
        return (amount * multiplier) / MINUTES.multiplier;
    }

    public long toHours(long amount) {
        return (amount * multiplier) / HOURS.multiplier;
    }

    public long toDays(long amount) {
        return (amount * multiplier) / DAYS.multiplier;
    }

    public long convert(long amount, TimeUnit targetUnit) {
        return (amount * multiplier) / targetUnit.multiplier;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean isDurationEstimated() {
        return false;
    }

    @Override
    public boolean isDateBased() {
        return false;
    }

    @Override
    public boolean isTimeBased() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R extends Temporal> R addTo(R temporal, long amount) {
        return (R) temporal.plus(amount * multiplier, ChronoUnit.MILLIS);
    }

    @Override
    public long between(Temporal temporal1Inclusive, Temporal temporal2Exclusive) {
        return temporal1Inclusive.until(temporal2Exclusive, ChronoUnit.MILLIS) / multiplier;
    }
}