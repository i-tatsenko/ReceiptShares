package cf.splitit.receipt.converter

import cf.splitit.DuckTypeConversion
import cf.splitit.receipt.model.ItemStatus
import cf.splitit.receipt.model.Receipt

trait ReceiptConverter extends DuckTypeConversion {

    def asType(Class _class) {
        Receipt result = super.asType(_class) as Receipt
        result.orderedItems = result.orderedItems.findAll({it.status == ItemStatus.ACTIVE})
        result.total = result.orderedItems ? result.orderedItems.total.sum() : 0
        result.totalsPerMember = result.orderedItems.groupBy({it.owner.id}).collectEntries {k,v-> [k, v.total.sum()]}
        return result
    }
}
