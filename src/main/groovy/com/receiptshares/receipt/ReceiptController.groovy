package com.receiptshares.receipt

import com.receiptshares.receipt.model.Place
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/rec")
class ReceiptController {

    def ReceiptService receiptService

    @Autowired
    ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService
    }

    @RequestMapping(value = '/all', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def current(Authentication user) {
        return receiptService.receiptsForUser(user.principal as User)
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNew(Authentication auth, @RequestBody Map requestBody) {
        def user = auth.principal as User
        def place = new Place(name: requestBody.place.name)
        Collection<Long> memberIds = requestBody.members.collect({it.id as Long})
        return receiptService.createNewReceipt(place, user as User, requestBody.name as String, memberIds)
    }
}
