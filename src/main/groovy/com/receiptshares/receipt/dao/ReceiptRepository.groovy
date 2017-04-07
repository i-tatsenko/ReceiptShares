package com.receiptshares.receipt.dao

import com.receiptshares.user.dao.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, Long> {

    @Query("SELECT r FROM ReceiptEntity r WHERE owner = ?1 OR ?1 MEMBER OF r.members")
    Flux<ReceiptEntity> findAllActiveReceipts(UserEntity userEntity)
}
