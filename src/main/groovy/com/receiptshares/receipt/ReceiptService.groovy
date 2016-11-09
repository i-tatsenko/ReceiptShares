package com.receiptshares.receipt

import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.ReceiptRepository
import com.receiptshares.receipt.model.Place
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.model.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
class ReceiptService {

    def ReceiptRepository receiptRepository

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository
    }

    def Collection<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user as UserEntity)
                .collect { it as Receipt }
    }

    def createNewReceipt(Place place, User owner, String name, Collection<User> members) {
        def newReceipt = new Receipt(place: place, owner: owner, members: members, status: ACTIVE, name: name)
        receiptRepository.save(newReceipt as ReceiptEntity)
    }
}
