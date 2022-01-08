/*
 *	Copyright 2021-2022 Cufy and ProgSpaceSA
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

import org.cufy.http.pipeline.Next;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * A client engine is a core instance a client relies on when attempting to perform a
 * connection.
 *
 * @param <I> the type of the input parameter (the request).
 * @param <O> the type of the output parameter (the response).
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface ClientEngine<I, O> {
	/**
	 * Construct a new engine that when it gets invoked it invokes the given {@code
	 * engines}.
	 * <br>
	 * The engines will be invoked in this form: the first engine will be invoked with a
	 * next function that invokes the next engine with itself when invoked.
	 *
	 * @param engines the engines to be combined.
	 * @param <I>     the type of the input parameter (the request).
	 * @param <O>     the type of the output parameter (the response).
	 * @return an engine invoking the given engines.
	 * @throws NullPointerException if the given {@code engines} is null.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <I, O> ClientEngine<I, O> combine(@Nullable ClientEngine<I, O> @NotNull ... engines) {
		Objects.requireNonNull(engines, "engines");
		return (input, next) -> {
			Iterator<ClientEngine<I, O>> iterator = Arrays
					.stream(engines)
					.filter(Objects::nonNull)
					.iterator();

			new Next<O>() {
				@Override
				public void invoke(@Nullable Throwable error) {
					if (error != null) {
						next.invoke(error);
						return;
					}
					if (!iterator.hasNext()) {
						next.invoke();
						return;
					}

					ClientEngine<I, O> engine = iterator.next();

					engine.connect(input, this);
				}
			}.invoke();
		};
	}

	/**
	 * Perform the connection with the given {@code input} and invoke the given {@code
	 * next} function when done.
	 *
	 * @param input the input instance.
	 * @param next  the next function.
	 * @throws NullPointerException          if the given {@code input} or {@code next} is
	 *                                       null.
	 * @throws UnsupportedOperationException if this engine does not support this
	 *                                       function.
	 * @since 0.3.0 ~2021.12.23
	 */
	void connect(@NotNull I input, @NotNull Next<O> next);
}
