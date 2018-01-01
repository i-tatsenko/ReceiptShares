package com.receiptshares.receipt

import com.receiptshares.receipt.dao.InviteEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.repository.InviteRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import com.receiptshares.receipt.model.Invite
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.PersonEntity
import com.receiptshares.user.model.Person
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
