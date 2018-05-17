package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion

class OrderedItemModification implements DuckTypeConversion {

        String id
        String receiptId
        String orderedItemId
        OrderedItemModificationType modificationType
        Integer countChange
        long created

}
