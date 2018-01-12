package cf.splitit.user.registration

import cf.splitit.DuckTypeConversion
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
class NewUserDTO implements DuckTypeConversion {

    def String name
    def String password
    def String email
    def String avatarUrl
}
