package cf.splitit.receipt.model

import cf.splitit.DuckTypeCollectionMapping
import cf.splitit.DuckTypeConversion
import cf.splitit.places.model.Place
import cf.splitit.user.model.Person
import groovy.transform.CompileStatic

@CompileStatic
class Receipt implements DuckTypeConversion {

    String id
    String name
    Place place
    Person owner
    @DuckTypeCollectionMapping(itemType = Person)
    Set<Person> members
    @DuckTypeCollectionMapping(itemType = OrderedItem)
    Set<OrderedItem> orderedItems
    ReceiptStatus status
    double total
    Map<String, Double> totalsPerMember
    String inviteLink
}
