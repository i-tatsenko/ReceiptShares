package com.receiptshares.receipt.dao

import org.springframework.data.repository.CrudRepository

interface ItemRepository extends CrudRepository<ItemEntity, Long> {

}