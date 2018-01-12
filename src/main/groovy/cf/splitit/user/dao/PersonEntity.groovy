package cf.splitit.user.dao

import cf.splitit.DuckTypeConversion
import groovy.transform.CompileStatic
import groovy.transform.ToString
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@CompileStatic
@ToString
@Document(collection = "persons")
class PersonEntity implements DuckTypeConversion {

    @Id
    String id
    String name
    String avatarUrl
}
