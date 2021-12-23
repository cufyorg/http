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
package org.cufy.http.pipeline.cursor;

import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.cufy.http.pipeline.wrapper.NextWrapper;
import org.cufy.http.pipeline.wrapper.PipeWrapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A multipurpose pipeline wrapper.
 *
 * @param <T>    the type of the pipeline parameter.
 * @param <Self> the type of the wrapper.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface Pipeline<T, Self extends Pipeline<T, Self>> extends
		NextWrapper<T, Self>, PipeWrapper<T, Self> {
	/**
	 * Replace the current pipe with a new pipe from combining the current pipe with the
	 * given {@code interceptor}.
	 *
	 * @param interceptor the interceptor to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code interceptor} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self intercept(@NotNull Interceptor<T> interceptor) {
		Objects.requireNonNull(interceptor, "interceptor");
		return this.pipe(p -> {
			return Pipe.combine(p, interceptor);
		});
	}

	/**
	 * Replace the current next function with a new next function from combining the
	 * current next function and the given {@code next} function.
	 *
	 * @param next the next function to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code next} function is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self then(@NotNull Next<T> next) {
		Objects.requireNonNull(next, "next");
		return this.next(n -> {
			return Next.combine(n, next);
		});
	}

	/**
	 * Replace the current pipe with a new pipe from combining the current pipe with the
	 * given {@code pipe}.
	 *
	 * @param pipe the pipe to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self use(@NotNull Pipe<T> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		return this.pipe(p -> {
			return Pipe.combine(p, pipe);
		});
	}
}
