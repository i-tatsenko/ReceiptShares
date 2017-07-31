package com.receiptshares.receipt.dao

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, String> {

    @Query('{\'owner.id\': ?#{new org.bson.types.ObjectId([0])}}')
    Flux<ReceiptEntity> findAllActiveReceipts(String ownerId)
}
