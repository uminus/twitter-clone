package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.Tweet
import uminus.clone.twitter.model.Tweets
import uminus.clone.twitter.model.User
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

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
            val tweets = if (!tweetIds.isNullOrEmpty()) {
                Tweet.forIds(tweetIds.map(UUID::fromString))
            } else if (!userIds.isNullOrEmpty()) {
                val ids = Tweets.slice(Tweets.id).select { Tweets.user inList userIds.map { UUID.fromString(it) } }
                    .orderBy(Tweets.createdAt to SortOrder.DESC)
                    .limit(limit!!, offset!!.toLong())
                    .asIterable()
                Tweet.forIds(ids.map { it[Tweets.id].value })
            } else {
                val ids = Tweets.selectAll()
                    .orderBy(Tweets.createdAt to SortOrder.DESC)
                    .limit(limit!!, offset!!.toLong())
                Tweet.forIds(ids.map { it[Tweets.id].value })
            }
            tweets.map { toTweetData(it) }.toTypedArray()
        }
    }
}

private fun toTweetData(tweet: Tweet): TweetData {
    return TweetData(
        tweet.id.toString(),
        tweet.text,
        tweet.createdAt.atZone(ZoneOffset.UTC),
        toUserData(tweet.user),
        tweet.likes.map { toUserData(it) }.toTypedArray()
    )
}

class TweetMutationService : Mutation {
    fun tweet(dfe: DataFetchingEnvironment, text: String): TweetData {
        val context = dfe.getLocalContext<Map<String, String>>()
        if (!context.containsKey("user")) {
            throw Exception("Bad Request")
        }
        return transaction {
            val user = User[UUID.fromString(context["user"])]

            val tweet = Tweet.new {
                this.user = user
                this.text = text
            }
            toTweetData(tweet)
        }
    }

    fun like(dfe: DataFetchingEnvironment, tweetId: String): TweetData {
        val context = dfe.getLocalContext<Map<String, String>>()
        if (!context.containsKey("user")) {
            throw Exception("Bad Request")
        }
        return transaction {
            val user = User[UUID.fromString(context["user"])]
            val tweet = Tweet[UUID.fromString(tweetId)]
            tweet.likes = SizedCollection(tweet.likes + user)
            toTweetData(tweet)
        }
    }
}