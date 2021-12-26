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
package org.cufy.http.pipeline;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * A pipe is a function that acts like a piece in a long chain. The continuation of the
 * execution of a pipe chain is based on {@link Next} functions.
 *
 * @param <T> the type of the parameter of the pipe.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface Pipe<T> {
	/**
	 * Construct a new pipe that when it gets invoked it invokes the given {@code pipes}.
	 * <br>
	 * The pipes will be invoked in this form: the first pipe will be invoked with a next
	 * function that invokes the next pipe with itself when invoked.
	 *
	 * @param pipes the pipes to be combined.
	 * @param <T>   the type of the parameter of the pipes.
	 * @return a pipe invoking the given pipes.
	 * @throws NullPointerException if the given {@code pipes} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <T> Pipe<T> combine(@Nullable Pipe<T> @NotNull ... pipes) {
		Objects.requireNonNull(pipes, "pipes");
		return (parameter, next) -> {
			Iterator<Pipe<T>> iterator = Arrays
					.stream(pipes)
					.filter(Objects::nonNull)
					.iterator();

			new Next<T>() {
				{
					// auto start the sequence
					this.invoke();
				}

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

					Pipe<T> pipe = iterator.next();

					try {
						pipe.invoke(parameter, this);
					} catch (Throwable e) {
						next.invoke(e);
					}
				}
			};
		};
	}

	/**
	 * Invoke this pipe. If this pipe wishes to continue the chain, it will invoke the
	 * given {@link Next} function.
	 * <br>
	 * If the execution fails safely, the given {@link Next} function will be invoked with
	 * the failure exception.
	 * <br>
	 * <br>
	 * If the execution fails unsafely, the exception will fall through to the caller.
	 *
	 * @param parameter the parameter to invoke this pipe with.
	 * @param next      the next function to be invoked if appropriate.
	 * @throws NullPointerException if the given {@code parameter} or {@code next} is
	 *                              null.
	 * @since 0.3.0 ~2021.12.23
	 */
	void invoke(@NotNull T parameter, @NotNull Next<T> next) throws Throwable;
}
