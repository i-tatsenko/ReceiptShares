package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.InviteEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface InviteRepository extends ReactiveCrudRepository<InviteEntity, String> {

}
