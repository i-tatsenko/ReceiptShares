package com.receiptshares.receipt.dao

import com.receiptshares.user.dao.UserEntity
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, Long> {

    @Query("{'owner': ?0}")
    Flux<ReceiptEntity> findAllActiveReceipts(UserEntity userEntity)
}
