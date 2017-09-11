package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.ItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ItemRepository extends ReactiveCrudRepository<ItemEntity, String> {

}