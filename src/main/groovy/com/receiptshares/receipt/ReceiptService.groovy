package com.receiptshares.receipt

import com.receiptshares.receipt.dao.ReceiptEntity
import com.receiptshares.receipt.dao.ReceiptRepository
import com.receiptshares.receipt.model.Place
import com.receiptshares.receipt.model.Receipt
import com.receiptshares.user.dao.UserEntity
import com.receiptshares.user.dao.UserRepo
import com.receiptshares.user.model.User
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import static com.receiptshares.receipt.model.ReceiptStatus.ACTIVE

@Component
@Slf4j
class ReceiptService {

    def ReceiptRepository receiptRepository
    def UserRepo userRepo

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository, UserRepo userRepo) {
        this.receiptRepository = receiptRepository
        this.userRepo = userRepo
    }

    def Collection<Receipt> receiptsForUser(User user) {
        return receiptRepository.findAllActiveReceipts(user as UserEntity)
                                .collect { it as Receipt }
    }

    def createNewReceipt(Place place, User owner, String name, Collection<Long> members) {
        def newReceipt = new Receipt(place: place, owner: owner, status: ACTIVE, name: name)
        def newReceiptEntity = newReceipt as ReceiptEntity

        newReceiptEntity.members = userRepo.findAll(members).toSet()
        return receiptRepository.save(newReceiptEntity).id
    }

    def findById(Long id) {
        receiptRepository.findOne(id) as Receipt
    }
}
