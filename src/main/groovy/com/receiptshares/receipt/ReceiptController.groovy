package com.receiptshares.receipt

import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import static com.receiptshares.receipt.model.ItemStatus.ACTIVE

@RestController
@RequestMapping("/v1/rec")
class ReceiptController {

    ReceiptService receiptService

    @Autowired
    ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService
    }

    @GetMapping(value = '/{id}', produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<Collection<OrderedItem>> receiptById(@PathVariable(name = "id") String id) {
        return receiptService.findById(id)
                             .map({ it.orderedItems.findAll({ it.status == ACTIVE }) })
    }

    @GetMapping(value = '/all', produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Flux<Receipt> allReceipts(Authentication user) {
        //TODO return only common data
        return receiptService.receiptsForUser(user.principal.person as String)
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<Receipt> createNew(Authentication auth, @RequestBody Map requestBody) {
        def user = auth.principal as User
        Collection<String> memberIds = requestBody.members
        return receiptService.createNewReceipt(requestBody.place.name as String, user.person.id, requestBody.name as String, memberIds)
    }

    @PostMapping(value = "/new-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    Mono<OrderedItem> createNewItem(Authentication auth, @RequestBody Map body) {
        def user = auth.principal as User
        def receiptId = body.receiptId as String
        def name = body.name as String
        def price = body.price as Double
        return receiptService.createNewItem(user.person.id, receiptId, name, price)
    }

    @PostMapping("/item/add")
    Mono<Void> addItem(Authentication auth, @RequestBody Map body) {
        return receiptService.addItem(auth.principal.person.id as String, body.receiptId as String, body.itemId as String)
                             .then()
    }
}
