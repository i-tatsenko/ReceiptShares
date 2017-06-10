package com.receiptshares.receipt.dao

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, String> {

    @Query('{$and: [{\'owner.id\': ?0}, {\'status\': \'ACTIVE\'}]}')
    Flux<ReceiptEntity> findAllActiveReceipts(String ownerId)
}
