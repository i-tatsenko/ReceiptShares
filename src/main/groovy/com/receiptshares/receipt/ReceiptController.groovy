package com.receiptshares.receipt

import com.receiptshares.receipt.model.OrderedItem
import com.receiptshares.receipt.model.Place
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
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

    @RequestMapping(value = '/{id}', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def receiptById(@PathVariable(name = "id") Long id) {
        return receiptService.findById(id)
    }

    @RequestMapping(value = '/all', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def allReceipts(Authentication user) {
        //TODO return only common data
        return receiptService.receiptsForUser(user.principal as User)
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNew(Authentication auth, @RequestBody Map requestBody) {
        def user = auth.principal as User
        def place = new Place(name: requestBody.place.name)
        Collection<Long> memberIds = requestBody.members.collect({it.id as Long})
        def newReceiptId = receiptService.createNewReceipt(place, user as User, requestBody.name as String, memberIds)
        return [id: newReceiptId]
    }

    @RequestMapping(value = "/new-item", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNewItem(Authentication auth, params) {
        def user = auth.principal as User
        def receiptId = params.receptId as Long
        def name = params.name as String
        def price = params.price as Double
        return receiptService.createNewItem(user, receiptId, name, price) as OrderedItem
    }
}
