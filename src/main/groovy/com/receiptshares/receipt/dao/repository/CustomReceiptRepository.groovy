package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.model.ItemStatus
import reactor.core.publisher.Mono

interface CustomReceiptRepository {

    Mono<Void> addOrderedItem(String receiptId, OrderedItemEntity orderedItem)

    /**
     *
     * @return {@code true} in case the last item was removed
     */
    Mono<Boolean> incrementOrderedItemAmount(String receiptId, String orderedItemId, boolean increment)

    Mono<Void> changeOrderedItemStatus(String receiptId, String orderedItemId, ItemStatus status)

    Mono<Void> addUserToReceipt(String receiptId, String userId)

}