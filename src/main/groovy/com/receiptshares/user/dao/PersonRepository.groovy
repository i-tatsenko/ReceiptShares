package com.receiptshares.user.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface PersonRepository extends ReactiveCrudRepository<PersonEntity, String> {
}
