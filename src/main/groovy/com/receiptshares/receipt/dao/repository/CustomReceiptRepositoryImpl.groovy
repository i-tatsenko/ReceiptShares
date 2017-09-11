package com.receiptshares.receipt.dao.repository

import com.mongodb.client.result.UpdateResult
import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query

@Component
class CustomReceiptRepositoryImpl implements CustomReceiptRepository {

    final ReactiveMongoOperations mongoOperations

    CustomReceiptRepositoryImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations
    }

    @Override
    Mono<Void> addOrderedItem(String receiptId, OrderedItemEntity orderedItem) {
        Update updateOperation = new Update().push("orderedItems", orderedItem)
        mongoOperations.updateFirst(query(where("_id").is(receiptId)), updateOperation, ReceiptEntity)
                       .flatMap(CustomReceiptRepositoryImpl.&expectSingleUpdateResult)
    }

    private static Mono<Void> expectSingleUpdateResult(UpdateResult updateResult) {
        if (updateResult.modifiedCount == 1) {
            return Mono.empty()
        } else {
            return Mono.error(new IllegalStateException("Expected only 1 receipt to be modified, but got: " + updateResult.modifiedCount))
        }
    }
}
