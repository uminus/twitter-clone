package uminus.clone.twitter.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*


object Tweets : UUIDTable() {
    val user = reference("user", Users)
    val text = text("text")
    val createdAt = timestamp("created_at")
        .default(ZonedDateTime.now(ZoneOffset.UTC).toInstant())
}

object Likes : Table() {
    val user = reference("user", Users)
    val tweet = reference("tweet", Tweets)
}

class Tweet(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Tweet>(Tweets)

    var user by User referencedOn Tweets.user
    var text by Tweets.text
    var createdAt by Tweets.createdAt
    var likes by User via Likes
}