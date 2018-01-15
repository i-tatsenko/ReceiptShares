package cf.splitit.security.limit;

import cf.splitit.security.limit.persistence.DayRateCounterEntity;
import cf.splitit.security.limit.persistence.DayRateCounterRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class DimensionOfDayRateLimit extends AbstractRateLimit<PerDayLimitCounter> {

    private int limitPerDay;
    private DayRateCounterRepository dayRateCounterRepository;
    private Clock clock;

    public DimensionOfDayRateLimit(String name, int limitPerDay, DayRateCounterRepository dayRateCounterRepository, Clock clock) {
        super(name);
        this.limitPerDay = limitPerDay;
        this.dayRateCounterRepository = dayRateCounterRepository;
        this.clock = clock;
    }

    @Override
    protected Mono<PerDayLimitCounter> createCounter(String key) {
        return dayRateCounterRepository.findByKeyAndDayOfYear(key, clock.getCurrentDayOfYear())
                                       .map(savedCounter -> new PerDayLimitCounter(savedCounter.getValue(), limitPerDay, clock))
                                       .switchIfEmpty(Mono.fromCallable(() -> new PerDayLimitCounter(limitPerDay, clock)));
    }

    @Override
    protected void persistLimit(String key, PerDayLimitCounter counter) {
        dayRateCounterRepository.findByKeyAndDayOfYear(key, counter.getDayOfYear())
                                .switchIfEmpty(Mono.fromCallable(() -> createNewEntity(key, counter)))
                                .doOnNext(entity -> entity.setValue(counter.getCounterState()))
                                .flatMap(dayRateCounterRepository::save)
                                .subscribeOn(Schedulers.elastic())
                                .subscribe(saved -> log.debug("Saved value for " + saved.getKey() + " with value " + saved.getValue()),
                                        error -> log.error("Can't save counter", error));
    }

    private DayRateCounterEntity createNewEntity(String key, PerDayLimitCounter counter) {
        DayRateCounterEntity result = new DayRateCounterEntity();
        result.setKey(key);
        result.setDayOfYear(counter.getDayOfYear());
        return result;
    }
}