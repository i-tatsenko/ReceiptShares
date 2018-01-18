package cf.splitit.receipt.dao

import cf.splitit.DuckTypeConversion
import cf.splitit.receipt.model.OrderedItemModificationType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

@Document(collection = "orderedItemsModifications")
class OrderedItemModificationEntity implements DuckTypeConversion {

    @Id
    String id
    String orderedItemId
    OrderedItemModificationType modificationType
    Integer countChange
    LocalDateTime created

}
