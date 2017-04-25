package com.receiptshares.receipt.dao

import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, Long> {

    @Query('{$and: [{\'owner\': ?0}, {\'status\': \'ACTIVE\'}]}')
    Flux<ReceiptEntity> findAllActiveReceipts(BigInteger ownerId)
}
