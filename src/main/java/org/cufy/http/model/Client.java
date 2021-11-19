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
package org.cufy.http.model;

import org.cufy.http.ext.okhttp.OkHttpMiddleware;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A client is a stateful object containing the necessary data to perform a http request.
 * Also, the client provides the ability to set callbacks to be invoked when a specific
 * task/event occurs. (e.g. the response of a request received)
 * <br>
 * Note: the client has no callbacks as default. So, in order for it to work, you need to
 * add inject middlewares that perform the standard actions for it. (e.g. {@link
 * OkHttpMiddleware})
 * <br>
 * Also Note: a client is intended to be used as the central unit for a single request.
 * So, if a common configuration is needed. You might declare a global client and {@link
 * #clone()} it on the need of using it.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Client extends Cloneable {
	/**
	 * Replace the extras map of this client to be the result of invoking the given {@code
	 * operator} with the current extras map of this client. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the extras map of this client cannot be
	 *                                       changed and the returned extras map from the
	 *                                       given {@code operator} is different from the
	 *                                       current extras map.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client extras(@NotNull UnaryOperator<Map<@Nullable String, @Nullable Object>> operator) {
		Objects.requireNonNull(operator, "operator");
		Map<String, Object> e = this.getExtras();
		Map<String, Object> extras = operator.apply(e);

		if (extras != null && extras != e)
			this.setExtras(extras);

		return this;
	}

	/**
	 * Set the extras map to be the given map.
	 *
	 * @param extras the new extras map.
	 * @return this.
	 * @throws NullPointerException          if the given {@code extras} is null.
	 * @throws UnsupportedOperationException if the extras map of this client cannot be
	 *                                       changed.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		throw new UnsupportedOperationException("setExtras");
	}

	/**
	 * Clone this caller. The clone must have a shallow copy of the listeners of this (but
	 * not the mappings).
	 * <br>
	 * Note: the cloned client will not be {@link #equals(Object) equal} to this client.
	 *
	 * @return a clone of this client.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Client clone();

	/**
	 * Return the extras map.
	 *
	 * @return the extras map.
	 * @since 0.2.0
	 */
	@NotNull
	@Contract(pure = true)
	Map<@Nullable String, @Nullable Object> getExtras();

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
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> Client on(@NotNull Action<T> action, @NotNull Callback<T> callback);

	/**
	 * Trigger the given {@code action} in this client. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param action    the action to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract("_,_->this")
	<T> Client perform(@NotNull Action<T> action, @Nullable T parameter);

	/**
	 * Inject the listeners of the given {@code middleware} to this client (using {@link
	 * Middleware#inject(Client)}). Duplicate injection is the middleware responsibility
	 * to solve.
	 *
	 * @param middleware the middleware to be injected.
	 * @return this.
	 * @throws NullPointerException     if the given {@code middleware} is null.
	 * @throws IllegalArgumentException if the given {@code middleware} refused to inject
	 *                                  its callbacks to this client for some aspect in
	 *                                  this client.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("_->this")
	Client use(@NotNull Middleware middleware);
}
