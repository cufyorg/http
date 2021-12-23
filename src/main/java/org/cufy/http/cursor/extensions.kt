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
package org.cufy.http.cursor

import org.cufy.http.Endpoint

/**
 * Shortcut for [Req.res].
 */
val <E : Endpoint, R : Res<E, T, *>, T : Req<E, R, *>> T.res: R get() = res()

/**
 * Shortcut for [Res.req].
 */
val <E : Endpoint, R : Req<E, T, *>, T : Res<E, R, *>> T.req: R get() = req()

/**
 * Returns this on the first element on destructuring.
 */
operator fun <E : Endpoint, R : Res<E, T, R>, T : Req<E, R, T>> T.component1(): T = this

/**
 * Returns [Req.res] on the second element on destructuring.
 */
operator fun <E : Endpoint, R : Res<E, T, R>, T : Req<E, R, T>> T.component2(): R = res()

/**
 * Returns [Res.req] on the first element on destructuring.
 */
operator fun <E : Endpoint, R : Req<E, T, R>, T : Res<E, R, T>> T.component1(): R = req()

/**
 * Returns this on the second element on destructuring.
 */
operator fun <E : Endpoint, R : Req<E, T, R>, T : Res<E, R, T>> T.component2(): T = this
