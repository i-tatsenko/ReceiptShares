package com.receiptshares.receipt.dao

import org.springframework.data.repository.CrudRepository

interface OrderItemRepository extends CrudRepository<OrderedItemEntity, Long> {
}
