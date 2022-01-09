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

import org.cufy.http.pipeline.Catcher;
import org.cufy.http.pipeline.Next;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A cursor that delegates to a next function.
 *
 * @param <T>    the type of the parameter the pipe accepts.
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public interface NextWrapper<T, Self extends NextWrapper<T, Self>> {
	// Next

	/**
	 * Set the next function to the given {@code catcher}.
	 *
	 * @param catcher the catcher to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code catcher} is null.
	 * @since 1.0.0 ~2022.01.09
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self catcher(@NotNull Catcher<T> catcher) {
		Objects.requireNonNull(catcher, "catcher");
		return this.next(catcher);
	}

	/**
	 * Replace the next function to the result of invoking the given {@code operator} with
	 * the argument being the previous next function.
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
	default Self next(@NotNull UnaryOperator<@NotNull Next<T>> operator) {
		Objects.requireNonNull(operator, "operator");
		Next<T> p = this.next();
		Next<T> next = operator.apply(p);

		if (next != p)
			this.next(next);

		return (Self) this;
	}

	/**
	 * Set the next function to the given {@code next}.
	 *
	 * @param next the next function to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code next} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self next(@NotNull Next<T> next);

	/**
	 * Return the next function.
	 *
	 * @return the next function.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	Next<T> next();
}
