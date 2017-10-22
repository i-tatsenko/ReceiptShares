package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.PlaceEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface PlaceRepository extends ReactiveCrudRepository<PlaceEntity, String> {

    Mono<PlaceEntity> findByIdAndProvider(String id, String provider)
}
