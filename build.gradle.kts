plugins {
    kotlin("jvm") version "1.6.10"
    id("com.apollographql.apollo3").version("3.2.2")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cz.speedy"
version = "1.0.0"

apollo {
    schemaFile.set(file("src/main/graphql/cz/speedy/clankipedia/schema.graphqls"))
    packageName.set("cz.speedy.clankipediabot.api")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "cz.speedy.clankipediabot.ClankipediaBotKt"
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-alpha.9")
    implementation("com.moandjiezana.toml:toml4j:0.7.2")
    implementation("com.apollographql.apollo3:apollo-runtime:3.2.2")
    implementation("org.apache.logging.log4j:log4j:2.17.2")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.17.2")
}