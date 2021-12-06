package org.cufy.http.ext

import kotlinx.coroutines.runBlocking
import org.cufy.http.Http.fetchSync
import org.cufy.http.body
import org.cufy.http.fetchSuspend
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoroutineTest {
    @Test
    fun main() {
        runBlocking {
            val suspendCursor =
                fetchSuspend("GET", "https://example.com", okHttpMiddleware())
            val syncCursor =
                fetchSync("GET", "https://example.com", okHttpMiddleware())

            assertEquals(
                suspendCursor.body,
                syncCursor.body
            )
        }
    }
}
