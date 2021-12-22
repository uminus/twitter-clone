val ktor_version: String by project
val exposed_version: String by project
val kotlin_version: String by project
val graphql_version: String by project
val graphql_extended_version: String by project
val logback_version: String by project
val h2_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "com.example"
version = "0.0.1"
application {
    mainClass.set("uminus.clone.twitter.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("com.expediagroup:graphql-kotlin-server:$graphql_version")
    implementation("com.graphql-java:graphql-java-extended-scalars:$graphql_extended_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("com.h2database:h2:$h2_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}