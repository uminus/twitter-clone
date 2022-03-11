package uminus.clone.twitter

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import uminus.clone.twitter.graphql.graphQLModule

fun main() {
    connect()

    embeddedServer(Netty, port = (System.getenv("PORT") ?: "8080").toInt(), host = "0.0.0.0") {
        install(CORS) {
            host("*")
            header(HttpHeaders.ContentType)
            header(HttpHeaders.Authorization)
        }
        install(ContentNegotiation) {
            jackson()
        }
        graphQLModule()
        routing {
            resource("/", "index.html")
        }
    }.start(wait = true)
}
