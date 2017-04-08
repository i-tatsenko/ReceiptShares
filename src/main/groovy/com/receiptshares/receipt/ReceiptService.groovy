package com.receiptshares.receipt

import com.receiptshares.receipt.dao.*
import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Place
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.util.function.BiFunction
import java.util.function.Function

import static com.receiptshares.Util.pickLast
import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
@Slf4j
class ReceiptService {

    ReceiptRepository receiptRepository
    UserRepo userRepo
    OrderItemRepository orderItemRepository
    ItemRepository itemRepository

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, UserRepo userRepo, OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.receiptRepository = receiptRepository
        this.userRepo = userRepo
        this.orderItemRepository = orderItemRepository
        this.itemRepository = itemRepository
    }

    Flux<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user as UserEntity)
                                .map { it as Receipt }
    }

    Mono<Receipt> createNewReceipt(Place place, User owner, String name, Collection<Long> members) {
        //TODO find place in DB
        def newReceipt = new Receipt(place: place, owner: owner, status: ACTIVE, name: name)
        def newReceiptEntity = newReceipt as ReceiptEntity

        userRepo.findAll(members)
                .collectList()
                .and({ memberEntities ->
            newReceiptEntity.members = memberEntities
            receiptRepository.save(newReceiptEntity)
        } as Function, pickLast())
                .map({ it as Receipt })
    }

    Mono<Receipt> findById(Long id) {
        //TODO check security
        receiptRepository.findOne(id).map({ it as Receipt })
    }

    Mono<OrderedItem> createNewItem(User user, Long receiptId, String name, Double price) {
        //TODO check security
        //TODO find item in DB if exists
        return itemRepository.save(new ItemEntity(name: name, price: price))
                             .and({ item -> createOrderedItem(user, item, receiptId) } as Function, pickLast())
                             .map({ it as OrderedItem })
    }

    Mono<OrderedItemEntity> addItem(User user, Long receiptId, Long itemId) {
        return itemRepository.findOne(itemId)
                             .and({ item -> createOrderedItem(user, item, receiptId) } as Function, pickLast())
    }

    private Mono<OrderedItemEntity> createOrderedItem(User user, ItemEntity item, Long receiptId) {
        Function<UserEntity, Mono<OrderedItemEntity>> createOrderedItemEntity = { userEntity ->
            orderItemRepository.save(new OrderedItemEntity(userEntity, item))
        }

        return userRepo.findOne(user.id)
                       .and(createOrderedItemEntity, pickLast())
                       .and(receiptRepository.findOne(receiptId), this.&addOrderedItem as BiFunction)
    }

    private OrderedItemEntity addOrderedItem(ReceiptEntity receipt, OrderedItemEntity orderedItem) {
        receipt.orderedItems.add(orderedItem)
        receiptRepository.save(receipt)
        return orderedItem
    }

}