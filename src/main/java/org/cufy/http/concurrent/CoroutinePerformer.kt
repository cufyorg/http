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

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * A performer implementation that uses the coroutines and continuations to
 * operate.
 */
open class CoroutinePerformer : SuspendPerformer() {
    companion object : CoroutinePerformer()

    override suspend fun executeSuspend(
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
