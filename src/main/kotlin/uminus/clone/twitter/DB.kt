package uminus.clone.twitter

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.*
import java.util.*

fun connect() {
    Database.connect(
        System.getenv("DB_URL") ?: "jdbc:h2:mem:twitter-clone;DB_CLOSE_DELAY=-1",
        user = System.getenv("DB_USER") ?: "",
        password = System.getenv("DB_PASSWORD") ?: "",
    )
    transaction {
        SchemaUtils.create(Users, Followers, Tweets, Likes)
        if (System.getenv("DB_URL") == null) {
            insertDemoData()
        }
    }
}


private fun insertDemoData() {
    fun createUser(name: String): Pair<EntityID<UUID>, User> {
        val id = Users.insert {
            it[Users.name] = name
            it[Users.password] = "password".toByteArray()
            it[profile] = "PROFILE $name"
        } get Users.id

        return Pair(id, User[id])
    }

    fun createTweet(user: EntityID<UUID>, text: String): Pair<EntityID<UUID>, Tweet> {
        val id = Tweets.insert {
            it[Tweets.user] = user
            it[Tweets.text] = text
        } get Tweets.id

        return Pair(id, Tweet[id])
    }

    fun <T> toSizedCollection(vararg entitties: Pair<EntityID<UUID>, T>): SizedCollection<T> {
        return SizedCollection(entitties.map { it.second })
    }

    transaction {
        val user1 = createUser("1")
        val user2 = createUser("2")
        val user3 = createUser("3")

        user1.second.followers = toSizedCollection(user2)
        user2.second.followers = toSizedCollection(user1, user3)

        val tweet1 = createTweet(user1.first, "TWEET 1")
        val tweet2 = createTweet(user1.first, "TWEET 2")
        val tweet3 = createTweet(user1.first, "TWEET 3")
        val tweet4 = createTweet(user2.first, "TWEET 4")
        val tweet5 = createTweet(user2.first, "TWEET 5")

        tweet1.second.likes = toSizedCollection(user2)
        tweet2.second.likes = toSizedCollection(user2, user3)
    }
}