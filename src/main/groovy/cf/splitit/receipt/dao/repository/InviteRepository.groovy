package cf.splitit.receipt.dao.repository

import cf.splitit.receipt.dao.InviteEntity
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface InviteRepository extends ReactiveCrudRepository<InviteEntity, String> {

}
