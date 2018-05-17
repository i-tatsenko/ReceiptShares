package cf.splitit.receipt

import cf.splitit.receipt.dao.OrderedItemModificationEntity
import cf.splitit.receipt.dao.repository.OrderedItemModificationRepository
import cf.splitit.receipt.model.OrderedItemModification
import cf.splitit.receipt.model.OrderedItemModificationType
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import reactor.core.publisher.EmitterProcessor
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Slf4j
class OrderedItemModificationService {

    OrderedItemModificationRepository orderedItemModificationRepository
    private EmitterProcessor<OrderedItemModification> emitter
    private Flux<OrderedItemModification> itemModificationFlux

    @Autowired
    OrderedItemModificationService(OrderedItemModificationRepository orderedItemModificationRepository) {
        this.orderedItemModificationRepository = orderedItemModificationRepository
        emitter = EmitterProcessor.create()
        itemModificationFlux = emitter.publish().refCount(1)

    }

    Mono<OrderedItemModification> logChange(String receiptId, String orderedItemId, OrderedItemModificationType type, Integer count = null) {
        def result = new OrderedItemModificationEntity(
                receiptId: receiptId,
                orderedItemId: orderedItemId,
                modificationType: type,
                countChange: count,
                created: (System.currentTimeMillis())
        )
        return orderedItemModificationRepository.save(result)
                                                .map({ it as OrderedItemModification })
                                                .doOnNext({ emitter.onNext(it) })
    }

    Flux<OrderedItemModification> listen() {
        return itemModificationFlux
    }
}
