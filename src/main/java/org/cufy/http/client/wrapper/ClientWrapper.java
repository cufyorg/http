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
package org.cufy.http.client.wrapper;

import org.cufy.http.client.Client;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A cursor that delegates to a client.
 *
 * @param <I>    the type of the input parameter (the request).
 * @param <O>    the type of the output parameter (the response).
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.09
 */
public interface ClientWrapper<I, O, Self extends ClientWrapper<I, O, Self>> {
	// Client

	/**
	 * Invoke the given {@code operator} with the current client as its parameter.
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
	default Self client(@NotNull Consumer<@NotNull Client<I, O>> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.client());
		return (Self) this;
	}

	/**
	 * Set the client to the given {@code client}.
	 *
	 * @param client the client to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self client(@Nullable Client<I, O> client);

	/**
	 * Return the wrapped client.
	 *
	 * @return the client wrapped by this cursor.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	Client<I, O> client();
}
