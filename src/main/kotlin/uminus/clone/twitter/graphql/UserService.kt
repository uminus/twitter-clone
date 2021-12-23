package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.User
import uminus.clone.twitter.model.Users
import uminus.clone.twitter.token
import java.util.*

data class UserData(val id: String, val name: String, val profile: String?, val token: String?)

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

private fun toUserData(user: User, token: String? = null): UserData {
    return UserData(
        user.id.toString(),
        user.name,
        user.profile,
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

    fun login(name: String, password: String): UserData {
        val user = transaction {
            User.find { (Users.name eq name) and (Users.password eq password.toByteArray()) }
                .first()
        }
        return toUserData(user, token(user))
    }

    fun logout(): Boolean {
        return true
    }
}