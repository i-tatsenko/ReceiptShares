package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.PlaceEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PlaceRepository extends ReactiveCrudRepository<PlaceEntity, String> {
}
