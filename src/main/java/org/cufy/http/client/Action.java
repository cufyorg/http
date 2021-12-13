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

import java.util.Arrays;
import java.util.Objects;

/**
 * An interface describing an action that can be observed.
 *
 * @param <T> the type of parameter this action accepts.
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.28
 */
@FunctionalInterface
public interface Action<T> {
	/**
	 * An action that gets triggered by anything except {@code exception}.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Object> ALL = (name, parameter) ->
			!"exception".equals(name);
	/**
	 * An action that gets triggered when an exception is thrown when invoking a
	 * callback.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Throwable> EXCEPTION = (name, parameter) ->
			"exception".equals(name) && parameter instanceof Throwable;

	/**
	 * Construct a new action from combining the given {@code actions}.
	 *
	 * @param actions the actions to be combined.
	 * @param <E>     the common type between the given {@code actions}.
	 * @return a new action from combining the given {@code actions}.
	 * @throws NullPointerException if the given {@code actions} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <E> Action<E> combine(@Nullable Action<? extends E> @NotNull ... actions) {
		Objects.requireNonNull(actions, "actions");
		return (name, parameter) ->
				Arrays.stream(actions)
					  .allMatch(action ->
							  action.test(name, parameter)
					  );
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
