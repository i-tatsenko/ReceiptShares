package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.model.ItemStatus
import reactor.core.publisher.Mono

interface CustomOrderedItemRepository {

    Mono<Void> incrementCount(String orderedItemId, int amount)

    Mono<Void> changeStatus(String orderedItemId, ItemStatus newStatus)
}
