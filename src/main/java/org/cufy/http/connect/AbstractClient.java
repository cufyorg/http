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
package org.cufy.http.connect;

import org.cufy.http.middleware.Middleware;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * A basic implementation of the interface {@link Client}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class AbstractClient implements Client {
	/**
	 * The callbacks registered to this caller. Actions mapped to a set of callbacks.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	protected Map<@NotNull Callback, @NotNull Set<@NotNull Action>> callbacks = new LinkedHashMap<>();

	/**
	 * The extras map.
	 *
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	protected Map<@Nullable String, @Nullable Object> extras = new HashMap<>();

	/**
	 * The current request of this client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	protected Request request;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient() {
		this.request = Request.request();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new copy of the given {@code client}.
	 *
	 * @param client the client to copy.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	public AbstractClient(Client client) {
		Objects.requireNonNull(client, "client");
		this.request = Request.request(client.getRequest());
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new client with its request begin the given {@code request}.
	 *
	 * @param request the request of this client.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = Request.request(request);
	}

	@NotNull
	@Override
	public AbstractClient clone() {
		try {
			AbstractClient clone = (AbstractClient) super.clone();
			clone.request = this.request.clone();
			clone.callbacks = new LinkedHashMap<>(this.callbacks);
			clone.callbacks.replaceAll((action, callbacks) -> new LinkedHashSet<>(callbacks));
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
	public Request getRequest() {
		return this.request;
	}

	@NotNull
	@Override
	public Client middleware(@NotNull Middleware middleware) {
		Objects.requireNonNull(middleware, "middleware");
		middleware.inject(this);
		return this;
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
							//noinspection unchecked
							c.call(this, parameter);
						} catch (Throwable throwable) {
							this.perform(Client.EXCEPTION, throwable);
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
	public Client setRequest(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
		return this;
	}

	@NotNull
	@Override
	public String toString() {
		return "Client " + System.identityHashCode(this);
	}
}
