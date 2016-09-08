package com.receiptshares.user.dao

import org.springframework.data.repository.CrudRepository

interface UserRepo extends CrudRepository<UserEntity, Long>{
}
