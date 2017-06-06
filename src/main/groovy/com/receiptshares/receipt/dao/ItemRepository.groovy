package com.receiptshares.receipt.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ItemRepository extends ReactiveCrudRepository<ItemEntity, String> {

}