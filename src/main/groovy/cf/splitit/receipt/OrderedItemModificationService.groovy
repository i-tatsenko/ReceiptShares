package cf.splitit.receipt

import cf.splitit.receipt.dao.OrderedItemModificationEntity
import cf.splitit.receipt.dao.repository.OrderedItemModificationRepository
import cf.splitit.receipt.model.OrderedItemModification
import cf.splitit.receipt.model.OrderedItemModificationType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import java.time.LocalDateTime

@Service
class OrderedItemModificationService {

    OrderedItemModificationRepository orderedItemModificationRepository
    private Flux<OrderedItemModificationEntity> modificationStream

    @Autowired
    OrderedItemModificationService(OrderedItemModificationRepository orderedItemModificationRepository) {
        this.orderedItemModificationRepository = orderedItemModificationRepository
        orderedItemModificationRepository.tai
    }

    Mono<OrderedItemModification> logChange(String orderedItemId, OrderedItemModificationType type, Integer count = null){
        def result = new OrderedItemModificationEntity(
                orderedItemId: orderedItemId,
                modificationType: type,
                countChange: count,
                created: LocalDateTime.now()
        )
        return orderedItemModificationRepository.save(result).map({ it as OrderedItemModification })
    }
}
