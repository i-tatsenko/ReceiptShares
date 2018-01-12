package cf.splitit.receipt

import cf.splitit.receipt.dao.OrderedItemEntity
import cf.splitit.receipt.dao.ReceiptEntity
import cf.splitit.receipt.dao.repository.ItemRepository
import cf.splitit.receipt.dao.repository.OrderItemRepository
import cf.splitit.receipt.dao.repository.ReceiptRepository
import cf.splitit.receipt.exception.OrderedItemNotFound
import cf.splitit.receipt.exception.ReceiptNotFoundException
import cf.splitit.receipt.model.Receipt
import cf.splitit.user.model.User
import com.cf.splititres.MockitoExtension
import com.cf.splititres.user.dao.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

import static org.mockito.Mockito.when

@ExtendWith(MockitoExtension)
class ReceiptServiceTest {

    @Mock
    ReceiptRepository receiptRepository
    @Mock
    UserRepository userRepo
    @Mock
    OrderItemRepository orderItemRepository
    @Mock
    ItemRepository itemRepository

    @InjectMocks
    ReceiptService underTest

    String userId = "userId"
    @Mock
    ReceiptEntity receiptEntity
    @Mock
    Receipt receipt
    @Mock
    User user

    @BeforeEach
    void setup() {
        when(receiptEntity as Receipt).thenReturn(receipt)
        when(receiptRepository.findAllActiveReceipts(userId)).thenReturn(Flux.just(receiptEntity))
        when(user.getId()).thenReturn(userId)
    }

    @Test
    void shouldReturnListOfActiveReceipts() {
        def result = underTest.receiptsForUser(userId)
        StepVerifier.create(result)
                    .expectNext(receipt)
                    .verifyComplete()
    }

    @Nested
    @DisplayName("Clone functionality tests")
    class CloneItemTests {

        String missingReceiptId = "missing receipt id"
        String missingOrderedItemId = "missing ordered item id"
        String presentReceiptId = "present receipt id"
        String presentOrderedItemId = "present ordered item id"
        @Mock
        OrderedItemEntity orderedItemEntity

        @BeforeEach
        void setup() {
            when(receiptRepository.findById(missingReceiptId)).thenReturn(Mono.empty())
            when(orderItemRepository.findById(missingOrderedItemId)).thenReturn(Mono.empty())
            when(receiptRepository.findById(presentReceiptId)).thenReturn(Mono.just(receiptEntity))
            when(orderItemRepository.findById(presentOrderedItemId)).thenReturn(Mono.just(orderedItemEntity))
        }

        @Test
        void "should return error when there is no receipt for id"() {
            StepVerifier.create(underTest.cloneItem(userId, missingReceiptId, presentOrderedItemId))
                        .expectError(ReceiptNotFoundException.class)
                        .verify()
        }

        @Test
        void "should return error when there is no ordered item for id"() {
            StepVerifier.create(underTest.cloneItem(userId, presentReceiptId, missingOrderedItemId))
                        .expectError(OrderedItemNotFound.class)
                        .verify()
        }
    }


}
