package networking

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun createHttpClient(engine: HttpClientEngine): HttpClient =
    HttpClient(engine) {
        install(Logging) {
            level = LogLevel.ALL
            // custom logger
//            logger = object : Logger{
//                override fun log(message: String) {
//                    println(message)
//                }
        }

        install(ContentNegotiation) {
            json(
                json =
                    Json {
                        ignoreUnknownKeys = true
                    },
            )
        }

        // install auth
//        install(Auth){
//            bearer {
//                refreshTokens {
//
//                }
//            }
//        }
    }
