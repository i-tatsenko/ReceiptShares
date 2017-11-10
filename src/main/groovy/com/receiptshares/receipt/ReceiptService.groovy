package com.receiptshares.receipt

import com.receiptshares.places.PlaceService
import com.receiptshares.places.dao.PlaceEntity
import com.receiptshares.receipt.dao.*
import com.receiptshares.receipt.dao.repository.InviteRepository
import com.receiptshares.receipt.dao.repository.ItemRepository
import com.receiptshares.receipt.dao.repository.OrderItemRepository
import com.receiptshares.places.dao.repository.PlaceRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import com.receiptshares.receipt.exception.OrderedItemNotFound
import com.receiptshares.receipt.exception.ReceiptNotFoundException
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.places.model.Place
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

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
@Slf4j
class ReceiptService {

    private ReceiptRepository receiptRepository
    private OrderItemRepository orderItemRepository
    private ItemRepository itemRepository
    private PlaceRepository placeRepository
    private PersonRepository personRepository
    private InviteService inviteService
    private PlaceService placeService

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, OrderItemRepository orderItemRepository,
                   ItemRepository itemRepository, PlaceRepository placeRepository, PersonRepository personRepository, PlaceService placeService, InviteService inviteService) {
        this.receiptRepository = receiptRepository
        this.orderItemRepository = orderItemRepository
        this.itemRepository = itemRepository
        this.placeRepository = placeRepository
        this.personRepository = personRepository
        this.placeService = placeService
        this.inviteService = inviteService
    }

    Flux<Receipt> receiptsForUser(String ownerId) {
        return receiptRepository.findAllActiveReceipts(ownerId)
                                .map { it as Receipt }
    }

    Mono<Receipt> createNewReceipt(Place p, String ownerId, String name, Collection<String> memberIds) {
        Mono<PersonEntity> ownerMono = personRepository.findById(ownerId)
        Mono<List<PersonEntity>> members = personRepository.findAllById(memberIds)
                                                           .collectList()
        Mono<PlaceEntity> place = placeService.findOrCreatePlace(p, ownerId)
        return Mono.when(ownerMono, members, place, Mono.just(name))
                   .map({ buildReceipt(it) })
                   .flatMap({ ReceiptEntity receipt -> receiptRepository.save(receipt) } as Function)
                   .flatMap(this.&addInviteLink)
                   .map({ it as Receipt })
    }

    private Mono<ReceiptEntity> addInviteLink(ReceiptEntity receipt) {
        return inviteService.createInviteLink(receipt.id, receipt.owner)
                            .flatMap({
            receipt.inviteLink = it
            return receiptRepository.save(receipt)
        })

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
        return itemRepository.save(new ItemEntity(name: name, price: price))
                             .flatMap({ item -> createOrderedItem(ownerId, item, receiptId) })
                             .map({ it as OrderedItem })
    }

    Mono<Boolean> incrementOrderedItem(String ownerId, String receiptId, String orderedItemId, boolean isIncrement) {
        //TODO check security
        return receiptRepository.incrementOrderedItemAmount(receiptId, orderedItemId, isIncrement)
                                .flatMap({ result ->
            if (result) {
                return orderItemRepository.changeStatus(orderedItemId, ItemStatus.DELETED)
                                          .then(Mono.just(result))
            } else {
                return orderItemRepository.incrementCount(orderedItemId, isIncrement ? 1 : -1)
                                          .then(Mono.just(result))
            }
        })
    }

    Mono<Void> restoreOrderedItem(String ownerId, String receiptId, String orderedItemId) {
        //TODO check security
        return Mono.when(receiptRepository.changeOrderedItemStatus(receiptId, orderedItemId, ItemStatus.ACTIVE),
                orderItemRepository.changeStatus(orderedItemId, ItemStatus.ACTIVE))
                   .then()

    }

    Mono<Void> cloneItem(String ownerId, String receiptId, String cloneSourceId) {
        //TODO check security
        Mono<ReceiptEntity> receipt = receiptRepository.findById(receiptId)
                                                       .switchIfEmpty(Mono.error(new ReceiptNotFoundException()))
        Mono<OrderedItemEntity> orderedItem = orderItemRepository.findById(cloneSourceId)
                                                                 .switchIfEmpty(Mono.error(new OrderedItemNotFound()))
        return Mono.when(receipt, orderedItem)
                   .flatMap({ createOrIncrementOrderedItem(ownerId, it.t1, it.t2) })
    }

    private Mono<Void> createOrIncrementOrderedItem(String ownerId, ReceiptEntity receipt, OrderedItemEntity orderedItem) {
        String existingOrderedItemId = userHaveAlreadyOrderedItem(ownerId, receipt, orderedItem.item)
        if (existingOrderedItemId) {
            return incrementOrderedItem(ownerId, receipt.id, existingOrderedItemId, true).then()
        } else {
            return createOrderedItem(ownerId, orderedItem.item, receipt.id).then()
        }
    }

    private String userHaveAlreadyOrderedItem(String ownerId, ReceiptEntity receipt, ItemEntity item) {
        return receipt.orderedItems.find({
            it.owner.id == ownerId && it.item.id == item.id && it.status == ItemStatus.ACTIVE.toString()
        })?.id
    }

    private Mono<OrderedItemEntity> createOrderedItem(String ownerId, ItemEntity item, String receiptId) {
        Mono<OrderedItemEntity> orderedItem = personRepository.findById(ownerId)
                                                              .map({ owner -> new OrderedItemEntity(owner, item) })
                                                              .flatMap({ orderedItem -> orderItemRepository.save(orderedItem) })
                                                              .cache()

        return orderedItem.flatMap({ receiptRepository.addOrderedItem(receiptId, it) })
                          .then(orderedItem)
    }
}