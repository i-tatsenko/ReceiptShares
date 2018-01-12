package cf.splitit.receipt.converter

import cf.splitit.DuckTypeConversion
import cf.splitit.receipt.model.OrderedItem

trait OrderedItemConverter extends DuckTypeConversion {

    def asType(Class _class) {
        OrderedItem result = super.asType(_class)
        result.total = result.count * result.item.price
        return result
    }

}