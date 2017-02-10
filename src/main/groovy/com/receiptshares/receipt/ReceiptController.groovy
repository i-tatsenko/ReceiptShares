package com.receiptshares.receipt

import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Place
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

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
    def receiptById(@PathVariable(name = "id") Long id) {
        def receipt = receiptService.findById(id)
        receipt.orderedItems = receipt.orderedItems.findAll {it.status == "ACTIVE"}
        return receipt
    }

    @GetMapping(value = '/all', produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def allReceipts(Authentication user) {
        //TODO return only common data
        return receiptService.receiptsForUser(user.principal as User)
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNew(Authentication auth, @RequestBody Map requestBody) {
        def user = auth.principal as User
        def place = new Place(name: requestBody.place.name)
        Collection<Long> memberIds = requestBody.members.collect({it.id as Long})
        def newReceiptId = receiptService.createNewReceipt(place, user as User, requestBody.name as String, memberIds)
        return [id: newReceiptId]
    }

    @PostMapping(value = "/new-item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNewItem(Authentication auth, @RequestBody Map body) {
        def user = auth.principal as User
        def receiptId = body.receiptId as Long
        def name = body.name as String
        def price = body.price as Double
        return receiptService.createNewItem(user, receiptId, name, price) as OrderedItem
    }

    @PostMapping("/item/add")
    def addItem(Authentication auth, @RequestBody Map body) {
        receiptService.addItem(auth.principal as User, body.receiptId as Long, body.itemId as Long)
        return ResponseEntity.ok().build()
    }
}
