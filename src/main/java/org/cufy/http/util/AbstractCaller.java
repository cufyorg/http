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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

/**
 * A basic implementation of the interface {@link Caller}.
 *
 * @param <C> the type of the caller. (the type of this)
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class AbstractCaller<C extends Caller<C>> implements Caller<C> {
	/**
	 * The callbacks registered to this caller. Actions mapped to a set of callbacks.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	protected HashMap<@NotNull String, @NotNull HashSet<@NotNull Callback<C, ?>>> callbacks = new HashMap<>();

	@Override
	public boolean equals(@Nullable Object object) {
		return this == object;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@NotNull
	@Override
	public C middleware(@NotNull Middleware middleware) {
		Objects.requireNonNull(middleware, "middleware");
		middleware.inject(this);
		return (C) this;
	}

	@NotNull
	@Override
	public <T> C on(@NotNull String action, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		this.callbacks.computeIfAbsent(action, a -> new HashSet<>())
				.add(callback);
		return (C) this;
	}

	@NotNull
	@Override
	public <T> C on(@NotNull Callback<C, T> callback, @Nullable String @NotNull ... actions) {
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(actions, "actions");
		for (String action : actions)
			if (action != null)
				this.on(action, callback);

		return (C) this;
	}

	@NotNull
	@Override
	public <T> C ont(@NotNull String action, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		this.on(action, (caller, parameter) ->
				new Thread(() -> {
					try {
						((Callback<C, Object>) callback)
								.call(caller, parameter);
					} catch (Throwable throwable) {
						this.trigger(throwable, Caller.EXCEPTION);
					}
				}).start()
		);
		return (C) this;
	}

	@NotNull
	@Override
	public <T> C ont(@NotNull Callback<C, T> callback, @Nullable String @NotNull ... actions) {
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(actions, "actions");
		for (String action : actions)
			if (action != null)
				this.ont(action, callback);
		return (C) this;
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return "Caller " + System.identityHashCode(this);
	}

	@NotNull
	@Override
	public C trigger(Object parameter, @NotNull String action) {
		Objects.requireNonNull(action, "action");
		Optional.ofNullable(this.callbacks.get(action))
				.ifPresent(callbacks ->
						callbacks.forEach(callback -> {
							try {
								((Callback<C, Object>) callback)
										.call((C) this, parameter);
							} catch (Throwable throwable) {
								this.trigger(throwable, Caller.EXCEPTION);
							}
						})
				);
		if (!action.equals(Caller.EXCEPTION))
			Optional.ofNullable(this.callbacks.get(Caller.ALL))
					.ifPresent(callbacks ->
							callbacks.forEach(callback -> {
								try {
									((Callback<C, Object>) callback)
											.call((C) this, parameter);
								} catch (Throwable throwable) {
									this.trigger(throwable, Caller.EXCEPTION);
								}
							})
					);

		return (C) this;
	}
}
