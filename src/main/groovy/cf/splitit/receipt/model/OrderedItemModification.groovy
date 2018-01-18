package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion

import java.time.LocalDateTime

class OrderedItemModification implements DuckTypeConversion {

        String id
        String orderedItemId
        OrderedItemModificationType modificationType
        Integer countChange
        LocalDateTime created

}
