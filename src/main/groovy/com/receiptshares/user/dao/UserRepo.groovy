package com.receiptshares.user.dao

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepo extends CrudRepository<UserEntity, Long>{

    @Query("FROM UserEntity WHERE email = ?")
    UserEntity findByEmail(String email)
}
