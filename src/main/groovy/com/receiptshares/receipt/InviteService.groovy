package com.receiptshares.receipt

import com.receiptshares.receipt.dao.InviteEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.repository.InviteRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
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

    Mono<String> getOrCreateInviteLink(String receiptId, String authorId) {
        inviteRepository.findByReceiptIdAndAuthorId(receiptId, authorId)
                        .switchIfEmpty(createInvite(receiptId, authorId))
                        .map(this.&constructInviteLink)
    }

    private Mono<InviteEntity> createInvite(String receiptId, String authorId) {
        Mono.defer({
            Mono.just(new InviteEntity(
                    receiptId: receiptId,
                    authorId: authorId,
                    creationTime: new Date().getTime()))
        })
            .flatMap(inviteRepository.&save)
    }

    private String constructInviteLink(InviteEntity invite) {
        return siteUrl + "/receipt/${invite.receiptId}/invite/" + invite.id + "/" + invite.creationTime
    }

    Mono<ReceiptEntity> accept(String userId, String inviteId, long creationTime) {
        inviteRepository.findByIdAndCreationTime(inviteId, creationTime)
                        .switchIfEmpty(Mono.error(new IllegalArgumentException("There is no such invite")))
                        .flatMap({ addUserToReceipt(userId, it.receiptId) })
    }

    private Mono<ReceiptEntity> addUserToReceipt(String userId, String receiptId) {
        return receiptRepository.addUserToReceipt(receiptId, userId)
                                .then(receiptRepository.findById(receiptId))
    }
}
