//package org.cufy.http.middleware.cache
//
//import org.cufy.http.model.Action
//import org.cufy.http.model.Client.*
//import org.cufy.http.kotlin.Client
//import org.cufy.http.kotlin.or
//import org.cufy.http.kotlin.plusAssign
//import org.cufy.http.middleware.cache.CacheMiddleware.cacheMiddleware
//import org.cufy.http.middleware.okhttp.OkHttpMiddleware.okHttpMiddleware
//import org.cufy.http.impl.UriImpl.uri
//import org.junit.Test
//
//class CacheTest {
//    @Test
//    fun main() {
//        Client {
//            this += okHttpMiddleware()
//            this += cacheMiddleware()
//
//            request.uri = uri(
//                "http://localhost/api/v2/categories"
//            )
//
//            on(Action.CONNECTED) { _, response ->
//                println(response)
//            }
//            on(Action.DISCONNECTED or Action.EXCEPTION) { _, exception ->
//                println(exception)
//            }
//
//            cached()
//        }
//
//        Thread.sleep(2_000)
//
//        Client {
//            this += okHttpMiddleware()
//            this += cacheMiddleware()
//
//            request.uri = uri(
//                "http://localhost/api/v2/categories"
//            )
//
//            on(Action.CONNECTED) { _, response ->
//                println(response)
//            }
//            on(Action.DISCONNECTED or Action.EXCEPTION) { _, exception ->
//                println(exception)
//            }
//
//            cached()
//        }
//
//        Thread.sleep(2_000)
//    }
//}
