package uminus.clone.twitter.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : UUIDTable() {
    val name = varchar("name", 64)
    val profile = text("profile").nullable()
}

object Followers : Table() {
    val user = reference("user", Users)
    val follow = reference("follow", Users)
}

class User(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(Users)

    var name by Users.name
    var profile by Users.profile
    var followers by User.via(Followers.follow, Followers.user)
}