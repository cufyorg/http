/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.client.wrapper

import org.cufy.http.client.ClientTask
import org.cufy.http.concurrent.cursor.performSuspend

/** An alias for [ClientWrapper.client] */
var <I, O, T : ClientWrapper<I, O, *>> T.client
    get() = client()
    set(v) {
        client(v)
    }

/**
 * A suspend version of [ClientReq.connect].
 */
suspend fun <E, T : ClientReq<E>> T.connectSuspend() =
    this.performSuspend(ClientTask.CONNECT)

/**
 * A suspend version of [ClientRes.connect].
 */
suspend fun <E, T : ClientRes<E>> T.connectSuspend() =
    this.req().connectSuspend()