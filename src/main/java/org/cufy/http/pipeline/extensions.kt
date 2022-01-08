package org.cufy.http.pipeline

/** Combine the receiver with the [other] */
operator fun <T> Next<T>.plus(other: Next<T>): Next<T> =
    Next.combine(this, other)

/** Combine the receiver with the [other] */
operator fun <T> Middleware<in T>.plus(other: Middleware<in T>): Middleware<T> =
    Middleware.combine(this, other)

/** Combine the receiver with the [other] */
operator fun <T> Pipe<T>.plus(other: Pipe<T>): Pipe<T> =
    Pipe.combine(this, other)

/** Combine the receiver with the [other] */
operator fun <T> Interceptor<T>.plus(other: Interceptor<T>): Interceptor<T> =
    Interceptor.combine(this, other)
