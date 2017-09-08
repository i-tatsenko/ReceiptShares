package com.receiptshares.receipt

import com.receiptshares.receipt.dao.*
import com.receiptshares.receipt.exception.OrderedItemNotFound
import com.receiptshares.receipt.exception.ReceiptNotFoundException
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.PersonEntity
import com.receiptshares.user.dao.PersonRepository
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple4

import java.util.function.Function
import java.util.function.Predicate

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE
import static java.util.Collections.singleton

@Component
@Slf4j
class ReceiptService {

    private ReceiptRepository receiptRepository
    private OrderItemRepository orderItemRepository
    private ItemRepository itemRepository
    private PlaceRepository placeRepository
    private PersonRepository personRepository

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, OrderItemRepository orderItemRepository,
                   ItemRepository itemRepository, PlaceRepository placeRepository, PersonRepository personRepository) {
        this.receiptRepository = receiptRepository
        this.orderItemRepository = orderItemRepository
        this.itemRepository = itemRepository
        this.placeRepository = placeRepository
        this.personRepository = personRepository
    }

    Flux<Receipt> receiptsForUser(String ownerId) {
        return receiptRepository.findAllActiveReceipts(ownerId)
                .map { it as Receipt }
    }

    Mono<Receipt> createNewReceipt(String placeName, String ownerId, String name, Collection<String> memberIds) {
        //TODO find place in DB
        Mono<PersonEntity> ownerMono = personRepository.findById(ownerId)
        Mono<List<PersonEntity>> members = personRepository.findAllById(memberIds)
                .collectList()
        Mono<PlaceEntity> place = placeRepository.save(new PlaceEntity(name: placeName, authorId: ownerId))
        return Mono.when(ownerMono, members, place, Mono.just(name))
                .map({ buildReceipt(it) })
                .flatMap({ ReceiptEntity receipt -> receiptRepository.save(receipt) } as Function)
                .map({ it as Receipt })
    }

    private ReceiptEntity buildReceipt(Tuple4<PersonEntity, List<PersonEntity>, PlaceEntity, String> ownerMembersAndPlaceAndName) {
        return ReceiptEntity.builder()
                .owner(ownerMembersAndPlaceAndName.t1)
                .place(ownerMembersAndPlaceAndName.t3)
                .status(ACTIVE.toString())
                .name(ownerMembersAndPlaceAndName.t4)
                .members(new ArrayList<PersonEntity>(ownerMembersAndPlaceAndName.t2))
                .orderedItems(Collections.emptyList())
                .build()
    }

    Mono<Receipt> findById(String id) {
        //TODO check security
        receiptRepository.findById(id)
                .map({ it as Receipt })
                .switchIfEmpty(Mono.error(new ReceiptNotFoundException()))
    }

    Mono<OrderedItem> createNewItem(String ownerId, String receiptId, String name, Double price) {
        //TODO check security
        //TODO find item in DB if exists
        return itemRepository.save(new ItemEntity(name: name, price: price))
                .flatMap({ item -> createOrderedItem(ownerId, item, receiptId) } as Function)
                .map({ it as OrderedItem })
    }

    Mono<OrderedItem> duplicateOrderedItem(String ownerId, String receiptId, String orderedItemId) {
        return orderItemRepository.findById(orderedItemId)
                .map({ it.item })
                .flatMap({ ItemEntity item -> createOrderedItem(ownerId, item, receiptId) } as Function)
                .map({ it as OrderedItem })
    }

    Mono<Void> deleteOrderedItem(String ownerId, String receiptId, String orderedItemId) {
        return updateOrderedItemStatus(receiptId, byId(orderedItemId).and(byOwner(ownerId)), ItemStatus.DELETED)
                .flatMap({ orderItemRepository.deleteById(it.id) })

    }

    Mono<Void> restoreOrderedItem(String ownerId, String receiptId, String orderedItemId) {
        return updateOrderedItemStatus(receiptId, byId(orderedItemId).and(byOwner(ownerId)), ItemStatus.ACTIVE)
                .flatMap({ orderItemRepository.save(it) })
                .then()
    }

    private static Predicate<OrderedItemEntity> byId(String id) {
        return { it.id == id }
    }

    private static Predicate<OrderedItemEntity> byOwner(String ownerId) {
        return { it.owner.id == ownerId }
    }

    private Mono<OrderedItemEntity> updateOrderedItemStatus(String receiptId, Predicate<OrderedItemEntity> orderedItemPredicate, ItemStatus newStatus) {
        receiptRepository.findById(receiptId)
                .flatMap({ receipt -> updateOrderedItem(receipt, orderedItemPredicate) })
                .doOnNext({ receiptAndFountItem -> receiptAndFountItem.second.status = newStatus.toString() })
                .flatMap({ receiptAndFountItem ->
            receiptRepository.save(receiptAndFountItem.first)
                    .then(Mono.just(receiptAndFountItem.second))
        })
    }

    private Mono<Tuple2<ReceiptEntity, OrderedItemEntity>> updateOrderedItem(ReceiptEntity receipt, Predicate<OrderedItemEntity> itemPredicate) {
        OrderedItemEntity orderedItem = receipt.orderedItems.find(itemPredicate.&test)
        if (orderedItem) {
            return Mono.just(new Tuple2<ReceiptEntity, OrderedItemEntity>(receipt, orderedItem))
        } else {
            return Mono.error(new OrderedItemNotFound())
        }
    }

    private Mono<OrderedItemEntity> createOrderedItem(String ownerId, ItemEntity item, String receiptId) {
        Mono<OrderedItemEntity> orderedItem = personRepository.findById(ownerId)
                .map({ owner -> new OrderedItemEntity(owner, item) })
                .flatMap({ orderedItem -> orderItemRepository.save(orderedItem) })
        Mono<ReceiptEntity> receipt = receiptRepository.findById(receiptId)

        return Mono.when(receipt, orderedItem)
                .flatMap({ receiptAndItem ->
            if (receiptAndItem.t1.orderedItems == null) {
                receiptAndItem.t1.orderedItems = new ArrayList<>()
            }
            log.info("Adding ordered item: " + receiptAndItem.t2)
            receiptAndItem.t1.orderedItems << receiptAndItem.t2
            return receiptRepository.save(receiptAndItem.t1)
        })
                .then(orderedItem)
    }
}