package uminus.clone.twitter

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import uminus.clone.twitter.graphql.graphQLModule

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        graphQLModule()
    }.start(wait = true)
}
