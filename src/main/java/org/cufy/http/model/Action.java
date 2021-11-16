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

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
	 * An action that gets triggered by anything exception {@link #EXCEPTION}.
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
	Action<Call> CONNECT = Action.action(Call.class, "connect", "connect");
	/**
	 * An action that gets triggered by the connection middleware when the connection is
	 * done.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Call> CONNECTED = Action.action(Call.class, "connected", "connected");
	/**
	 * An action that gets triggered when a connection fails.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Call> DISCONNECTED = Action.action(Call.class, "disconnected|not-sent|not-received|malformed|not-parsed", "disconnected");
	/**
	 * An action that gets triggered when an exception is thrown when invoking a
	 * callback.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Throwable> EXCEPTION = Action.action(Throwable.class, "exception", "exception");
	/**
	 * <b>Feature</b>
	 * <br>
	 * <b>CMW -> ImplicitH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the response has
	 * been built successfully and ready to be used.
	 * <br>
	 * <b>Triggers:</b> the implicit helpers to fill-out implicit data.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Call> RECEIVED = Action.action(Call.class, "received", "received");
	/**
	 * An action that gets triggered by the connection middleware for the user to fill the
	 * request data.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Action<Call> REQUEST = Action.action(Call.class, "request", "request");
	/**
	 * An action that gets triggered by the connection middleware for the user to fill
	 * additional response data.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	Action<Call> RESPONSE = Action.action(Call.class, "request", "request");
	/**
	 * <b>Feature</b>
	 * <br>
	 * <b>USER -> ImplicitH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the connection will
	 * be performed soon.
	 * <br>
	 * <b>Triggers:</b> the helper listeners to fill-out implicit data.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Call> SENDING = Action.action(Call.class, "sending", "sending");

	/**
	 * Return an action that accept any action name that matches the given {@code regex}.
	 *
	 * @param regex the regex matching the action names allowed for the returned action.
	 * @param names the names triggering the action.
	 * @return an action that accepts the action names that matches the given {@code
	 * 		regex} and  the parameter that are instances of the given {@code type}
	 * @throws NullPointerException   if the given {@code type} or {@code regex} or {@code
	 *                                names} is null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	static Action<Object> action(@NotNull @Language("RegExp") String regex, @Nullable String @NotNull ... names) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(names, "names");
		Pattern pattern = Pattern.compile(regex);
		return new Action<>() {
			@NotNull
			@Override
			public Iterator<String> iterator() {
				return Arrays.asList(names).iterator();
			}

			@Override
			public boolean test(@NotNull String name, @Nullable Object parameter) {
				Objects.requireNonNull(name, "trigger");
				return pattern.matcher(name).matches();
			}
		};
	}

	/**
	 * Return an action that accept any action name that matches the given {@code regex}
	 * and the parameters that are instances of the given {@code type}.
	 *
	 * @param regex the regex matching the action names allowed for the returned action.
	 * @param type  the type of parameters that the returned action accepts.
	 * @param names the names triggering the action.
	 * @param <T>   the type of the parameters allowed for the returned action.
	 * @return an action that accepts the action names that matches the given {@code
	 * 		regex} and  the parameter that are instances of the given {@code type}
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
		Pattern pattern = Pattern.compile(regex);
		return new Action<>() {
			@NotNull
			@Override
			public Iterator<String> iterator() {
				return Arrays.asList(names).iterator();
			}

			@Override
			public boolean test(@NotNull String name, @Nullable Object parameter) {
				Objects.requireNonNull(name, "trigger");
				return pattern.matcher(name).matches() && type.isInstance(parameter);
			}
		};
	}

	/**
	 * Return a new action that gets satisfied if any of the given {@code actions} gets
	 * satisfied and has all the names of the given {@code actions}.
	 *
	 * @param actions the actions to be combined.
	 * @param <T>     the type of the expected parameter.
	 * @return a new action from combining the given {@code actions}.
	 * @throws NullPointerException if the given {@code actions} is null.
	 * @since 0.2.8 ~2021.08.27
	 */
	@NotNull
	@SafeVarargs
	@Contract(value = "_->new", pure = true)
	static <T> Action<T> action(@Nullable Action<? extends T>... actions) {
		Objects.requireNonNull(actions, "actions");
		return new Action<>() {
			@NotNull
			@Override
			public Iterator<@NotNull String> iterator() {
				Set<String> set = new HashSet<>();

				for (Action<? extends T> action : actions)
					if (action != null)
						for (String name : action)
							set.add(name);

				return set.iterator();
			}

			@Override
			public boolean test(@NotNull String name, @Nullable Object parameter) {
				for (Action<? extends T> a : actions)
					if (a != null)
						if (a.test(name, parameter))
							return true;
				return false;
			}
		};
	}

	@NotNull
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
}
