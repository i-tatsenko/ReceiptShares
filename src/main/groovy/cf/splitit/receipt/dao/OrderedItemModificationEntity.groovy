package cf.splitit.receipt.dao

import cf.splitit.DuckTypeConversion
import cf.splitit.receipt.model.OrderedItemModificationType
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "orderedItemsModifications")
@ToString
class OrderedItemModificationEntity implements DuckTypeConversion {

    @Id
    String id
    String receiptId
    String orderedItemId
    OrderedItemModificationType modificationType
    Integer countChange
    long created

}
