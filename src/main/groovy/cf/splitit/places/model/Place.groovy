package cf.splitit.places.model

import cf.splitit.DuckTypeConversion
import groovy.transform.CompileStatic

@CompileStatic
class Place implements DuckTypeConversion {

    String id
    String provider
    String name
    String imageUrl
}
