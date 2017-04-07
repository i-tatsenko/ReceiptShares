package com.receiptshares.user.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface UserConnectionRepo extends ReactiveCrudRepository<UserConnectionEntity, String> {

    Flux<UserConnectionEntity> findByProviderUserIdIn(Collection<String> providerUserId)
}
