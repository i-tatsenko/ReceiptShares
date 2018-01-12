package cf.splitit.receipt.dao

import cf.splitit.places.dao.PlaceEntity
import cf.splitit.receipt.converter.ReceiptConverter
import cf.splitit.user.dao.PersonEntity
import groovy.transform.CompileStatic
import groovy.transform.builder.Builder
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "receipts")
@CompileStatic
@Builder
class ReceiptEntity implements ReceiptConverter {

    @Id
    String id
    String name
    PlaceEntity place
    PersonEntity owner
    List<PersonEntity> members
    List<OrderedItemEntity> orderedItems
    String status
    String inviteLink

}
