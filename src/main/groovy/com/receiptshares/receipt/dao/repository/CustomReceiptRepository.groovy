package com.receiptshares.receipt.dao.repository

import com.receiptshares.receipt.dao.OrderedItemEntity
import reactor.core.publisher.Mono

interface CustomReceiptRepository {

    Mono<Void> addOrderedItem(String receiptId, OrderedItemEntity orderedItem)

}