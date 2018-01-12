package cf.splitit.receipt.model

import cf.splitit.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Item implements DuckTypeConversion {

    String id
    String name
    double price
}
