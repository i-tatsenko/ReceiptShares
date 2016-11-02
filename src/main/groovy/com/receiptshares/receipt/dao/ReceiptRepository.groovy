package com.receiptshares.receipt.dao

import com.receiptshares.user.dao.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface ReceiptRepository extends CrudRepository<ReceiptEntity, Long> {

    @Query("SELECT r FROM ReceiptEntity r WHERE owner = ?1 OR ?1 MEMBER OF r.members")
    Collection<ReceiptEntity> findAllActiveReceipts(UserEntity userEntity)
}
