package com.receiptshares.receipt

import com.receiptshares.receipt.model.Invite
import com.receiptshares.receipt.model.Receipt
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
    Mono<Receipt> accept(Authentication auth, @RequestParam("inviteId") String inviteId) {
        inviteService.accept(auth.principal.person.id, inviteId)
    }

    @GetMapping("/{inviteId}")
    @ResponseBody
    Mono<Invite> inviteForId(@PathVariable("inviteId") String inviteId) {
        return inviteService.findById(inviteId)
    }
}
