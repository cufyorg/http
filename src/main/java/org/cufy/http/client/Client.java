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
package org.cufy.http.client;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * A client is a stateful object containing the necessary data to perform a http request.
 * Also, the client provides the ability to set callbacks to be invoked when a specific
 * task/event occurs. (e.g. the response of a request received)
 * <br>
 * Note: the client has no callbacks as default. So, in order for it to work, you need to
 * add inject middlewares that perform the standard actions for it.
 * <br>
 * Also Note: a client is intended to be used as the central unit for a single request.
 * So, if a common configuration is needed. You might declare a global client and {@link
 * #clone()} it on the need of using it.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class Client implements Cloneable {
	/**
	 * The callbacks registered to this caller. Actions mapped to a set of callbacks.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	protected Map<@NotNull Callback, @NotNull Set<@NotNull Action>> callbacks;
	/**
	 * The extras map.
	 *
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	protected Map<@Nullable String, @Nullable Object> extras;

	/**
	 * Construct a new default client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public Client() {
		this.callbacks = new LinkedHashMap<>();
		this.extras = new LinkedHashMap<>();
	}

	/**
	 * Construct a new client with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new client.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Client(@NotNull Consumer<@NotNull Client> builder) {
		Objects.requireNonNull(builder, "builder");
		this.callbacks = new LinkedHashMap<>();
		this.extras = new LinkedHashMap<>();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
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
	public Client clone() {
		try {
			Client clone = (Client) super.clone();
			clone.callbacks = new LinkedHashMap<>(this.callbacks);
			clone.callbacks.replaceAll((action, callbacks) -> new LinkedHashSet<>(callbacks));
			clone.extras = new LinkedHashMap<>(this.extras);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Return a string representation of this client.
	 *
	 * @return a string representation of this client.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return "Client " + System.identityHashCode(this);
	}

	/**
	 * Trigger the given {@code emission} in this client. Invoke all the callbacks with
	 * the given {@code parameter} that was registered to be called when the given {@code
	 * emission} occurs. When an exception from a callback is thrown, the given {@code
	 * error} consumer will be invoked with that exception as the argument.
	 *
	 * @param emission  the emission instance containing the names to be triggered.
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param error     the callback to be invoked when an error occurs.
	 * @param <T>       the type of the parameter.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@Contract(mutates = "this")
	public <T> void emit(@NotNull Emission<T> emission, @Nullable T parameter, @Nullable Consumer<Throwable> error) {
		Objects.requireNonNull(emission, "action");
		//foreach callback in the callbacks
		this.callbacks.forEach((callback, actions) -> {
			//foreach name in the provided emission
			for (String name : emission)
				//foreach action associated with the callback
				for (Action action : actions)
					//test the action
					if (action.test(name, parameter)) {
						try {
							callback.call(parameter);
						} catch (Throwable throwable) {
							if (error != null)
								error.accept(throwable);
						}

						//go to the next callback after execution when a matching action is found
						return;
					}
		});
	}

	/**
	 * Return the extras map.
	 *
	 * @return the extras map.
	 * @since 0.2.0
	 */
	@NotNull
	@Contract(pure = true)
	public Map<@Nullable String, @Nullable Object> getExtras() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.extras;
	}

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
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@Contract(mutates = "this")
	public <T> void on(@NotNull Action<T> action, @NotNull Callback<? super T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		this.callbacks.computeIfAbsent(callback, k -> new LinkedHashSet<>()).add(action);
	}

	/**
	 * Set the extras map to be the given map.
	 *
	 * @param extras the new extras map.
	 * @throws NullPointerException          if the given {@code extras} is null.
	 * @throws UnsupportedOperationException if the extras map of this client cannot be
	 *                                       changed.
	 * @since 0.2.0 ~2021.08.26
	 */
	@Contract(mutates = "this")
	public void setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		Objects.requireNonNull(extras, "extras");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.extras = extras;
	}

	/**
	 * Inject the listeners of the given {@code middlewares} to this client (using {@link
	 * Middleware#inject(Client)}). Duplicate injection is the middleware responsibility
	 * to solve.
	 *
	 * @param middlewares the middlewares to be injected.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to this client for some
	 *                                  aspect in this client.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Contract(mutates = "this")
	public void use(@Nullable Middleware @NotNull ... middlewares) {
		Objects.requireNonNull(middlewares, "middlewares");
		for (Middleware middleware : middlewares)
			if (middleware != null)
				middleware.inject(this);
	}
}
