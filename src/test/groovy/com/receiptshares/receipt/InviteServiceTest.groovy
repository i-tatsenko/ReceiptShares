package com.receiptshares.receipt

import com.receiptshares.MockitoExtension
import com.receiptshares.receipt.dao.InviteEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.repository.InviteRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import com.receiptshares.user.dao.PersonEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.mockito.ArgumentMatchers.refEq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify
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
    String personId = "42"
    PersonEntity person = new PersonEntity(id: personId, name: "Tester")
    String receiptId = "receipt_id"
    long creationTime = new Date().time
    String site = "https://test.com"

    @BeforeEach
    void init() {
        underTest.siteUrl = site
        createInviteEntity()
    }

    @Test
    void "Should return link to the created invite"() {
        when(inviteRepository.save(refEq(inviteEntity, "id", "creationTime"))).thenReturn(Mono.just(inviteEntity))

        StepVerifier.create(underTest.createInviteLink(receiptId, person))
                .expectNext(site + '/receipt/invite/' + inviteId)
                .verifyComplete()
    }

    @Test
    void "Should return error when there is no invite for id"() {
        when(inviteRepository.findById(inviteId)).thenReturn(Mono.empty())

        StepVerifier.create(underTest.accept(personId, inviteId))
                .expectError(IllegalArgumentException)
                .verify()
    }

    @Test
    void "Should add user to receipt and return receipt"() {
        ReceiptEntity receipt = mock(ReceiptEntity)
        when(inviteRepository.findById(inviteId)).thenReturn(Mono.just(inviteEntity))
        when(receiptRepository.addUserToReceipt(receiptId, personId)).thenReturn(Mono.empty())
        when(receiptRepository.findById(receiptId)).thenReturn(Mono.just(receipt))

        StepVerifier.create(underTest.accept(personId, inviteId))
                .expectNext(receipt)
                .verifyComplete()

        verify(receiptRepository).addUserToReceipt(receiptId, personId)
    }

    void createInviteEntity() {
        inviteEntity = new InviteEntity(id: inviteId, author: person, receiptId: receiptId, creationTime: creationTime)
    }

}