package org.cufy.http.middleware.cache

import org.cufy.http.connect.Client.*
import org.cufy.http.kotlin.Client
import org.cufy.http.kotlin.or
import org.cufy.http.kotlin.plusAssign
import org.cufy.http.middleware.cache.CacheMiddleware.cacheMiddleware
import org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware
import org.cufy.http.uri.Uri.uri
import org.junit.Test

class CacheTest {
    @Test
    fun main() {
        Client {
            this += okHttpMiddleware()
            this += cacheMiddleware()

            request.uri = uri("http://localhost/api/v2/categories")

            on(CONNECTED) { _, response ->
                println(response)
            }
            on(DISCONNECTED or EXCEPTION) { _, exception ->
                println(exception)
            }

            cached()
        }

        Thread.sleep(2_000)

        Client {
            this += okHttpMiddleware()
            this += cacheMiddleware()

            request.uri = uri("http://localhost/api/v2/categories")

            on(CONNECTED) { _, response ->
                println(response)
            }
            on(DISCONNECTED or EXCEPTION) { _, exception ->
                println(exception)
            }

            cached()
        }

        Thread.sleep(2_000)
    }
}
