package com.receiptshares.receipt

import com.receiptshares.user.model.User
import org.springframework.stereotype.Component

@Component
class ReceiptService {

    def Receipt currentReceiptForUser(User user) {
//        return new Receipt(owner: user, members: [user], orderedItems: [new OrderedItem(user: user, item: new Item(name: "Beer", price: 10.00))])
        return null;
    }
}
