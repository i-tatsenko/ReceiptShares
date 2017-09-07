package com.receiptshares.receipt

import com.receiptshares.receipt.dao.*
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

import java.util.function.Consumer
import java.util.function.Function

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

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
                            .members(new HashSet<PersonEntity>(ownerMembersAndPlaceAndName.t2))
                            .build()
    }

    Mono<Receipt> findById(String id) {
        //TODO check security
        receiptRepository.findById(id)
                         .map({ it as Receipt })
    }

    Mono<OrderedItem> createNewItem(String ownerId, String receiptId, String name, Double price) {
        //TODO check security
        //TODO find item in DB if exists
        return itemRepository.save(new ItemEntity(name: name, price: price))
                             .flatMap({ item -> createOrderedItem(ownerId, item, receiptId) } as Function)
                             .map({ it as OrderedItem })
    }

    Mono<OrderedItem> addItem(String ownerId, String receiptId, String orderedItemId) {
        return orderItemRepository.findById(orderedItemId)
                                  .map({ it.item })
                                  .flatMap({ ItemEntity item -> createOrderedItem(ownerId, item, receiptId) } as Function)
                                  .map({ it as OrderedItem })
    }

    Mono<Receipt> deleteItem(String ownerId, String receiptId, String orderedItemId) {
        return orderItemRepository.deleteById(orderedItemId)
                                  .then(receiptRepository.findById(receiptId))
                                  .flatMap({ deleteOrderedItem(it, ownerId, orderedItemId) })
                                  .map({ it as Receipt })

    }

    private Mono<ReceiptEntity> deleteOrderedItem(ReceiptEntity receipt, String itemOwnerId, String orderedItemId) {
        def orderedItems = new HashSet<>(receipt.orderedItems)
        def deleted = orderedItems.removeIf({ it.id == orderedItemId && it.owner.id == itemOwnerId })
        if (deleted) {
            receipt.orderedItems = orderedItems
            return receiptRepository.save(receipt)
        } else {
            return Mono.error(new OrderedItemNotFound(orderedItemId: orderedItemId))
        }

    }

    private Mono<OrderedItemEntity> createOrderedItem(String ownerId, ItemEntity item, String receiptId) {
        Mono<OrderedItemEntity> orderedItem = personRepository.findById(ownerId)
                                                              .map({ owner -> new OrderedItemEntity(owner, item) })
                                                              .flatMap({ orderedItem -> orderItemRepository.save(orderedItem) } as Function)
        Mono<ReceiptEntity> receipt = receiptRepository.findById(receiptId)

        return Mono.when(receipt, orderedItem)
                   .doOnNext({ receiptAndItem ->
            if (receiptAndItem.t1.orderedItems == null) {
                receiptAndItem.t1.orderedItems = new HashSet<>()
            }
            receiptAndItem.t1.orderedItems << receiptAndItem.t2
            receiptRepository.save(receiptAndItem.t1).block()
        } as Consumer)
                   .then(orderedItem)
    }
}