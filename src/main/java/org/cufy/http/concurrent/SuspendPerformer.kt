package org.cufy.http.concurrent

import kotlinx.coroutines.runBlocking
import java.util.function.Consumer

/**
 * A suspendable version of [Performer].
 */
interface SuspendPerformer : Performer {
    override fun perform(
        block: Runnable, callbackConsumer: Consumer<Runnable>
    ) {
        runBlocking {
            performSuspend({ block.run() }, { callbackConsumer.accept(it) })
        }
    }

    /**
     * A suspendable version of [perform].
     */
    suspend fun performSuspend(
        block: (() -> Unit) -> Unit
    ) {
        var callback: (() -> Unit)? = null
        var state = false

        this.performSuspend(
            {
                if (callback == null)
                    error("Callback not provided")
                if (state)
                    error("Block already executed")

                state = true

                block(callback!!)
            },
            {
                if (callback != null)
                    error("Callback already provided")

                callback = it
            }
        )
    }

    /**
     * A suspendable version of [perform].
     */
    suspend fun performSuspend(
        block: () -> Unit, callbackConsumer: (() -> Unit) -> Unit
    )
}
