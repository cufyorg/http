/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
 *
 *	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *	    http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 *	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
package org.cufy.http.concurrent

import kotlinx.coroutines.runBlocking
import java.util.function.Consumer

/**
 * A suspendable version of [Strategy].
 */
abstract class SuspendStrategy : Strategy() {
    override fun execute(
        block: Runnable, callbackConsumer: Consumer<Runnable>
    ) {
        runBlocking {
            executeSuspend({ block.run() }, { callbackConsumer.accept(it) })
        }
    }

    /**
     * A suspendable version of [execute].
     */
    suspend fun executeSuspend(
        block: (() -> Unit) -> Unit
    ) {
        var callback: (() -> Unit)? = null
        var state = false

        this.executeSuspend(
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
     * A suspendable version of [execute].
     */
    abstract suspend fun executeSuspend(
        block: () -> Unit, callbackConsumer: (() -> Unit) -> Unit
    )
}
