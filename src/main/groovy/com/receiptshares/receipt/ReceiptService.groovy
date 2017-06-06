package com.receiptshares.receipt

import com.receiptshares.receipt.dao.*
import com.receiptshares.receipt.model.OrderedItem
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
    private PlaceRepository placeRepository

    @Autowired
    ReceiptService(ReceiptRepository receiptRepository, UserRepo userRepo, OrderItemRepository orderItemRepository,
                   ItemRepository itemRepository, PlaceRepository placeRepository) {
        this.receiptRepository = receiptRepository
        this.userRepo = userRepo
        this.orderItemRepository = orderItemRepository
        this.itemRepository = itemRepository
        this.placeRepository = placeRepository
    }

    Flux<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user.id)
                                .map { it as Receipt }
    }

    Mono<Receipt> createNewReceipt(String placeName, String ownerId, String name, Collection<String> members) {
        //TODO find place in DB
        return placeRepository.save(new PlaceEntity(name: placeName))
                              .map({ PlaceEntity place ->
            ReceiptEntity.builder()
                         .ownerId(ownerId)
                         .placeId(place.id)
                         .status(ACTIVE.toString())
                         .name(name)
                         .memberIds(new HashSet<String>(members))
                         .build()
        })
                              .flatMap({ ReceiptEntity receipt -> receiptRepository.save(receipt) } as Function)
                              .map({ it as Receipt })
    }

    Mono<Receipt> findById(String id) {
        //TODO check security
        receiptRepository.findById(id)
                         .map({ it as Receipt })
    }

    Mono<OrderedItem> createNewItem(User user, String receiptId, String name, Double price) {
        //TODO check security
        //TODO find item in DB if exists
        return itemRepository.save(new ItemEntity(name: name, price: price))
                             .map({ ItemEntity item -> createOrderedItem(user, item, receiptId) } as Function)
                             .map({ it as OrderedItem })
    }

    Mono<OrderedItemEntity> addItem(User user, String receiptId, String itemId) {
        return itemRepository.findById(itemId)
                             .map({ ItemEntity item -> createOrderedItem(user, item, receiptId) } as Function)
    }

    private Mono<OrderedItemEntity> createOrderedItem(User user, ItemEntity item, String receiptId) {
        return receiptRepository.
                findById(receiptId).
                flatMap({ ReceiptEntity receipt ->
                    receipt.memberIds << String.valueOf(receiptId)
                    receiptRepository.save(receipt)
                } as Function).
                then(orderItemRepository.save(new OrderedItemEntity(user.id, item.id)))
    }
}