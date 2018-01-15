package cf.splitit.security.limit.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DayRateCounterRepository extends ReactiveCrudRepository<DayRateCounterEntity, String> {

    Mono<DayRateCounterEntity> findByKeyAndDayOfYear(String name, int dayOfYear);
}
