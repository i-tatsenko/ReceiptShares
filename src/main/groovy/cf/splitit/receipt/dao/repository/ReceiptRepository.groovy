package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.dao.ReceiptEntity
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface ReceiptRepository extends ReactiveCrudRepository<ReceiptEntity, String>, CustomReceiptRepository {

    @Query('{$or: [{\'owner.id\': ?#{new org.bson.types.ObjectId([0])}}, {"members._id": ?#{new org.bson.types.ObjectId([0])}}]}')
    Flux<ReceiptEntity> findAllActiveReceipts(String ownerId)
}
