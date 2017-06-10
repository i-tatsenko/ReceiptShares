package com.receiptshares.receipt.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PlaceRepository extends ReactiveCrudRepository<PlaceEntity, String> {
}
