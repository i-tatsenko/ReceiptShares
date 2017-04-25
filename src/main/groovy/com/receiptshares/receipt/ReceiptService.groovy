package com.receiptshares.receipt

import com.receiptshares.receipt.dao.*
import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Place
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.util.function.Function

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
@Slf4j
@CompileStatic
class ReceiptService {

    private ReceiptRepository receiptRepository
    private UserRepo userRepo
    private OrderItemRepository orderItemRepository
    private ItemRepository itemRepository

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, UserRepo userRepo, OrderItemRepository orderItemRepository, ItemRepository itemRepository) {
        this.receiptRepository = receiptRepository
        this.userRepo = userRepo
        this.orderItemRepository = orderItemRepository
        this.itemRepository = itemRepository
    }

    Flux<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user.id)
                                .map { it as Receipt }
    }

    Mono<Receipt> createNewReceipt(Place place, User owner, String name, Collection<Long> members) {
        //TODO find place in DB
        def newReceipt = new Receipt(place: place, owner: owner, status: ACTIVE, name: name)
        def newReceiptEntity = newReceipt as ReceiptEntity
        newReceiptEntity.members = new HashSet<>(members.collect(BigInteger.&valueOf))

        receiptRepository.save(newReceiptEntity)
                         .map({ it as Receipt })
    }

    Mono<Receipt> findById(Long id) {
        //TODO check security
        receiptRepository.findOne(id)
                         .map({ it as Receipt })
    }

    Mono<OrderedItem> createNewItem(User user, Long receiptId, String name, Double price) {
        //TODO check security
        //TODO find item in DB if exists
        return itemRepository.save(new ItemEntity(name: name, price: price))
                             .map({ ItemEntity item -> createOrderedItem(user, item, receiptId) } as Function)
                             .map({ it as OrderedItem })
    }

    Mono<OrderedItemEntity> addItem(User user, Long receiptId, Long itemId) {
        return itemRepository.findOne(itemId)
                             .map({ ItemEntity item -> createOrderedItem(user, item, receiptId) } as Function)
    }

    private Mono<OrderedItemEntity> createOrderedItem(User user, ItemEntity item, Long receiptId) {
        return receiptRepository.
                findOne(receiptId).
                flatMap({ ReceiptEntity receipt ->
                    receipt.members << BigInteger.valueOf(receiptId)
                    receiptRepository.save(receipt)
                } as Function).
                then(orderItemRepository.save(new OrderedItemEntity(user.id, item.id)))
    }
}