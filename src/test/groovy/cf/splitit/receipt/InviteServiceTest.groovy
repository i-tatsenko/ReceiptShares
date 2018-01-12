package cf.splitit.receipt

import cf.splitit.MockitoExtension
import cf.splitit.receipt.dao.InviteEntity
import cf.splitit.receipt.dao.ReceiptEntity
import cf.splitit.receipt.dao.repository.InviteRepository
import cf.splitit.receipt.dao.repository.ReceiptRepository
import cf.splitit.receipt.model.Receipt
import cf.splitit.user.dao.PersonEntity
import cf.splitit.user.model.Person
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.assertj.core.api.Assertions.assertThat
import static org.mockito.ArgumentMatchers.refEq
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
    @Mock
    ReceiptEntity receiptEntity
    @Mock
    Receipt receipt
    long creationTime = new Date().time
    String site = "https://test.com"

    @BeforeEach
    void init() {
        underTest.siteUrl = site
        createInviteEntity()
        when(receiptEntity.asType(Receipt)).thenReturn(receipt)
    }

    @Test
    void "Should return link to the created invite"() {
        when(inviteRepository.save(refEq(inviteEntity, "id", "creationTime"))).thenReturn(Mono.just(inviteEntity))

        StepVerifier.create(underTest.createInviteLink(receiptId, person))
                    .expectNext(site + '/receipt/invite/' + inviteId)
                    .verifyComplete()
    }

    @Test
    void "Should set receipt and already invited to the invite"() {
        when(inviteRepository.findById(inviteId)).thenReturn(Mono.just(inviteEntity))
        when(receiptRepository.findById(receiptId)).thenReturn(Mono.just(receiptEntity))
        when(receipt.owner).thenReturn(new Person())

        StepVerifier.create(underTest.findById(personId, inviteId))
                    .assertNext({ invite ->
            assertThat(invite.receipt).isSameAs(receipt)
            assertThat(invite.alreadyAccepted).isFalse()
        })
                    .verifyComplete()
    }

    @Test
    void "Should set already invited field"() {
        when(inviteRepository.findById(inviteId)).thenReturn(Mono.just(inviteEntity))
        when(receiptRepository.findById(receiptId)).thenReturn(Mono.just(receiptEntity))
        when(receipt.members).thenReturn([new Person(id: personId)] as Set)
        when(receipt.owner).thenReturn(new Person())

        StepVerifier.create(underTest.findById(personId, inviteId))
                    .assertNext({ assertThat(it.alreadyAccepted).isTrue() })
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
        when(inviteRepository.findById(inviteId)).thenReturn(Mono.just(inviteEntity))
        when(receiptRepository.addUserToReceipt(receiptId, personId)).thenReturn(Mono.empty())
        when(receiptRepository.findById(receiptId)).thenReturn(Mono.just(receiptEntity))

        StepVerifier.create(underTest.accept(personId, inviteId))
                    .expectNext(receipt)
                    .verifyComplete()

        verify(receiptRepository).addUserToReceipt(receiptId, personId)
    }

    void createInviteEntity() {
        inviteEntity = new InviteEntity(id: inviteId, author: person, receiptId: receiptId, creationTime: creationTime)
    }

}