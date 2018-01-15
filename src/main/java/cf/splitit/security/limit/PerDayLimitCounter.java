package cf.splitit.security.limit;

import java.util.concurrent.atomic.AtomicInteger;

class PerDayLimitCounter implements LimitCounter{

    private final AtomicInteger counter;
    private final int limit;
    private final AtomicInteger dayOfYear;
    private final Clock clock;

    PerDayLimitCounter(int limit, Clock clock) {
        this(0, limit, clock);
    }

    PerDayLimitCounter(int counterInitialValue, int limit, Clock clock) {
        this.counter = new AtomicInteger(counterInitialValue);
        this.dayOfYear = new AtomicInteger(clock.getCurrentDayOfYear());
        this.limit = limit;
        this.clock = clock;
    }

    @Override
    public boolean acquire() {
        if (dayOfYear.get() != clock.getCurrentDayOfYear()) {
            resetCounter();
        }
        return counter.getAndIncrement() < limit;
    }

    int getCounterState() {
        return counter.get();
    }

    int getDayOfYear() {
        return dayOfYear.get();
    }

    private void resetCounter() {
        int currentDay = clock.getCurrentDayOfYear();
        synchronized (this) {
            if (dayOfYear.compareAndSet(currentDay - 1, currentDay)) {
                counter.set(0);
            }
        }
    }
}
