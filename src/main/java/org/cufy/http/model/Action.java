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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;

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
}
