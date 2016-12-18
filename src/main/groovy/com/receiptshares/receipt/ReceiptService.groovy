package com.receiptshares.receipt

import com.receiptshares.receipt.dao.ItemEntity
import com.receiptshares.receipt.dao.ItemRepository
import com.receiptshares.receipt.dao.OrderItemRepository
import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.ReceiptRepository
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.receipt.model.Place
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
@Slf4j
class ReceiptService {

    ReceiptRepository receiptRepository
    UserRepo userRepo
    OrderItemRepository orderItemRepository
    ItemRepository itemRepository

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, UserRepo userRepo) {
        this.receiptRepository = receiptRepository
        this.userRepo = userRepo
    }

    Collection<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user as UserEntity)
                                .collect { it as Receipt }
    }

    def createNewReceipt(Place place, User owner, String name, Collection<Long> members) {
        def newReceipt = new Receipt(place: place, owner: owner, status: ACTIVE, name: name)
        def newReceiptEntity = newReceipt as ReceiptEntity

        newReceiptEntity.members = userRepo.findAll(members).toSet()
        return receiptRepository.save(newReceiptEntity).id
    }

    def findById(Long id) {
        //TODO check security
        receiptRepository.findOne(id) as Receipt
    }

    def createNewItem(User user, Long receiptId, String name, Double price) {
        //TODO check security
        def receipt = receiptRepository.findOne(receiptId)
        def item = itemRepository.save(new ItemEntity(name: name, price: price))
        def userEntity = userRepo.findOne(user.id)
        def orderItem = new OrderedItemEntity(user: userEntity, item: item, status: ItemStatus.ACTIVE.toString())
        receipt.orderedItems.add(orderItem)
        receiptRepository.save(receipt)
        return orderItem
    }
}