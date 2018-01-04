package com.receiptshares.receipt

import com.receiptshares.MockitoExtension
import com.receiptshares.receipt.dao.OrderedItemEntity
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.repository.ItemRepository
import com.receiptshares.receipt.dao.repository.OrderItemRepository
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import com.receiptshares.receipt.exception.OrderedItemNotFound
import com.receiptshares.receipt.exception.ReceiptNotFoundException
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserRepository
import com.receiptshares.user.model.User
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
