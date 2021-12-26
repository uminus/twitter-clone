package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.User
import uminus.clone.twitter.model.Users
import uminus.clone.twitter.token
import java.util.*
import uminus.clone.twitter.verify as verifyToken

data class UserData(
    val id: String,
    val name: String,
    val profile: String?,
    val followers: Array<String> = emptyArray(),
    val token: String?
)

class UserQueryService : Query {
    fun users(dfe: DataFetchingEnvironment, id: String? = null): Array<UserData> {
        val context = dfe.getLocalContext<Map<String, String>>()
        if (!context.containsKey("user")) {
            return emptyArray()
        }

        return transaction {
            if (id != null) {
                val user = User[UUID.fromString(id)]
                arrayOf(toUserData(user))
            } else {
                User.all().map { toUserData(it) }.toTypedArray()
            }
        }
    }
}

fun toUserData(user: User, token: String? = null): UserData {
    return UserData(
        user.id.toString(),
        user.name,
        user.profile,
        // FIXME WORKAROUND infinite loop
        if(user.followers != null ) { user.followers.map { it.id.toString() }.toTypedArray() } else emptyArray(),
        token
    )
}

class UserMutationService : Mutation {
    fun signup(name: String, password: String): UserData {
        transaction {
            if (User.find(Users.name eq name).count() > 0) {
                throw Exception("TODO already exists.")
            }

            Users.insert {
                it[Users.name] = name
                it[Users.password] = password.toByteArray()
            }
        }
        return login(name, password)
    }

    fun verify(token: String): UserData {
        val verified = verifyToken(token)
        val user = transaction {
            User.get(UUID.fromString(verified.claims["user"]!!.asString()))
        }
        return toUserData(user, token(user))
    }

    fun login(name: String, password: String): UserData {
        val user = transaction {
            User.find { (Users.name eq name) and (Users.password eq password.toByteArray()) }
                .first()
        }
        return toUserData(user, token(user))
    }

    fun logout(dfe: DataFetchingEnvironment): Boolean {
        val context = dfe.getLocalContext<Map<String, String>>()
        if (!context.containsKey("user")) {
            return false
        }
        return true
    }

    fun follow(dfe: DataFetchingEnvironment, userId: String): UserData {
        val context = dfe.getLocalContext<Map<String, String>>()
        if (!context.containsKey("user")) {
            throw Exception("Bad Request")
        }
        return transaction {
            val user = User[UUID.fromString(context["user"])]
            val followee = User[UUID.fromString(userId)]
            user.followers = SizedCollection(user.followers + followee)
            toUserData(user)
        }
    }
}