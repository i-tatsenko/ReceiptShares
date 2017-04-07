package com.receiptshares.user.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface UserRepo extends ReactiveCrudRepository<UserEntity, Long> {

    Mono<UserEntity> findByEmail(String email)

    Flux<UserEntity> findByEmailIn(Collection<String> emails)
}
