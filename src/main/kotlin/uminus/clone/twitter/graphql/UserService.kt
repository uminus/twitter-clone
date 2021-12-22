package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import org.jetbrains.exposed.sql.transactions.transaction
import uminus.clone.twitter.model.User
import java.util.*

data class UserData(val id: String, val name: String, val profile: String?, val token: String)

class UserQueryService : Query {
    fun users(id: String? = null): Array<UserData> {
        return transaction {
            if (id != null) {
                val user = User[UUID.fromString(id)]
                arrayOf(toUserData(user))
            } else {
                User.all().map { toUserData(it) }.toTypedArray()
            }
        }
    }

    private fun toUserData(user: User): UserData {
        return UserData(
            user.id.toString(),
            user.name,
            user.profile,
            "DUMMY"
        )
    }
}

class UserMutationService : Mutation {
    fun signup(id: String, password: String): UserData {
        return login(id, password)
    }

    fun login(id: String, password: String): UserData {
        return UserData("dummy", "dummy", null, "DUMMY")
    }

    fun logout(token: String): Boolean {
        return true
    }
}