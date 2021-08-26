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

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
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
	static Action<Object> action(@NotNull @Language("RegExp") String regex, @Nullable String @NotNull ... names) {
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(names, "names");
		Pattern pattern = Pattern.compile(regex);
		return new Action<Object>() {
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
	static <T> Action<T> action(@NotNull Class<? super T> type, @NotNull @Language("RegExp") String regex, @Nullable String @NotNull ... names) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(names, "names");
		Pattern pattern = Pattern.compile(regex);
		return new Action<T>() {
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
