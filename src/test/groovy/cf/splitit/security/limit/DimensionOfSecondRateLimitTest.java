package cf.splitit.security.limit;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.MILLIS;

class DimensionOfSecondRateLimitTest {

    private DimensionOfSecondRateLimit underTest = new DimensionOfSecondRateLimit("test", 2);

    @Test
    void shouldAllowWhenUnderLimit() {
        StepVerifier.create(Mono.zip(
                underTest.checkRate("test"),
                Mono.delay(Duration.of(600, MILLIS)).then(underTest.checkRate("test"))
                ))
                    .verifyComplete();
    }

    @Test
    void shouldNotAllowWhenOverLimit() {
        StepVerifier.create(Mono.zip(
                underTest.checkRate("test"),
                Mono.delay(Duration.of(333, MILLIS)).then(underTest.checkRate("test")),
                Mono.delay(Duration.of(667, MILLIS)).then(underTest.checkRate("test"))
        ))
                    .expectError(RateLimitException.class)
                    .verify();
    }

}