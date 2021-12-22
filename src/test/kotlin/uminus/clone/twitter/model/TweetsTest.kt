package uminus.clone.twitter.model

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import java.util.*
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals


class TweetsTest {
    @Test
    fun tweets() {
        Database.connect("jdbc:h2:mem:${UUID.randomUUID()}", driver = "org.h2.Driver", user = "root", password = "")
        transaction {
            SchemaUtils.create(Users, Followers, Tweets, Likes)

            val user1 = Users.insert {
                it[name] = "1"
            } get Users.id

            val user2 = Users.insert {
                it[name] = "2"
            } get Users.id

            val user3 = Users.insert {
                it[name] = "3"
            } get Users.id

            val tweet1 = Tweets.insert {
                it[user] = user1
                it[text] = "unit test"
            } get Tweets.id

            val t = Tweet[tweet1]
            t.likes = SizedCollection(listOf(User[user2], User[user3]))

            assertEquals("unit test", t.text)
            assertEquals("1", t.user.name)
            assertContentEquals(listOf("2", "3"), t.likes.map { it.name })
        }
    }
}