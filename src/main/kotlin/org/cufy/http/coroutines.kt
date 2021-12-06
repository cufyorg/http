/*
 *	Copyright 2021 Cufy and AgileSA
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
package org.cufy.http

import org.cufy.http.Action.*
import org.cufy.http.cursor.Cursor
import org.cufy.http.cursor.ResponseCursor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun Cursor<*>.suspend(): ResponseCursor<*> {
    return suspendCoroutine { continuation ->
        on(EXCEPTION) {
            continuation.resumeWithException(it)
        }
        on(DISCONNECTED) {
            continuation.resumeWithException(it.exception!!)
        }
        on(CONNECTED) {
            continuation.resume(it)
        }
        connect()
    }
}

suspend fun <T> Cursor<*>.suspend(performAction: Action<*>, suspendAction: Action<T>): T {
    return suspendCoroutine { continuation ->
        on(suspendAction) { parameter ->
            continuation.resume(parameter)
        }
        perform(performAction)
    }
}
