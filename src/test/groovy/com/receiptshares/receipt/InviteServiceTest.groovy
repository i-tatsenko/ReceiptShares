package com.receiptshares.receipt

import com.receiptshares.MockitoExtension
import com.receiptshares.receipt.dao.InviteEntity
import com.receiptshares.receipt.dao.repository.InviteRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.anyString
import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class InviteServiceTest {

    @Mock
    InviteRepository inviteRepository
    @Mock
    ReceiptRepository receiptRepository

    @InjectMocks
    InviteService underTest

    InviteEntity inviteEntity
    String inviteId = "invite_id"
    String userId = "user_id"
    String receiptId = "receipt_id"
    long creationTime = new Date().time
    String site = "https://test.com"

    @BeforeEach
    void init() {
        underTest.siteUrl = site
        createInviteEntity()
    }

    @Test
    void "should return link from existing invite"() {
        when(inviteRepository.findByReceiptIdAndAuthorId(receiptId, userId)).thenReturn(Mono.just(inviteEntity))

        StepVerifier.create(underTest.getOrCreateInviteLink(receiptId, userId))
                    .expectNext(site + "/receipt/${receiptId}/invite/${inviteId}/${creationTime}")
                    .verifyComplete()
    }

    @Test
    void "should create if no invite found for receipt"() {
        when(inviteRepository.findByReceiptIdAndAuthorId(anyString(), anyString())).thenReturn(Mono.empty())
        when(inviteRepository.save(any())).thenReturn(Mono.just(inviteEntity))

        StepVerifier.create(underTest.getOrCreateInviteLink(receiptId, userId))
                    .expectNext(site + "/receipt/${receiptId}/invite/${inviteId}/${creationTime}")
                    .verifyComplete()
    }

    void createInviteEntity() {
        inviteEntity = new InviteEntity(id: inviteId, authorId: userId, receiptId: receiptId, creationTime: creationTime)
    }

}