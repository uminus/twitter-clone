package uminus.clone.twitter.graphql

import graphql.schema.idl.SchemaPrinter
import kotlin.test.Test

class GraphQLModuleKtTest {
    @Test
    fun `dump schema`() {
        println(SchemaPrinter().print(getGraphQLObject().graphQLSchema))
    }

    @Test
    fun test() {
        val gql = getGraphQLObject()
        val result = gql.execute("""
            query {tweets{id}}
        """.trimIndent())
    }
}