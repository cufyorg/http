package org.cufy.http.ext

import kotlinx.coroutines.runBlocking
import org.cufy.http.SuspendHttp.fetch
import org.cufy.http.SuspendPerform.SUSPEND
import org.cufy.http.body
import org.cufy.http.client.Http.fetch
import org.cufy.http.client.Perform.SYNC
import org.cufy.http.okhttp.okHttpMiddleware
import org.cufy.http.response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoroutineTest {
    @Test
    fun main() {
        runBlocking {
            val suspendCursor =
                fetch(SYNC, "GET", "https://example.com", okHttpMiddleware())
            val syncCursor =
                fetch(SUSPEND, "GET", "https://example.com", okHttpMiddleware())

            println(suspendCursor.response)
            assertEquals(
                suspendCursor.body,
                syncCursor.body
            )
        }
    }
}
