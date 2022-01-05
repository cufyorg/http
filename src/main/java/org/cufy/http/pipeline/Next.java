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
package org.cufy.http.pipeline;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A next function is a function that handles the transition between two pipes after the
 * first pipe finishes from its work. Also, it can be used as the first instance to be
 * invoked instead of invoking a pipe.
 *
 * @param <T> the type of the parameter the pipe accepts.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
@FunctionalInterface
public interface Next<T> {
	/**
	 * Return a new next function that invokes the given next functions when it gets
	 * invoked and with the parameters given to it.
	 *
	 * @param nexts the next functions.
	 * @param <T>   the type of the parameter the pipe accepts.
	 * @return a new next function from combining the given next function.
	 * @throws NullPointerException if the given {@code nexts} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <T> Next<T> combine(@Nullable Next<T> @NotNull ... nexts) {
		Objects.requireNonNull(nexts, "next");
		return error -> {
			for (Next<T> next : nexts)
				if (next != null)
					next.invoke(error);
		};
	}

	/**
	 * Invoke the next pipe.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	default void invoke() {
		this.invoke(null);
	}

	/**
	 * Invoke the next pipe.
	 * <br>
	 * If an error is passed, the pipe chain execution will finish and the given {@code
	 * error} will be handled safely. Otherwise, the next pipe will be invoked.
	 *
	 * @param error the error to be handled, if any.
	 * @since 0.3.0 ~2021.12.23
	 */
	void invoke(@Nullable Throwable error);
}
