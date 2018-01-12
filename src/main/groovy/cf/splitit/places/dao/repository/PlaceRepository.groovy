package cf.splitit.places.dao.repository

import cf.splitit.places.dao.PlaceEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface PlaceRepository extends ReactiveCrudRepository<PlaceEntity, String> {

    Flux<PlaceEntity> findByProvider(String id)

    Mono<PlaceEntity> findByIdAndProvider(String id, String provider)
}
