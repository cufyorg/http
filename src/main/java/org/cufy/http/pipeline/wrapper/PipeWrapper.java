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
package org.cufy.http.pipeline.wrapper;

import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.Pipe;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A cursor that delegates to a pipe.
 *
 * @param <T>    the type of the parameter of the pipe.
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public interface PipeWrapper<T, Self extends PipeWrapper<T, Self>> {
	// Pipe

	/**
	 * Set the pipe to the given {@code interceptor}.
	 *
	 * @param interceptor the interceptor to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code interceptor} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self interceptor(@NotNull Interceptor<T> interceptor) {
		Objects.requireNonNull(interceptor, "interceptor");
		return this.pipe(interceptor);
	}

	/**
	 * Replace the pipe to the result of invoking the given {@code operator} with the
	 * argument being the previous pipe.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self pipe(@NotNull UnaryOperator<@NotNull Pipe<T>> operator) {
		Objects.requireNonNull(operator, "operator");
		Pipe<T> p = this.pipe();
		Pipe<T> pipe = operator.apply(p);

		if (pipe != p)
			this.pipe(pipe);

		return (Self) this;
	}

	/**
	 * Set the pipe to the given {@code pipe}.
	 *
	 * @param pipe the pipe to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code pipe} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self pipe(@NotNull Pipe<T> pipe);

	/**
	 * Return the pipe.
	 *
	 * @return the pipe.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	Pipe<T> pipe();
}
