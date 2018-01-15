package cf.splitit.security.limit;

import com.google.common.util.concurrent.RateLimiter;
import reactor.core.publisher.Mono;

import java.util.Optional;

public class DimensionOfSecondRateLimit extends AbstractRateLimit<PerSecondLimitCounter> {

    private int ratePerSecond;

    public DimensionOfSecondRateLimit(String name, int ratePerSecond) {
        super(name);
        this.ratePerSecond = ratePerSecond;
    }

    @Override
    protected Mono<PerSecondLimitCounter> createCounter(String key) {
        return Mono.just(new PerSecondLimitCounter(ratePerSecond));
    }

    @Override
    protected void persistLimit(String key, PerSecondLimitCounter counter) {
        //noop
    }

    @Override
    protected Optional<Integer> retryAfter(String key, PerSecondLimitCounter counter) {
        return Optional.of(1);
    }
}

class PerSecondLimitCounter implements LimitCounter {

    private final RateLimiter limiter;

    PerSecondLimitCounter(int ratePerSecond) {
        this.limiter = RateLimiter.create(ratePerSecond);
    }

    @Override
    public boolean acquire() {
        return limiter.tryAcquire();
    }
}
