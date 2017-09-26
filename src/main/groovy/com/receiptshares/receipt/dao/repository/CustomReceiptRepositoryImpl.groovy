package com.receiptshares.receipt.dao.repository

import com.receiptshares.Util
import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.model.ItemStatus
import groovy.util.logging.Slf4j
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query

@Component
@Slf4j
class CustomReceiptRepositoryImpl implements CustomReceiptRepository {

    final ReactiveMongoOperations mongoOperations

    CustomReceiptRepositoryImpl(ReactiveMongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations
    }

    @Override
    Mono<Void> addOrderedItem(String receiptId, OrderedItemEntity orderedItem) {
        Update updateOperation = new Update().push("orderedItems", orderedItem)
        return mongoOperations.updateFirst(query(where("_id").is(receiptId)), updateOperation, ReceiptEntity)
                              .flatMap(Util.&expectSingleUpdateResult)
    }

    @Override
    Mono<Boolean> incrementOrderedItemAmount(String receiptId, String orderedItemId, boolean isIncrement = true) {
        Update update = new Update().inc('orderedItems.$.count', isIncrement ? 1 : -1)

        def receiptFindCriteria = where("_id").is(receiptId)

        Query q
        if (!isIncrement) {
            q = query(receiptFindCriteria.and("orderedItems").elemMatch(where("_id").is(new ObjectId(orderedItemId)).and("count").gt(1)))
        } else {
            q = query(receiptFindCriteria.and("orderedItems._id").is(new ObjectId(orderedItemId)))
        }
        return mongoOperations.updateFirst(q, update, ReceiptEntity)
                              .flatMap({ result ->
            if (result.modifiedCount == 0 && !isIncrement) {
                return changeOrderedItemStatus(receiptId, orderedItemId, ItemStatus.DELETED).then(Mono.just(true))
            }
            return Util.expectSingleUpdateResult(result).then(Mono.just(false))
        })
    }

    @Override
    Mono<Void> changeOrderedItemStatus(String receiptId, String orderedItemId, ItemStatus status) {
        def q = query(where("_id").is(receiptId).and("orderedItems._id").is(new ObjectId(orderedItemId)))
        mongoOperations.updateFirst(q, new Update().set('orderedItems.$.status', status.toString()), ReceiptEntity)
                       .flatMap(Util.&expectSingleUpdateResult)
    }

}
