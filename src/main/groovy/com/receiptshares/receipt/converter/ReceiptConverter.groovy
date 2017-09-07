package com.receiptshares.receipt.converter

import com.receiptshares.DuckTypeConversion
import com.receiptshares.receipt.model.ItemStatus
import com.receiptshares.receipt.model.Receipt

trait ReceiptConverter extends DuckTypeConversion {

    def asType(Class _class) {
        Receipt result = super.asType(_class) as Receipt
        result.orderedItems = result.orderedItems.findAll({it.status == ItemStatus.ACTIVE})
        return result
    }
}
