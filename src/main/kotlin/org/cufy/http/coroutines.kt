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
import org.cufy.http.cursor.RequestCursor
import org.cufy.http.cursor.ResponseCursor
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun fetchSuspend(vararg middlewares: Middleware) =
    Http.open(*middlewares).connectSuspend()

suspend fun fetchSuspend(method: Method, uri: Uri, vararg middlewares: Middleware) =
    Http.open(method, uri, *middlewares).connectSuspend()

suspend fun fetchSuspend(method: String, uri: String, vararg middlewares: Middleware) =
    Http.open(method, uri, *middlewares).connectSuspend()

suspend fun fetchSuspend(builder: (RequestCursor<*>) -> Unit) =
    Http.open(builder).connectSuspend()

suspend fun Cursor<*>.connectSuspend(): ResponseCursor<*> {
    return suspendCoroutine { continuation ->
        var resumed = false
        on(EXCEPTION) {
            if (!resumed) {
                resumed = true
                continuation.resumeWithException(it)
            }
        }
        on(DISCONNECTED) {
            if (!resumed) {
                resumed = true
                continuation.resumeWithException(it.exception!!)
            }
        }
        on(CONNECTED) {
            if (!resumed) {
                resumed = true
                continuation.resume(it)
            }
        }
        connect()
    }
}
