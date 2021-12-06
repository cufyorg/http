package org.cufy.http.ext

import kotlinx.coroutines.runBlocking
import org.cufy.http.Http
import org.cufy.http.HttpSuspend
import org.cufy.http.body
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoroutineTest {
    @Test
    fun main() {
        runBlocking {
            val suspendCursor =
                HttpSuspend.fetch("GET", "https://example.com", okHttpMiddleware())
            val syncCursor =
                Http.fetch("GET", "https://example.com", okHttpMiddleware())

            assertEquals(
                suspendCursor.body,
                syncCursor.body
            )
        }
    }
}
