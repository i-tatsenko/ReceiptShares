package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion
import cf.splitit.user.model.Person
import groovy.transform.CompileStatic

@CompileStatic
class OrderedItem implements DuckTypeConversion {

    String id
    Person owner
    Item item
    ItemStatus status
    int count
    double total

}
