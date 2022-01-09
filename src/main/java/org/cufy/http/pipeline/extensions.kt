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
package org.cufy.http.pipeline

/** Combine the receiver with the [other] */
@JvmName("rightShift")
operator fun <T> Next<T>.plus(other: Next<T>): Next<T> =
    Next.combine(this, other)

/** Combine the receiver with the [other] */
@JvmName("rightShift")
operator fun <T> Middleware<in T>.plus(other: Middleware<in T>): Middleware<T> =
    Middleware.combine(this, other)

/** Combine the receiver with the [other] */
@JvmName("rightShift")
operator fun <T> Pipe<T>.plus(other: Pipe<T>): Pipe<T> =
    Pipe.combine(this, other)

/** Combine the receiver with the [other] */
@JvmName("rightShift")
operator fun <T> Interceptor<T>.plus(other: Interceptor<T>): Interceptor<T> =
    Interceptor.combine(this, other)
