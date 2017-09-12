package com.receiptshares.receipt.dao.repository

import com.receiptshares.Util
import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.model.ItemStatus
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query

@Component
class CustomOrderedItemRepositoryImpl implements CustomOrderedItemRepository {

    ReactiveMongoOperations mongoOperations

    CustomOrderedItemRepositoryImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations
    }

    @Override
    Mono<Void> incrementCount(String orderedItemId, int amount) {
        mongoOperations.updateFirst(query(where("_id").is(orderedItemId)), new Update().inc("count", amount), OrderedItemEntity)
                       .flatMap(Util.&expectSingleUpdateResult)
    }

    @Override
    Mono<Void> changeStatus(String orderedItemId, ItemStatus newStatus) {
        mongoOperations.updateFirst(query(where("_id").is(orderedItemId)), new Update().set("status", newStatus.toString()), OrderedItemEntity)
                       .flatMap(Util.&expectSingleUpdateResult)
    }
}
