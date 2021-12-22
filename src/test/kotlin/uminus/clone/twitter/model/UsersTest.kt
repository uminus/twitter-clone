package uminus.clone.twitter.model

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class UsersTest {
    @Test
    fun users() {
        Database.connect("jdbc:h2:mem:${UUID.randomUUID()}")
        transaction {
            SchemaUtils.create(Users, Followers)

            val user1 = Users.insert {
                it[name] = "1"
                it[profile] = "USER 1"
            } get Users.id

            val user2 = Users.insert {
                it[name] = "2"
                it[profile] = "USER 2"
            } get Users.id

            val user3 = Users.insert {
                it[name] = "3"
                it[profile] = "USER 3"
            } get Users.id

            val u = User[user1]
            u.followers = SizedCollection(listOf(User[user2], User[user3]))

            assertEquals("1", u.name)
            assertEquals("USER 1", u.profile)
            assertContentEquals(listOf("2", "3"), u.followers.map { it.name })
        }
    }
}