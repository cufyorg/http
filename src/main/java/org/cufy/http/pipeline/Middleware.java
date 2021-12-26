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

import java.util.Objects;

/**
 * A middleware is a function that injects configurations in a parameter.
 *
 * @param <T> the type of the parameter.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2022.12.26
 */
@FunctionalInterface
public interface Middleware<T> {
	/**
	 * Return a new middleware that injects the given middlewares when it gets injected
	 * and with the parameters given to it.
	 *
	 * @param middlewares the middlewares.
	 * @param <T>         the type of the parameter the middleware accepts.
	 * @return a new middleware from combining the given middlewares.
	 * @throws NullPointerException if the given {@code middlewares} is null.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	@SafeVarargs
	static <T> Middleware<T> combine(@Nullable Middleware<? super T> @NotNull ... middlewares) {
		Objects.requireNonNull(middlewares, "middlewares");
		return parameter -> {
			for (Middleware<? super T> middleware : middlewares)
				if (middleware != null)
					middleware.inject(parameter);
		};
	}

	/**
	 * Inject this middleware to the given {@code parameter}.
	 *
	 * @param parameter the parameter to inject this middleware to.
	 * @throws NullPointerException          if the given {@code parameter} is null.
	 * @throws UnsupportedOperationException if this middleware does not support the given
	 *                                       {@code parameter}.
	 * @throws IllegalArgumentException      if this middleware rejected the given {@code
	 *                                       parameter} for some aspect in it.
	 * @since 0.3.0 ~2022.12.26
	 */
	@Contract(mutates = "param")
	void inject(@NotNull T parameter);
}
