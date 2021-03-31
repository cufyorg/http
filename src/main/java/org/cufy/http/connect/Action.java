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
import org.jetbrains.annotations.*;

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
public interface Action<T> {
	/**
	 * Return an action that accept any action name that matches the given {@code regex}
	 * and the parameters that are instances of the given {@code type}.
	 *
	 * @param regex    the regex matching the action triggers allowed for the returned
	 *                 action.
	 * @param type     the type of parameters that the returned action accepts.
	 * @param triggers the triggers triggering the action.
	 * @param <T>      the type of the parameters allowed for the returned action.
	 * @return an action that accepts the action triggers that matches the given {@code
	 * 		regex} and  the parameter that are instances of the given {@code type}
	 * @throws NullPointerException   if the given {@code type} or {@code regex} or {@code
	 *                                triggers} is null.
	 * @throws PatternSyntaxException if the given {@code regex} has a syntax error.
	 * @since 0.0.6 ~2021.03.28
	 */
	static <T> Action<T> of(@NotNull Class<? super T> type, @NotNull @NonNls @Language("RegExp") String regex, @Nullable @NonNls String @NotNull ... triggers) {
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(regex, "regex");
		Objects.requireNonNull(triggers, "triggers");
		Pattern pattern = Pattern.compile(regex);
		Set<String> namesSet = new HashSet<>(Arrays.asList(triggers));
		namesSet.remove(null);
		return new Action<T>() {
			@Override
			public boolean test(@NotNull @NonNls String trigger, @Nullable Object parameter) {
				Objects.requireNonNull(trigger, "trigger");
				return pattern.matcher(trigger).matches() && type.isInstance(parameter);
			}

			@NotNull
			@UnmodifiableView
			@Override
			public Set<@NotNull @NonNls String> triggers() {
				return namesSet;
			}
		};
	}

	/**
	 * Return an array of the names triggering action.
	 * <br>
	 * This method might not return any name if the action is not intended to be triggered
	 * from its instance.
	 *
	 * @return the names that triggers this action.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@UnmodifiableView
	@Contract(pure = true)
	default Set<@NotNull @NonNls String> triggers() {
		return Collections.emptySet();
	}

	/**
	 * Test if the given {@code trigger} is accepted by this action.
	 *
	 * @param trigger   the action trigger to be tested.
	 * @param parameter the parameter to be passed to the listeners of this action.
	 * @return true, if the given {@code trigger} is a valid trigger for this action.
	 * @throws NullPointerException if the given {@code trigger} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@Contract(pure = true)
	boolean test(@NotNull @NonNls String trigger, @Nullable Object parameter);
}
