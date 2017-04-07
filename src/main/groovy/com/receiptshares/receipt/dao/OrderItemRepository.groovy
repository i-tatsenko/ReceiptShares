package com.receiptshares.receipt.dao

import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderItemRepository extends ReactiveCrudRepository<OrderedItemEntity, Long> {
}
