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
package org.cufy.http.pipeline.wrapper;

import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.Middleware;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
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
public interface PipelineContext<T, Self extends PipelineContext<T, Self>> extends
		NextWrapper<T, Self>, PipeWrapper<T, Self> {
	/**
	 * <h3>Before Pipeline</h3>
	 * Inject the given {@code middleware} to this.
	 * <br>
	 * A middleware is just a fancy way to encapsulate configurations in one place. The
	 * injection of a middleware has nothing to do with the actual execution of the
	 * pipeline. It will always be invoked right away!
	 *
	 * @param middleware the middleware to be injected.
	 * @return this.
	 * @throws NullPointerException          if the given {@code middleware} is null.
	 * @throws UnsupportedOperationException if this middleware does not support the given
	 *                                       {@code parameter}.
	 * @throws IllegalArgumentException      if this middleware rejected the given {@code
	 *                                       parameter} for some aspect in it.
	 * @since 0.3.0 ~2022.12.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self inject(@NotNull Middleware<? super Self> middleware) {
		Objects.requireNonNull(middleware, "middleware");
		middleware.inject((Self) this);
		return (Self) this;
	}

	/**
	 * <h3>Pipeline</h3>
	 * Replace the current pipe with a new pipe from combining the current pipe with the
	 * given {@code interceptor}.
	 * <br>
	 * The pipe of this pipeline is the first thing to be invoked when the pipeline
	 * starts. So, providing an interceptor to this method will combine the current pipe
	 * with the interceptor given (the current pipe first then the pipe provided later).
	 * This way, the given interceptor will be invoked when the current pipe finishes
	 * executing. And, the interceptor will not have the control to stop the chain. Making
	 * it more safe to use.
	 *
	 * @param interceptor the interceptor to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code interceptor} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self peek(@NotNull Interceptor<T> interceptor) {
		Objects.requireNonNull(interceptor, "interceptor");
		return this.pipe(p -> {
			return Pipe.combine(p, interceptor);
		});
	}

	/**
	 * <h3>After Pipeline</h3>
	 * Replace the current next function with a new next function from combining the
	 * current next function and the given {@code next} function.
	 * <br>
	 * Unlike the pipe, the next function is always the last thing been executed (after
	 * the pipeline finishes executing). The provided next function will be combined with
	 * the current next function (the current next function first then the function
	 * provided). When combining two next functions, each function cannot interrupt the
	 * other (unless an exception is thrown). So, be careful. The next function is
	 * intended to be for cleanup purposes and exception handling. Do not handle the
	 * request/response itself in it.
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
	 * <h3>Pipeline</h3>
	 * Replace the current pipe with a new pipe from combining the current pipe with the
	 * given {@code pipe}.
	 * <br>
	 * The pipe of this pipeline is the first thing to be invoked when the pipeline
	 * starts. So, providing a pipe to this method will combine the current pipe with the
	 * pipe given (the current pipe first then the pipe provided later). This way, the
	 * given pipe will have control over the continuation of the execution of the pipe.
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
