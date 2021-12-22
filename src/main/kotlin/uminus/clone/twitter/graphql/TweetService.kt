package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.Tweet
import java.time.ZoneOffset
import java.time.ZonedDateTime

data class TweetData(
    val id: String,
    val text: String,
    val created_at: ZonedDateTime,
    val user: UserData?,
    val likes: Array<UserData>
)

class TweetQueryService : Query {
    fun tweets(
        tweetIds: Array<String>? = emptyArray(),
        userIds: Array<String>? = emptyArray(),
        offset: Int? = 0,
        limit: Int? = 10
    ): Array<TweetData> {
        return transaction {
            Tweet.all().map { toTweetData(it) }.toTypedArray()
        }

    }

    private fun toTweetData(tweet: Tweet): TweetData {
        return TweetData(
            tweet.id.toString(),
            tweet.text,
            tweet.createdAt.atZone(ZoneOffset.UTC),
            UserData("dummy", "dummy", null, "DUMMY"),
            arrayOf()
        )
    }
}

class TweetMutationService : Mutation {
    fun tweet(text: String): TweetData {
        return TweetData(
            "dummy",
            "dummy text",
            ZonedDateTime.now(),
            UserData("dummy", "dummy", null, "DUMMY"),
            arrayOf()
        )
    }

    fun like(tweetId: String): TweetData {
        return TweetData(
            "dummy",
            "dummy text", ZonedDateTime.now(),
            UserData("dummy", "dummy", null, "DUMMY"),
            arrayOf(
                UserData("dummy", "dummy", null, "DUMMY"),
                UserData("dummy", "dummy", null, "DUMMY")
            )
        )
    }
}