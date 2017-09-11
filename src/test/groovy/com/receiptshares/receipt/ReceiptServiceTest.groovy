package com.receiptshares.receipt

import com.receiptshares.MockitoExtension
import com.receiptshares.receipt.dao.repository.ItemRepository
import com.receiptshares.receipt.dao.repository.OrderItemRepository
import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.repository.ReceiptRepository
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserRepository
import com.receiptshares.user.model.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import reactor.core.publisher.Flux
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
}
