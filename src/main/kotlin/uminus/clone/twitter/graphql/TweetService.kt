package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import java.time.ZonedDateTime

data class TweetData(val text: String, val created_at: ZonedDateTime, val user: UserData?, val likes: Array<UserData>)

class TweetQueryService : Query {
    fun tweets(
        tweetIds: Array<String>? = emptyArray(),
        userIds: Array<String>? = emptyArray(),
        offset: Int? = 0,
        limit: Int? = 10
    ): Array<UserData> {
        return arrayOf(UserData("dummy", "DUMMY"))
    }
}

class TweetMutationService : Mutation {
    fun tweet(text: String): TweetData {
        return TweetData("dummy text", ZonedDateTime.now(), UserData("dummy", "DUMMY"), arrayOf())
    }

    fun like(tweetId: String): TweetData {
        return TweetData(
            "dummy text", ZonedDateTime.now(),
            UserData("dummy", "DUMMY"),
            arrayOf(
                UserData("dummy1", "DUMMY"),
                UserData("dummy2", "DUMMY")
            )
        )
    }
}