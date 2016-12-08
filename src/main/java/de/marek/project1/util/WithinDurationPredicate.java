package de.marek.project1.util;

import com.google.common.base.Stopwatch;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class WithinDurationPredicate implements Predicate {

    private final Stopwatch sw;
    private final long timeout;

    public WithinDurationPredicate(Duration timeout) {
        this.timeout = timeout.toMillis();
        this.sw = Stopwatch.createStarted();
    }

    @Override
    public boolean test(Object o) {
        final long elapsed = sw.elapsed(TimeUnit.MILLISECONDS);
        return elapsed < timeout;
    }
}
