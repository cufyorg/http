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
package org.cufy.http.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A caller is a unit that stores callbacks and call them when specific actions occurs.
 * <br>
 * Actions:
 * <ul>
 *     <li>{@link #ALL}</li>
 *     <li>{@link #EXCEPTION}</li>
 * </ul>
 *
 * @param <C> the type of the caller. (the type of this)
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Caller<C extends Caller<C>> {
	/**
	 * The "*" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Object} nullable: the parameter.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>Any action got triggered. (except {@link #EXCEPTION})</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23l
	 */
	String ALL = "*";
	/**
	 * The "exception" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Throwable} not-null: the throwable that got caught.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>Any throwable got caught when invoking any callback of a caller.</li>
	 * </ul>
	 * <br>
	 * Note: if the callback itself thrown an exception then it will trigger itself, AGAIN!
	 * But, with the new thrown exception.
	 *
	 * @since 0.0.1 ~2021.03.23l
	 */
	String EXCEPTION = "exception";

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
	 * {@code null-parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param action the action to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("_->this")
	default C trigger(@NotNull String action) {
		return this.trigger(null, action);
	}

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
	 * given {@code array} that was registered to be called when the given {@code action}
	 * occurs.
	 *
	 * @param action the action to be triggered.
	 * @param array  the parameter to invoke the callbacks with.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	default C trigger(@NotNull String action, Object... array) {
		return this.trigger(array, action);
	}

	/**
	 * A caller equals another caller when they are the same instance.
	 *
	 * @param object the object to be checked.
	 * @return true, if the given {@code object} is this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(value = "null->false", pure = true)
	@Override
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of this caller. (implementation specific)
	 *
	 * @return the hash code of this caller.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(pure = true)
	@Override
	int hashCode();

	/**
	 * The string representation of a caller is a text that describes the type of this
	 * caller following by some text that identifies this caller from other callers. The
	 * string representation must be a single line string.
	 *
	 * @return a string representation of this caller.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@NonNls
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Inject the listeners of the given {@code middleware} to this caller (using {@link
	 * Middleware#inject(Caller)}). Duplicate injection is the middleware responsibility
	 * to solve. It is recommended for the middleware to inject the same callback instance
	 * to solve the problem.
	 *
	 * @param middleware the middleware to be injected.
	 * @return this.
	 * @throws NullPointerException     if the given {@code middleware} is null.
	 * @throws IllegalArgumentException if the given {@code middleware} refused to inject
	 *                                  it's callbacks to this caller for some aspect in
	 *                                  this caller.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("_->this")
	C middleware(@NotNull Middleware<C> middleware);

	/**
	 * Add the given {@code callback} to be performed when the given {@code action}
	 * occurs. The thread executing the given {@code callback} is not specific so the
	 * given {@code callback} must not assume it will be executed in a specific thread
	 * (e.g. the application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param action   the action to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> C on(@NotNull String action, @NotNull Callback<C, T> callback);

	/**
	 * Add the given {@code callback} to be performed when any of the given {@code
	 * actions} occurs. The thread executing the given {@code callback} is not specific so
	 * the given {@code callback} must not assume it will be executed in a specific thread
	 * (e.g. the application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 * <br>
	 * Null elements in the given {@code actions} array will be skipped.
	 *
	 * @param callback the callback to be set.
	 * @param actions  the actions to listen to.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code callback} or {@code actions} is
	 *                              null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> C on(@NotNull Callback<C, T> callback, @Nullable String @NotNull ... actions);

	/**
	 * Add the given {@code callback} to be performed in a new thread (when possible) when
	 * the given {@code action} occurs.
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param action   the action to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> C ont(@NotNull String action, @NotNull Callback<C, T> callback);

	/**
	 * Add the given {@code callback} to be performed in a new thread (when possible) when
	 * any of the given {@code actions} occurs.
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 * <br>
	 * Null elements in the given {@code actions} array will be skipped.
	 *
	 * @param callback the callback to be set.
	 * @param actions  the actions to listen to.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code callback} or {@code actions} is
	 *                              null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> C ont(@NotNull Callback<C, T> callback, @Nullable String @NotNull ... actions);

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param action    the action to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("_,_->this")
	C trigger(Object parameter, @NotNull String action);
}
