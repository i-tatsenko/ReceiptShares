package cf.splitit.receipt.dao

import cf.splitit.receipt.converter.OrderedItemConverter
import cf.splitit.receipt.model.ItemStatus
import cf.splitit.user.dao.PersonEntity
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "orderedItems")
@CompileStatic
@InheritConstructors
@ToString
class OrderedItemEntity implements OrderedItemConverter {

    @Id
    String id
    PersonEntity owner
    ItemEntity item
    String status
    int count

    OrderedItemEntity(PersonEntity owner, ItemEntity item) {
        this.owner = owner
        this.item = item
        this.status = ItemStatus.ACTIVE.toString()
        this.count = 1
    }
}
