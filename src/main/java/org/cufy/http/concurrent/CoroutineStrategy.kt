package org.cufy.http.concurrent

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A performer implementation that uses the coroutines and continuations to
 * operate.
 */
open class CoroutineStrategy : SuspendStrategy() {
    companion object : CoroutineStrategy()

    override suspend fun performSuspend(
        block: () -> Unit, callbackConsumer: (() -> Unit) -> Unit
    ) {
        suspendCoroutine<Unit> { continuation ->
            callbackConsumer {
                continuation.resume(Unit)
            }

            block()
        }
    }
}
