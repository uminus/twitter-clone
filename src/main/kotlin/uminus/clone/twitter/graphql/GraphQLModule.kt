package uminus.clone.twitter.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import com.expediagroup.graphql.generator.toSchema
import graphql.ExecutionInput
import graphql.GraphQL
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLType
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.ZonedDateTime
import kotlin.reflect.KClass
import kotlin.reflect.KType


private val config = SchemaGeneratorConfig(
    supportedPackages = listOf("uminus.clone.twitter.graphql"),
    hooks = ExtendedScalarTypes()
)

class ExtendedScalarTypes : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? = when (type.classifier as? KClass<*>) {
        ZonedDateTime::class -> ExtendedScalars.DateTime
        else -> null
    }
}

private val queries = listOf(
    TopLevelObject(UserQueryService()),
    TopLevelObject(TweetQueryService()),
)

private val mutations = listOf(
    TopLevelObject(UserMutationService()),
    TopLevelObject(TweetMutationService()),
)

private val graphQLSchema = toSchema(config, queries, mutations)


fun getGraphQLObject(): GraphQL = GraphQL.newGraphQL(graphQLSchema).build()

fun Application.graphQLModule() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("graphql") {
            val req = call.receive<GraphQLRequest>()
            val result = getGraphQLObject().execute(
                ExecutionInput.newExecutionInput()
                    .operationName(req.operationName)
                    .variables(req.variables ?: emptyMap())
                    .query(req.query)
                    .build()
            )
            if (result.isDataPresent) {
                call.respond(HttpStatusCode.OK, result.toSpecification())
            } else {
                call.respond(HttpStatusCode.BadRequest, result.errors)
            }
        }
    }
}

data class GraphQLRequest(
    val query: String,
    val operationName: String? = null,
    val variables: Map<String, Any?>? = null
)
