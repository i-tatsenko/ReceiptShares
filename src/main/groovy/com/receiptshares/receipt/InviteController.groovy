package com.receiptshares.receipt

import com.receiptshares.receipt.dao.ReceiptEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/invite")
class InviteController {

    InviteService inviteService

    @Autowired
    InviteController(InviteService inviteService) {
        this.inviteService = inviteService
    }

    @PostMapping("/accept")
    @ResponseBody
    Mono<ReceiptEntity> accept(Authentication auth, @RequestParam("inviteId") String inviteId, @RequestParam("time")long creationTime) {
        inviteService.accept(auth.principal.person.id, inviteId)
    }
}
