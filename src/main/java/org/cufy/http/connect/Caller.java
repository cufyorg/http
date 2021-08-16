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
import org.cufy.http.sink.Sink;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
	 * <b>Dynamic</b>
	 * <br>
	 * <b>Any -> NosyUser</b>
	 * <br>
	 * <b>Triggered by:</b> anything except "exception".
	 * <br>
	 * <b>Triggers:</b> nothing.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Object> ALL = (trigger, parameter) -> !"exception".equals(trigger);
	/**
	 * <b>Mandatory</b>
	 * <br>
	 * <b>Caller -> UEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the caller internally to notify that a callback has thrown an
	 * uncaught exception. (and got caught by the caller itself)
	 * <br>
	 * <b>Triggers:</b> the uncaught exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Throwable> EXCEPTION = Action.of(Throwable.class, "exception", "exception");

	/**
	 * Add the given {@code callback} to be performed when the given {@code regex} occurs.
	 * The thread executing the given {@code callback} is not specific so the given {@code
	 * callback} must not assume it will be executed in a specific thread (e.g. the
	 * application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param regex    the regex to listen to.
	 * @param callback the callback to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code regex} or {@code callback} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default C on(@NotNull @NonNls @Language("RegExp") String regex, @NotNull Callback<C, Object> callback) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(callback, "callback");
		return this.on(Action.of(Object.class, regex), callback);
	}

	/**
	 * Add the given {@code callback} to be performed when the given {@code regex} occurs.
	 * The thread executing the given {@code callback} is not specific so the given {@code
	 * callback} must not assume it will be executed in a specific thread (e.g. the
	 * application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param type     the type of the accepted parameters.
	 * @param regex    the regex to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code type} or {@code regex} or {@code
	 *                              callback} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this")
	default <T> C on(@NotNull Class<T> type, @NotNull @NonNls @Language("RegExp") String regex, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(callback, "callback");
		return this.on(Action.of(type, regex), callback);
	}

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
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <T> C on(@NotNull Callback<C, T> callback, @Nullable Action<T> @NotNull ... actions) {
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(actions, "actions");
		for (Action<T> action : actions)
			if (action != null)
				this.on(action, callback);

		//noinspection unchecked
		return (C) this;
	}

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
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <T> C ont(@NotNull Action<T> action, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(action, "action");
		Objects.requireNonNull(callback, "callback");
		return this.on(action, (caller, parameter) ->
				new Thread(() -> {
					try {
						callback.call(caller, parameter);
					} catch (Throwable throwable) {
						this.trigger(Caller.EXCEPTION, throwable);
					}
				}).start()
		);
	}

	/**
	 * Add the given {@code callback} to be performed in a new thread (when possible) when
	 * the given {@code regex} occurs.
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param regex    the regex to listen to.
	 * @param callback the callback to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code regex} or {@code callback} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default C ont(@NotNull @NonNls @Language("RegExp") String regex, @NotNull Callback<C, Object> callback) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(callback, "callback");
		return this.on(Object.class, regex, (caller, parameter) ->
				new Thread(() -> {
					try {
						callback.call(caller, parameter);
					} catch (Throwable throwable) {
						this.trigger(Caller.EXCEPTION, throwable);
					}
				}).start()
		);
	}

	/**
	 * Add the given {@code callback} to be performed in a new thread (when possible) when
	 * the given {@code regex} occurs.
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param type     the type of the accepted parameters.
	 * @param regex    the regex to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code type} or {@code regex} or {@code
	 *                              callback} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this")
	default <T> C ont(@NotNull Class<T> type, @NotNull @NonNls @Language("RegExp") String regex, @NotNull Callback<C, T> callback) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(callback, "callback");
		return this.on(type, regex, (caller, parameter) ->
				new Thread(() -> {
					try {
						callback.call(caller, parameter);
					} catch (Throwable throwable) {
						this.trigger(Caller.EXCEPTION, throwable);
					}
				}).start()
		);
	}

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
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <T> C ont(@NotNull Callback<C, T> callback, @Nullable Action<T> @NotNull ... actions) {
		Objects.requireNonNull(callback, "callback");
		Objects.requireNonNull(actions, "actions");
		for (Action<T> action : actions)
			if (action != null)
				this.ont(action, callback);

		//noinspection unchecked
		return (C) this;
	}

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param sink      the sink to push the trigger command to.
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param action    the action to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code sink} or {@code action} is null.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract("_,_,_->this")
	default <T> C trigger(@NotNull Sink sink, @NotNull Action<T> action, @Nullable T parameter) {
		Objects.requireNonNull(sink, "sink");
		Objects.requireNonNull(action, "action");
		sink.push(() -> this.trigger(action, parameter));
		//noinspection unchecked
		return (C) this;
	}

	/**
	 * Trigger the given {@code trigger} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * trigger} occurs.
	 *
	 * @param sink      the sink to push the trigger command to.
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param trigger   the trigger to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code sink} or {@code trigger} is null.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract("_,_,_->this")
	default C trigger(@NotNull Sink sink, @NotNull @NonNls String trigger, @Nullable Object parameter) {
		Objects.requireNonNull(sink, "sink");
		Objects.requireNonNull(trigger, "trigger");
		sink.push(() -> this.trigger(trigger, parameter));
		//noinspection unchecked
		return (C) this;
	}

	/**
	 * Trigger the given {@code actions} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param sink      the sink to push the trigger command to.
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param actions   the actions to be triggered.
	 * @param <T>       the type of the parameter passed.
	 * @return this.
	 * @throws NullPointerException if the given {@code sink} or {@code actions} is null.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract("_,_,_->this")
	default <T> C trigger(@NotNull Sink sink, @Nullable T parameter, @Nullable Action<T> @NotNull ... actions) {
		Objects.requireNonNull(sink, "sink");
		Objects.requireNonNull(actions, "actions");
		sink.push(() -> this.trigger(parameter, actions));
		//noinspection unchecked
		return (C) this;
	}

	/**
	 * Trigger the given {@code triggers} in this caller. Invoke all the callbacks with
	 * the given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param sink      the sink to push the trigger command to.
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param triggers  the triggers to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code sink} or {@code triggers} is
	 *                              null.
	 * @since 0.0.7 ~2021.04.09
	 */
	@NotNull
	@Contract("_,_,_->this")
	default C trigger(@NotNull Sink sink, @Nullable Object parameter, @Nullable @NonNls String @NotNull ... triggers) {
		Objects.requireNonNull(sink, "sink");
		Objects.requireNonNull(triggers, "triggers");
		sink.push(() -> this.trigger(parameter, triggers));
		//noinspection unchecked
		return (C) this;
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
	C middleware(@NotNull Middleware<? super C> middleware);

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
	<T> C on(@NotNull Action<T> action, @NotNull Callback<C, T> callback);

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
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
	<T> C trigger(@NotNull Action<T> action, @Nullable T parameter);

	/**
	 * Trigger the given {@code trigger} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * trigger} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param trigger   the trigger to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code trigger} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract("_,_->this")
	C trigger(@NotNull @NonNls String trigger, @Nullable Object parameter);

	/**
	 * Trigger the given {@code actions} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param actions   the actions to be triggered.
	 * @param <T>       the type of the parameter passed.
	 * @return this.
	 * @throws NullPointerException if the given {@code actions} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract("_,_->this")
	<T> C trigger(@Nullable T parameter, @Nullable Action<T> @NotNull ... actions);

	/**
	 * Trigger the given {@code triggers} in this caller. Invoke all the callbacks with
	 * the given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param triggers  the triggers to be triggered.
	 * @return this.
	 * @throws NullPointerException if the given {@code triggers} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract("_,_->this")
	C trigger(@Nullable Object parameter, @Nullable @NonNls String @NotNull ... triggers);
}
