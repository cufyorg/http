/*
 *	Copyright 2021 Cufy
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
package org.cufy.http.ext

import org.cufy.http.ext.form.ParametersBody
import org.cufy.http.ext.text.TextBody
import org.cufy.http.model.*

/**
 * Return a new action that have the names of the receiver action and the given [action]
 * and accepts both what the receiver action and the given [action] accepts.
 *
 * @receiver the receiver action to be combined.
 * @param action the action to combine with the receiver action into a new action.
 * @return a new action from combining the receiver action and the given [action].
 * @throws NullPointerException if the receiver or [action] is null.
 * @author LSafer
 * @since 0.2.1 ~2021.08.26
 */
infix fun <T> Action<in T>.or(action: Action<in T>): Action<T> =
    object : Action<T> {
        override fun test(name: String, parameter: Any?): Boolean {
            return this@or.test(name, parameter) ||
                   action.test(name, parameter)
        }

        override fun iterator(): MutableIterator<String> {
            val set = HashSet(this@or.toList())
            set.addAll(action.toList())
            return set.iterator()
        }
    }

/**
 * Return a new callback that calls the receiver callback then the given [callback]
 * respectfully when called.
 *
 * @receiver the receiver callback to be combined.
 * @param callback the callback to be called after the receiver callback when the returned
 *                 callback get called.
 * @return a new callback from combining the receiver callback and the given [callback].
 * @throws NullPointerException if the receiver or [callback] is null.
 * @author LSafer
 * @since 0.2.1 ~2021.08.26
 */
infix fun <T> Callback<in T>.then(callback: Callback<in T>): Callback<T> =
    Callback { t, u ->
        this@then.call(t, u)
        callback.call(t, u)
    }

operator fun <T> Callback<in T>.invoke(client: Client, parameter: T) =
    call(client, parameter)

/**
 * Allows to use the index operator for storing values in a parameters body.
 */
operator fun ParametersBody.set(name: String, value: String): ParametersBody =
    put(name, value)

/**
 * Allows to use the index operator for storing values in headers.
 */
operator fun Headers.set(name: String, value: String): Headers =
    put(name, value)

/**
 * Allows to use the index operator for storing values in a query.
 */
operator fun Query.set(name: String, value: String): Query =
    put(name, value)

/**
 * Allows to use the index operator for storing values in an userinfo.
 */
operator fun UserInfo.set(index: Int, value: String): UserInfo =
    put(index, value)

/**
 * Allows to use the appending operator for storing values in an userinfo.
 */
operator fun TextBody.plusAssign(content: Any?) {
    append(content)
}

/**
 * A shortcut for [Client.use].
 */
operator fun Client.plusAssign(middleware: Middleware) {
    use(middleware)
}

/**
 * Register the given `callback` to be called when the given `action` gets
 * triggered with a non-null parameter.
 *
 * @param action   the action to listen to.
 * @param callback the callback to be set.
 * @param <T>      the type of the expected parameter.
 * @return this.
 * @throws NullPointerException if the given `action` or `callback` is
 * null.
 * @since 0.3.0 ~2021.11.16
</T> */
fun <T> Client.on(action: Action<T>, callback: (T) -> Unit): Client =
    this.on(action, Callback { _, parameter ->
        if (parameter != null) callback(parameter)
    })

fun <T> Client.on(action: Action<T>, callback: (Client, T) -> Unit): Client =
    this.on(action, Callback { client, parameter ->
        if (parameter != null) callback(client, parameter)
    })
