package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.dao.OrderedItemModificationEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface OrderedItemModificationRepository extends ReactiveCrudRepository<OrderedItemModificationEntity, String> {
}
