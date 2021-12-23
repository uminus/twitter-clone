package uminus.clone.twitter

import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables
import uk.org.webcompere.systemstubs.jupiter.SystemStub
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension
import uminus.clone.twitter.model.User
import uminus.clone.twitter.model.Users
import java.util.*

@ExtendWith(SystemStubsExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthorizationKtTest {

    @SystemStub
    private var environment: EnvironmentVariables? = null

    @Test
    fun jwt() {
        environment!!.set(KEY_TOKEN_SECRET, "UNIT TEST")
        assertEquals("UNIT TEST", System.getenv(KEY_TOKEN_SECRET))

        Database.connect("jdbc:h2:mem:${UUID.randomUUID()}")
        transaction {
            SchemaUtils.create(Users)

            val id = Users.insert {
                it[name] = "1"
            } get Users.id

            val token = token(User[id])
            val decoded = verify(token)
            assertEquals(id.toString(), decoded.claims["user"]!!.asString())

            assertThrows<SignatureVerificationException> {
                verify(token + "a")
            }
            assertThrows<JWTDecodeException> {
                verify("DEAD.BEEF.CAFE")
            }
        }
    }
}