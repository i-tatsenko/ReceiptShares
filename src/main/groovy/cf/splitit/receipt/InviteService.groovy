package cf.splitit.receipt

import cf.splitit.receipt.dao.InviteEntity
import cf.splitit.receipt.dao.ReceiptEntity
import cf.splitit.receipt.dao.repository.InviteRepository
import cf.splitit.receipt.dao.repository.ReceiptRepository
import cf.splitit.receipt.model.Invite
import cf.splitit.receipt.model.Receipt
import cf.splitit.user.dao.PersonEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class InviteService {

    InviteRepository inviteRepository
    ReceiptRepository receiptRepository
    @Value('${website.url}')
    String siteUrl

    @Autowired
    InviteService(InviteRepository inviteRepository, ReceiptRepository receiptRepository) {
        this.inviteRepository = inviteRepository
        this.receiptRepository = receiptRepository
    }

    Mono<String> createInviteLink(String receiptId, PersonEntity author) {
        createInvite(receiptId, author).map(this.&constructInviteLink)
    }

    Mono<Receipt> accept(String userId, String inviteId) {
        inviteRepository.findById(inviteId)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("There is no such invite")))
                        .flatMap({ addUserToReceipt(userId, it.receiptId) })
                        .map({ it as Receipt })
    }

    Mono<Invite> findById(String userId, String inviteId) {
        inviteRepository.findById(inviteId)
                        .flatMap(this.&mapToInvite)
                        .map({
            it.alreadyAccepted = it.receipt.owner.id == userId || it.receipt.members?.find({ member -> member.id == userId }) != null
            return it
        })
    }

    private Mono<Invite> mapToInvite(InviteEntity inviteEntity) {
        Invite result = inviteEntity as Invite
        return receiptRepository.findById(inviteEntity.receiptId)
                                .map({
            result.receipt = it as Receipt
            return result
        })
    }

    private Mono<InviteEntity> createInvite(String receiptId, PersonEntity author) {
        return inviteRepository.save(new InviteEntity(
                receiptId: receiptId,
                author: author,
                creationTime: new Date().getTime()))
    }

    private String constructInviteLink(InviteEntity invite) {
        return siteUrl + "/receipt/invite/" + invite.id
    }

    private Mono<ReceiptEntity> addUserToReceipt(String userId, String receiptId) {
        return receiptRepository.addUserToReceipt(receiptId, userId)
                                .then(receiptRepository.findById(receiptId))
    }
}
