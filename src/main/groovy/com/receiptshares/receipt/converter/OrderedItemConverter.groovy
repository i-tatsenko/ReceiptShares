package com.receiptshares.receipt.converter

import com.receiptshares.DuckTypeConversion
import com.receiptshares.receipt.model.OrderedItem

trait OrderedItemConverter extends DuckTypeConversion {

    def asType(Class _class) {
        OrderedItem result = super.asType(_class)
        result.total = result.count * result.item.price
        return result
    }

}