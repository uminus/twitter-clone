package uminus.clone.twitter.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query

data class UserData(val id: String, val token: String)

class UserQueryService : Query {
    fun users(id: String?): Array<UserData> {
        return arrayOf(UserData("dummy", "DUMMY"))
    }
}

class UserMutationService : Mutation {
    fun signup(id: String, password: String): UserData {
        return login(id, password)
    }

    fun login(id: String, password: String): UserData {
        return UserData("dummy", "DUMMY")
    }

    fun logout(token: String): Boolean {
        return true
    }
}