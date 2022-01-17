package org.cufy.http.ext

import kotlinx.coroutines.runBlocking
import org.cufy.http.client.Http.fetch
import org.cufy.http.client.SuspendHttp.fetchSuspend
import org.cufy.http.concurrent.CoroutinePerformer
import org.cufy.http.concurrent.Performer
import org.cufy.http.okhttp.OkEngine
import org.cufy.http.wrapper.body
import org.cufy.http.wrapper.response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CoroutineTest {
    @Test
    fun main() {
        runBlocking {
            val suspendCursor =
                fetchSuspend(OkEngine, CoroutinePerformer, "GET", "https://example.com")
            val syncCursor =
                fetch(OkEngine, Performer.WAIT, "GET", "https://example.com")

            println(suspendCursor.response)
            assertEquals(
                suspendCursor.body,
                syncCursor.body
            )
        }
    }
}
