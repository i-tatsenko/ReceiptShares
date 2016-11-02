package com.receiptshares.receipt

import com.receiptshares.receipt.model.Receipt
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

    @RequestMapping(value = '/current', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def current(Authentication user) {
        return receiptService.currentReceiptsForUser(user.principal as User)
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    def createNew(Authentication auth, @RequestBody Receipt receipt) {
        def user = auth.principal as User
        receipt.place.author = user
        return receiptService.createNewReceipt(receipt.place, user as User, receipt.name, receipt.members)
    }
}
