package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.dao.ItemEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface ItemRepository extends ReactiveCrudRepository<ItemEntity, String> {

}