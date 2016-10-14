package com.receiptshares.user.dao

import org.springframework.data.repository.CrudRepository

interface UserConnectionRepo extends CrudRepository<UserConnectionEntity, String> {

    Collection<UserConnectionEntity> findByProviderUserIdIn(Collection<String> providerUserId)
}
