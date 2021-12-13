/*
 *	Copyright 2021 Cufy and AgileSA
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
package org.cufy.http.wrapper;

import org.cufy.http.Endpoint;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A cursor that delegates to an endpoint.
 *
 * @param <E>    the type of the endpoint.
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface EndpointWrapper<E extends Endpoint, Self extends EndpointWrapper<E, Self>> {
	// Endpoint

	/**
	 * Invoke the given {@code operator} with the current endpoint as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.27
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self endpoint(@NotNull Consumer<@NotNull E> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.endpoint());
		return (Self) this;
	}

	/**
	 * Set the endpoint to the given {@code endpoint}.
	 *
	 * @param endpoint the endpoint to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code endpoint} is null.
	 * @throws UnsupportedOperationException if the endpoint cannot be changed.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self endpoint(@NotNull E endpoint);

	/**
	 * Return the wrapped endpoint.
	 *
	 * @return the wrapped endpoint.
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	@Contract(pure = true)
	E endpoint();
}
