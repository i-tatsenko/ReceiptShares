package cf.splitit.security.limit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PerDayLimitCounterTest {

    private int currentDayOfYear = 1;
    private PerDayLimitCounter underTest = new PerDayLimitCounter(1, () -> currentDayOfYear);

    @Test
    void shouldAllowWhenUnderLimit() {
        assertThat(underTest.acquire()).isTrue();
    }

    @Test
    void shouldNotAllowWhenOverLimit() {
        assertThat(underTest.acquire()).isTrue();
        assertThat(underTest.acquire()).isFalse();
    }

    @Test
    void shouldAllowWhenDayHaveChanged() {
        assertThat(underTest.acquire()).isTrue();
        currentDayOfYear = 2;
        assertThat(underTest.acquire()).isTrue();
        assertThat(underTest.getDayOfYear()).isEqualTo(2);
        assertThat(underTest.getCounterState()).isEqualTo(1);
    }

    @Test
    void shouldSetInitialValueWhenPassed() {
        PerDayLimitCounter underTest = new PerDayLimitCounter(1, 2, () -> currentDayOfYear);
        assertThat(underTest.acquire()).isTrue();
        assertThat(underTest.acquire()).isFalse();
    }

}