/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.client.wrapper;

import org.cufy.http.client.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A call cursor with shortcut client field accessors.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientExtension<Self extends ClientExtension<Self>> extends ClientWrapper<Self> {
	//.emit

	/**
	 * Trigger the given {@code emission} in the client. Invoke all the callbacks with
	 * {@code null} as the parameter that was registered to be called when the given
	 * {@code emission} occurs.
	 *
	 * @param emission the emission to be triggered.
	 * @param <T>      the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code emission} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default <T> Self emit(@NotNull Emission<T> emission) {
		return this.emit(emission, null);
	}

	/**
	 * Trigger the given {@code emission} in the client. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * emission} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param emission  the emission to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code emission} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <T> Self emit(@NotNull Emission<T> emission, @Nullable T parameter) {
		return this.emit(emission, parameter, error ->
				this.client().emit(Emission.EXCEPTION, error, e -> {
					if (e != error)
						e.addSuppressed(error);
					//noinspection CallToPrintStackTrace
					e.printStackTrace();
				})
		);
	}

	/**
	 * Trigger the given {@code emission} in the client. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * emission} occurs. When an exception from a callback is thrown, the given {@code
	 * error} consumer will be invoked with that exception as the argument.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param error     the callback to be invoked when an error occurs.
	 * @param emission  the emission to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code emission} is null.
	 * @since 0.3.0 ~2021.12.09
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this")
	default <T> Self emit(@NotNull Emission<T> emission, @Nullable T parameter, @Nullable Consumer<Throwable> error) {
		this.client().emit(emission, parameter, error);
		return (Self) this;
	}

	//.on

	/**
	 * Add the given {@code callback} to be performed when the given {@code action} occurs
	 * on the wrapped client. The thread executing the given {@code callback} is not
	 * specific so the given {@code callback} must not assume it will be executed in a
	 * specific thread (e.g. the application thread).
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
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <T> Self on(@NotNull Action<T> action, @NotNull Callback<T> callback) {
		this.client().on(action, callback);
		return (Self) this;
	}

	// .perform

	/**
	 * Execute the given {@code performer} with this as the parameter.
	 * <br>
	 * Exceptions thrown by the given {@code performer} will be caught safely. But,
	 * exception by a thread created by the performer is left for the callback to handle.
	 *
	 * @param performer the performer to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code performer} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self perform(@NotNull Performer<? super Self> performer) {
		Objects.requireNonNull(performer, "performer");

		try {
			performer.perform((Self) this);
		} catch (Throwable e) {
			this.emit(Emission.EXCEPTION, e);
		}

		return (Self) this;
	}

	// .resume

	/**
	 * Add the given {@code callback} to be performed when the given {@code action} occurs
	 * on the wrapped client (with this cursor as the parameter). The thread executing the
	 * given {@code callback} is not specific so the given {@code callback} must not
	 * assume it will be executed in a specific thread (e.g. the application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param action   the action to listen to.
	 * @param callback the callback to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Self resume(@NotNull Action<?> action, @NotNull Callback<Self> callback) {
		this.client().on(action, parameter ->
				callback.call((Self) this)
		);
		return (Self) this;
	}

	// .use

	/**
	 * Inject the listeners of the given {@code middlewares} to the wrapped client (using
	 * {@link Middleware#inject(Client)}). Duplicate injection is the middleware
	 * responsibility to solve.
	 *
	 * @param middlewares the middlewares to be injected.
	 * @return this.
	 * @throws NullPointerException     if the given {@code middlewares} is null.
	 * @throws IllegalArgumentException if any of the given {@code middlewares} refused to
	 *                                  inject its callbacks to the client for some aspect
	 *                                  in it.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract("_->this")
	default Self use(@Nullable Middleware @NotNull ... middlewares) {
		this.client().use(middlewares);
		return (Self) this;
	}
}
