package cf.splitit.security.limit;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;


class AbstractRateLimitTest {

    @Test
    void shouldReturnErrorWhenCannotProceed() {
        AbstractRateLimit underTest = new OneWayRateLimit(false);

        StepVerifier.create(underTest.checkRate(""))
                    .verifyErrorMatches(t -> t instanceof RateLimitException && ((RateLimitException) t).retryAfter().get() == 13);
    }

    @Test
    void shouldReturnOkWhenCanProceed() {
        AbstractRateLimit underTest = new OneWayRateLimit(true) ;
        StepVerifier.create(underTest.checkRate(""))
                    .expectComplete()
                    .verify();
    }

    @Test
    void shouldPersistCounterWhenCacheIsFull() {
        String[] persistedKey = new String[1];
        OneWayRateLimit underTest = new OneWayRateLimit(true, 1) {
            @Override
            protected void persistLimit(String key, LimitCounter counter) {
                persistedKey[0] = key;
            }
        };
        StepVerifier.create(underTest.checkRate("first"))
                    .verifyComplete();
        StepVerifier.create(underTest.checkRate("second"))
                    .verifyComplete();
        assertThat(persistedKey).isNotEmpty()
                                .containsExactly("first");
    }

    @Test
    void shouldHandleRaceConditions() {
        ParallelTestRateLimit underTest = new ParallelTestRateLimit();

        Mono.zip(underTest.checkRate("test").subscribeOn(Schedulers.elastic()),
                underTest.checkRate("test").subscribeOn(Schedulers.elastic())).block();
        assertThat(underTest.getFastCounterValue()).isEqualTo(2);
    }

}

class CountingCounter implements LimitCounter {

    private AtomicInteger count = new AtomicInteger();

    @Override
    public boolean acquire() {
        count.incrementAndGet();
        return true;
    }

    public int getCount() {
        return count.intValue();
    }
}

class ParallelTestRateLimit extends AbstractRateLimit<CountingCounter> {

    private CountingCounter fastCounter;
    private AtomicBoolean slowCreated = new AtomicBoolean();

    public ParallelTestRateLimit() {
        super("Parallel test rate limit");
    }

    @Override
    protected Mono<CountingCounter> createCounter(String key) {
        if (!slowCreated.get() && slowCreated.compareAndSet(false, true)) {
            return Mono.delay(Duration.of(1, ChronoUnit.SECONDS)).then(Mono.just(new CountingCounter()));
        }
        fastCounter = new CountingCounter();
        return Mono.just(fastCounter);
    }

    public int getFastCounterValue() {
        return fastCounter.getCount();
    }

    @Override
    protected void persistLimit(String key, CountingCounter counter) {

    }
}

class OneWayRateLimit extends AbstractRateLimit {

    private boolean result;

    public OneWayRateLimit(boolean result) {
        super("Always " + result + " rate limit");
        this.result = result;
    }

    public OneWayRateLimit(boolean result, int size) {
        super("Always " + result + " rate limit", size, 100);
        this.result = result;
    }

    @Override
    protected Mono<LimitCounter> createCounter(String key) {
        return Mono.just(() -> result);
    }

    @Override
    protected Optional<Integer> retryAfter(String key, LimitCounter counter) {
        return Optional.of(13);
    }

    @Override
    protected void persistLimit(String key, LimitCounter counter) {

    }
}