package uminus.clone.twitter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import uminus.clone.twitter.model.User

typealias Token = String

const val KEY_TOKEN_SECRET = "TOKEN_SECRET"
private val ALGORITHM = Algorithm.HMAC256(System.getenv(KEY_TOKEN_SECRET) ?: "uminus.clone.twitter")
private val VERIFIER = JWT.require(ALGORITHM)
    .withIssuer("auth0")
    .build()

fun token(user: User): Token {
    return JWT.create()
        .withClaim("user", user.id.toString())
        .withIssuer("auth0")
        .sign(ALGORITHM)
}

fun verify(token: Token): DecodedJWT {
    return VERIFIER.verify(token)
}