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
package org.cufy.http.concurrent.wrapper;

import org.cufy.http.concurrent.Performer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A cursor that delegates to a performer.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.09
 */
public interface PerformerWrapper<Self extends PerformerWrapper<Self>> {
	// Performer

	/**
	 * Replace the performer with the result of invoking the given {@code operator} with
	 * the current performer as its parameter.
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
	default Self performer(@NotNull UnaryOperator<@Nullable Performer> operator) {
		Objects.requireNonNull(operator, "operator");
		Performer p = this.performer();
		Performer performer = operator.apply(p);

		if (performer != p)
			this.performer(performer);

		return (Self) this;
	}

	/**
	 * Set the performer to the given {@code performer}.
	 *
	 * @param performer the performer to be set.
	 * @return this.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self performer(@Nullable Performer performer);

	/**
	 * Return the wrapped performer.
	 *
	 * @return the performer wrapped by this cursor.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	@Contract(pure = true)
	Performer performer();
}
