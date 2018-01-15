package cf.splitit.security.limit;

import cf.splitit.MockitoExtension;
import cf.splitit.security.limit.persistence.DayRateCounterEntity;
import cf.splitit.security.limit.persistence.DayRateCounterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DimensionOfDayRateLimitTest {

    @Mock
    private DayRateCounterRepository dayRateCounterRepository;

    private DimensionOfDayRateLimit underTest;

    private int dayOfYear = 1;

    @BeforeEach
    void setup() {
        underTest = new DimensionOfDayRateLimit("test", 2, dayRateCounterRepository, () -> dayOfYear);
    }


    @Test
    void shouldCreateNewCounterWhenThereIsNoSaved() {
        when(dayRateCounterRepository.findByKeyAndDayOfYear("test", dayOfYear)).thenReturn(Mono.empty());

        StepVerifier.create(underTest.createCounter("test"))
                    .assertNext(counter -> assertThat(counter.getCounterState()).isEqualTo(0))
                    .verifyComplete();
    }

    @Test
    void shouldCreateNewCounterBasedOnSaved() {
        DayRateCounterEntity savedCounter = mock(DayRateCounterEntity.class);
        when(savedCounter.getValue()).thenReturn(1);
        when(dayRateCounterRepository.findByKeyAndDayOfYear("test", dayOfYear)).thenReturn(Mono.just(savedCounter));

        StepVerifier.create(underTest.createCounter("test"))
                    .assertNext(counter -> assertThat(counter.getCounterState()).isEqualTo(1))
                    .verifyComplete();
    }

    @Test
    void shouldUpdateExistingCounter() {
        DayRateCounterEntity entity = mock(DayRateCounterEntity.class);
        when(dayRateCounterRepository.findByKeyAndDayOfYear("test", dayOfYear)).thenReturn(Mono.just(entity));
        when(dayRateCounterRepository.save(entity)).thenReturn(Mono.just(entity));

        PerDayLimitCounter counter = new PerDayLimitCounter(13, 20, () -> dayOfYear);
        underTest.persistLimit("test", counter);

        verify(dayRateCounterRepository, timeout(200)).save(entity);
        verify(entity).setValue(13);
    }

    @Test
    void shouldSaveNewEntityWhenNoSaved() {
        int anotherDayOfYear = 7;
        when(dayRateCounterRepository.findByKeyAndDayOfYear("test", anotherDayOfYear)).thenReturn(Mono.empty());
        when(dayRateCounterRepository.save(any(DayRateCounterEntity.class))).thenReturn(Mono.just(mock(DayRateCounterEntity.class)));

        PerDayLimitCounter counter = new PerDayLimitCounter(13, 20, () -> anotherDayOfYear);
        underTest.persistLimit("test", counter);

        ArgumentCaptor<DayRateCounterEntity> entityCaptor = ArgumentCaptor.forClass(DayRateCounterEntity.class);
        verify(dayRateCounterRepository, timeout(200)).save(entityCaptor.capture());

        DayRateCounterEntity savedEntity = entityCaptor.getValue();
        assertThat(savedEntity.getKey()).isEqualTo("test");
        assertThat(savedEntity.getDayOfYear()).isEqualTo(anotherDayOfYear);
        assertThat(savedEntity.getValue()).isEqualTo(13);
    }
}

