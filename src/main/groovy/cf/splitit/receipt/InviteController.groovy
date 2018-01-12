package cf.splitit.receipt

import cf.splitit.receipt.model.Invite
import cf.splitit.receipt.model.Receipt
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

    @GetMapping("/{inviteId}")
    @ResponseBody
    Mono<Invite> inviteForId(Authentication auth, @PathVariable("inviteId") String inviteId) {
        return inviteService.findById(auth.principal.person.id, inviteId)
    }

    @PostMapping("{inviteId}/accept")
    @ResponseBody
    Mono<Receipt> accept(Authentication auth, @PathVariable("inviteId") String inviteId) {
        inviteService.accept(auth.principal.person.id, inviteId)
    }
}
