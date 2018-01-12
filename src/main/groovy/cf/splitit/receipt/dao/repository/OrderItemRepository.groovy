package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.dao.OrderedItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderItemRepository extends ReactiveCrudRepository<OrderedItemEntity, String>, CustomOrderedItemRepository {
}
