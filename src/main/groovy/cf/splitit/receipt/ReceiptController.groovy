package cf.splitit.receipt

import cf.splitit.places.model.Place
import cf.splitit.receipt.model.OrderedItem
import cf.splitit.receipt.model.OrderedItemModification
import cf.splitit.receipt.model.Receipt
import cf.splitit.user.model.User
import cf.splitit.web.response.SimpleValueResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/receipt")
class ReceiptController {

    ReceiptService receiptService
    OrderedItemModificationService orderedItemModificationService

    @Autowired
    ReceiptController(ReceiptService receiptService, OrderedItemModificationService orderedItemModificationService) {
        this.receiptService = receiptService
        this.orderedItemModificationService = orderedItemModificationService
    }

    @GetMapping(value = '/{id}', produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<Receipt> receiptById(@PathVariable(name = "id") String id) {
        return receiptService.findById(id)
    }

    @GetMapping(value = '/all', produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Flux<Receipt> allReceipts(Authentication user) {
        //TODO return only common data
        return receiptService.receiptsForUser(user.principal.person.id as String)
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<Receipt> createNew(Authentication auth, @RequestBody Map requestBody) {
        def user = auth.principal as User
        Collection<String> memberIds = requestBody.members
        return receiptService.createNewReceipt(new Place(requestBody.place), user.person.id, requestBody.name as String, memberIds)
    }

    @PostMapping(value = "/{id}/new-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<OrderedItem> createNewItem(Authentication auth, @RequestBody Map body, @PathVariable("id") String receiptId) {
        def user = auth.principal as User
        def name = body.name as String
        def price = body.price as Double
        return receiptService.createNewItem(user.person.id, receiptId, name, price)
    }

    @PostMapping("/{receiptId}/item/{orderedItemId}/increment")
    Mono<SimpleValueResponse<Boolean>> addItem(Authentication auth,
                                               @PathVariable("receiptId") String receiptId,
                                               @PathVariable("orderedItemId") String itemId,
                                               @RequestParam(value = "amount", required = false, defaultValue = "1") int amount) {
        return receiptService.incrementOrderedItem(auth.principal.person.id as String, receiptId, itemId, amount > 0)
                             .map({ new SimpleValueResponse<>(it) })
    }

    @PostMapping("/{receiptId}/item/{orderedItemId}/restore")
    Mono<Void> restoreOrderedItem(Authentication auth,
                                  @PathVariable("receiptId") String receiptId,
                                  @PathVariable("orderedItemId") String itemId) {
        return receiptService.restoreOrderedItem(auth.principal.person.id as String, receiptId, itemId)
    }

    @PostMapping("/{receiptId}/item/{orderedItemId}/clone")
    Mono<Void> cloneItem(Authentication auth,
                         @PathVariable("receiptId") String receiptId,
                         @PathVariable("orderedItemId") String orderedItemId) {
        return receiptService.cloneItem(auth.principal.person.id, receiptId, orderedItemId)
    }

    @GetMapping(value = "/{receiptId}/listen", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<OrderedItemModification> listen(@PathVariable("receiptId") String receiptId) {
        return orderedItemModificationService.listen().log().filter({it.receiptId == receiptId})
    }
}
