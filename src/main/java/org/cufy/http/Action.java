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
package org.cufy.http;

import org.cufy.http.cursor.Cursor;
import org.cufy.http.cursor.RequestCursor;
import org.cufy.http.cursor.ResponseCursor;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * An interface describing an action.
 *
 * @param <T> the type of parameter this action accepts.
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.28
 */
@FunctionalInterface
public interface Action<T> extends Iterable<String> {
	/**
	 * An action that gets triggered by anything except {@link #EXCEPTION}.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Object> ALL = (trigger, parameter) -> !"exception".equals(trigger);
	/**
	 * An action that triggers the connection middleware to perform the connection
	 * sequence on the call given to it.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Cursor> CONNECT = Action.action(Cursor.class, "connect", "connect");
	/**
	 * An action that gets triggered by the connection middleware when the connection is
	 * done.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<ResponseCursor> CONNECTED = Action.action(ResponseCursor.class, "connected", "connected");
	/**
	 * An action that gets triggered by the connection middleware when the connection
	 * fails.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Cursor> DISCONNECTED = Action.action(Cursor.class, "disconnected", "disconnected");
	/**
	 * An action that gets triggered when an exception is thrown when invoking a
	 * callback.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Throwable> EXCEPTION = Action.action(Throwable.class, "exception", "exception");
	/**
	 * An action that gets triggered by the connection middleware before performing the
	 * connection.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Action<RequestCursor> REQUEST = Action.action(RequestCursor.class, "request", "request");
	/**
	 * An action that gets triggered by the connection middleware after the connection is
	 * done and before {@link #CONNECTED}.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Action<ResponseCursor> RESPONSE = Action.action(ResponseCursor.class, "request", "request");

	/**
	 * Return a new action that gets satisfied if any of the given {@code actions} gets
	 * satisfied and has all the names of the given {@code actions}.
	 *
	 * @param actions the actions to be combined.
	 * @param <T>     the type of the param of the returned action.
	 * @return a new action from combining the given {@code actions}.
	 * @throws NullPointerException if the given {@code actions} is null.
	 * @since 0.2.8 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <T> Action<T> action(@Nullable Action<? extends T>... actions) {
		Objects.requireNonNull(actions, "actions");
		return new Impl<>(
				Arrays.stream(actions)
					  .filter(Objects::nonNull)
					  .flatMap(a -> StreamSupport.stream(a.spliterator(), false))
					  .collect(Collectors.toSet()),
				(name, parameter) ->
						Arrays.stream(actions)
							  .filter(Objects::nonNull)
							  .anyMatch(a -> a.test(name, parameter))
		);
	}

	/**
	 * Construct a new action that accept any action name that matches the given {@code
	 * regex}.
	 *
	 * @param regex the regex matching the action names allowed for the returned action.
	 * @param names the names triggering the action.
	 * @param <T>   the type of the returned action.
	 * @return a new action for the given parameters.
	 * @throws NullPointerException   if the given {@code regex} or {@code names} is
	 *                                null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static <T> Action<T> action(@NotNull @Language("RegExp") String regex, @Nullable String @NotNull ... names) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(names, "names");
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		return new Impl<>(
				Arrays.stream(names)
					  .filter(Objects::nonNull)
					  .collect(Collectors.toSet()),
				(name, parameter) ->
						pattern.matcher(name).matches()
		);
	}

	/**
	 * Return an action that accept any action name that matches the given {@code regex}
	 * and the parameters that are instances of the given {@code type}.
	 *
	 * @param regex the regex matching the action names allowed for the returned action.
	 * @param type  the type of parameters that the returned action accepts.
	 * @param names the names triggering the action.
	 * @param <T>   the type of the returned action.
	 * @return a new action for the given parameters.
	 * @throws NullPointerException   if the given {@code type} or {@code regex} or {@code
	 *                                names} is null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	static <T> Action<T> action(@NotNull Class<? super T> type, @NotNull @Language("RegExp") String regex, @Nullable String @NotNull ... names) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(names, "names");
		java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
		return new Impl<>(
				Arrays.stream(names)
					  .filter(Objects::nonNull)
					  .collect(Collectors.toSet()),
				(name, parameter) ->
						type.isInstance(parameter) &&
						pattern.matcher(name).matches()
		);
	}

	/**
	 * Return the names that this action triggers when invoked. This function must return
	 * an iterator iterating over the same items everytime.
	 *
	 * @return an iterator iterating over the names this action triggers.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	@Override
	default Iterator<@NotNull String> iterator() {
		return Collections.emptyIterator();
	}

	/**
	 * Test if the given {@code name} is accepted by this action.
	 *
	 * @param name      the action name to be tested.
	 * @param parameter the parameter to be passed to the listeners of this action.
	 * @return true, if the given {@code name} is a valid name for this action.
	 * @throws NullPointerException if the given {@code name} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@Contract(pure = true)
	boolean test(@NotNull String name, @Nullable Object parameter);

	/**
	 * A basic implementation of the interface {@link Action}.
	 *
	 * @param <T> the type of parameter this action accepts.
	 * @author LSafer
	 * @version 0.3.0
	 * @since 0.3.0 ~2021.11.18
	 */
	class Impl<T> implements Action<T> {
		/**
		 * The names this action triggers.
		 *
		 * @since 0.3.0 ~2021.11.18
		 */
		@NotNull
		protected final Set<@NotNull String> names;
		/**
		 * The predicate this action is using.
		 *
		 * @since 0.3.0 ~2021.11.18
		 */
		@NotNull
		protected final BiPredicate<@NotNull String, @Nullable Object> predicate;

		/**
		 * Construct a new action from the given component.
		 *
		 * @param names     the names the constructed action will trigger.
		 * @param predicate the predicate the constructed action will use.
		 * @throws NullPointerException if the given {@code names} or {@code predicate} is
		 *                              null.
		 * @since 0.3.0 ~2021.11.18
		 */
		public Impl(@NotNull Set<@NotNull String> names, @NotNull BiPredicate<@NotNull String, @NotNull Object> predicate) {
			Objects.requireNonNull(names, "names");
			Objects.requireNonNull(predicate, "predicate");
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			this.names = names;
			this.predicate = predicate;
		}

		@NotNull
		@Override
		public Iterator<@NotNull String> iterator() {
			return this.names.iterator();
		}

		@Override
		public boolean test(@NotNull String name, @Nullable Object parameter) {
			return this.predicate.test(name, parameter);
		}
	}
}
