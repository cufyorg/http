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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

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
	protected Map<@NotNull Action<Object>, @NotNull Set<@NotNull Callback<C, Object>>> callbacks = new LinkedHashMap<>();

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
	public C middleware(@NotNull Middleware<? super C> middleware) {
		Objects.requireNonNull(middleware, "middleware");
		middleware.inject(this);
		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	@Override
	public <T> C on(@NotNull Action<T> action, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		//noinspection unchecked
		this.callbacks.computeIfAbsent((Action<Object>) action, a -> new LinkedHashSet<>())
					  .add((Callback<C, Object>) callback);
		//noinspection unchecked
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
	public <T> C trigger(@NotNull Action<T> action, @Nullable T parameter) {
		Objects.requireNonNull(action, "action");
		//noinspection OverlyLongLambda
		this.callbacks.forEach((a, callbacks) -> {
			for (String trigger : action.triggers())
				if (a.test(trigger, parameter)) {
					for (Callback<C, Object> callback : callbacks)
						try {
							//noinspection unchecked
							callback.call((C) this, parameter);
						} catch (Throwable throwable) {
							this.trigger(Caller.EXCEPTION, throwable);
						}

					return;
				}
		});

		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	@Override
	public C trigger(@NotNull @NonNls String trigger, @Nullable Object parameter) {
		Objects.requireNonNull(trigger, "trigger");
		//noinspection OverlyLongLambda
		this.callbacks.forEach((a, callbacks) -> {
			if (a.test(trigger, parameter))
				for (Callback<C, Object> callback : callbacks)
					try {
						//noinspection unchecked
						callback.call((C) this, parameter);
					} catch (Throwable throwable) {
						this.trigger(Caller.EXCEPTION, throwable);
					}
		});

		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	@Override
	public <T> C trigger(@Nullable T parameter, @Nullable Action<T> @NotNull ... actions) {
		Objects.requireNonNull(actions, "actions");
		//noinspection OverlyLongLambda
		this.callbacks.forEach((a, callbacks) -> {
			for (Action<T> action : actions)
				if (action != null)
					for (String trigger : action.triggers())
						if (a.test(trigger, parameter)) {
							for (Callback<C, Object> callback : callbacks)
								try {
									//noinspection unchecked
									callback.call((C) this, parameter);
								} catch (Throwable throwable) {
									this.trigger(Caller.EXCEPTION, throwable);
								}

							return;
						}
		});

		//noinspection unchecked
		return (C) this;
	}

	@NotNull
	@Override
	public C trigger(@Nullable Object parameter, @Nullable @NonNls String @NotNull ... triggers) {
		Objects.requireNonNull(triggers, "triggers");

		//noinspection OverlyLongLambda
		this.callbacks.forEach((a, callbacks) -> {
			for (String trigger : triggers)
				if (trigger != null)
					if (a.test(trigger, parameter)) {
						for (Callback<C, Object> c : callbacks)
							try {
								//noinspection unchecked
								c.call((C) this, parameter);
							} catch (Throwable throwable) {
								this.trigger(Caller.EXCEPTION, throwable);
							}

						return;
					}
		});

		//noinspection unchecked
		return (C) this;
	}
}
