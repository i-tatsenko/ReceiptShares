package com.receiptshares.receipt

import com.receiptshares.receipt.dao.PlaceEntity
import com.receiptshares.receipt.dao.repository.PlaceRepository
import com.receiptshares.receipt.model.Place
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class PlaceService {

    PlaceRepository placeRepository

    @Autowired
    PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository
    }

    Mono<PlaceEntity> findOrCreatePlace(Place place, String authorId) {
        Mono<PlaceEntity> result = Mono.empty()
        if (place.id) {
            result = placeRepository.findByIdAndProvider(place.id, place.provider)
        }
        result.switchIfEmpty(createNewPlace(place, authorId))

    }

    Mono<PlaceEntity> createNewPlace(Place place, String authorId) {
        return placeRepository.save(new PlaceEntity(id: place.id, name: place.name, provider: place.provider ?: authorId))

    }
}
