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
package org.cufy.http.impl;

import org.cufy.http.model.Action;
import org.cufy.http.model.Callback;
import org.cufy.http.model.Client;
import org.cufy.http.model.Middleware;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * A basic implementation of the interface {@link Client}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class ClientImpl implements Client {
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
	 * <b>Default</b>
	 * <br>
	 * Construct a new default client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public ClientImpl() {
		this.callbacks = new LinkedHashMap<>();
		this.extras = new LinkedHashMap<>();
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new client with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new client.
	 * @return the client constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Client client(@NotNull Consumer<Client> builder) {
		Objects.requireNonNull(builder, "builder");
		Client client = new ClientImpl();
		builder.accept(client);
		return client;
	}

	@NotNull
	@Override
	public ClientImpl clone() {
		try {
			ClientImpl clone = (ClientImpl) super.clone();
			clone.callbacks = new LinkedHashMap<>(this.callbacks);
			clone.callbacks.replaceAll((action, callbacks) -> new LinkedHashSet<>(callbacks));
			clone.extras = new LinkedHashMap<>(this.extras);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public Map<@Nullable String, @Nullable Object> getExtras() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.extras;
	}

	@NotNull
	@Override
	public <T> Client on(@NotNull Action<T> action, @NotNull Callback<T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		this.callbacks.computeIfAbsent(callback, k -> new LinkedHashSet<>()).add(action);
		return this;
	}

	@NotNull
	@Override
	public <T> Client perform(@NotNull Action<T> action, @Nullable T parameter) {
		Objects.requireNonNull(action, "action");
		//foreach callback in the callbacks
		this.callbacks.forEach((c, as) -> {
			//foreach name in the provided action
			for (String name : action)
				//foreach action associated with the callback
				for (Action a : as)
					//test the action
					if (a.test(name, parameter)) {
						try {
							c.call(this, parameter);
						} catch (Throwable throwable) {
							this.perform(Action.EXCEPTION, throwable);
						}

						//go to the next callback after execution when a matching action is found
						return;
					}
		});

		return this;
	}

	@NotNull
	@Override
	public Client setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		Objects.requireNonNull(extras, "extras");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.extras = extras;
		return this;
	}

	@NotNull
	@Override
	public String toString() {
		return "Client " + System.identityHashCode(this);
	}

	@NotNull
	@Override
	public Client use(@NotNull Middleware middleware) {
		Objects.requireNonNull(middleware, "middleware");
		middleware.inject(this);
		return this;
	}
}
