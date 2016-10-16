package com.receiptshares.user.dao

import org.springframework.data.repository.CrudRepository

interface UserRepo extends CrudRepository<UserEntity, Long>{

    UserEntity findByEmail(String email)

    List<UserEntity> findByEmailIn(Collection<String> emails)
}
