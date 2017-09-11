package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.OrderedItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderItemRepository extends ReactiveCrudRepository<OrderedItemEntity, String> {
}
