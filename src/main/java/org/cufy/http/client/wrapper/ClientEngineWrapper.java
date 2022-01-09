/*
 *	Copyright 2021-2022 Cufy
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

import org.cufy.http.client.ClientEngine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A wrapper wrapping a client engine.
 *
 * @param <I>    the type of the input parameter (the request).
 * @param <O>    the type of the output parameter (the response).
 * @param <Self> the type of this.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.08
 */
public interface ClientEngineWrapper<I, O, Self extends ClientEngineWrapper<I, O, Self>> {
	// Engine

	/**
	 * Replace the engine to the result of invoking the given {@code operator} with the
	 * argument being the previous engine.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self engine(@NotNull UnaryOperator<@NotNull ClientEngine<I, O>> operator) {
		Objects.requireNonNull(operator, "operator");
		ClientEngine<I, O> e = this.engine();
		ClientEngine<I, O> engine = operator.apply(e);

		if (engine != e)
			this.engine(engine);

		return (Self) this;
	}

	/**
	 * Set the engine to the given {@code engine}.
	 *
	 * @param engine the engine to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code engine} is null.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	Self engine(@NotNull ClientEngine<I, O> engine);

	/**
	 * Return the engine.
	 *
	 * @return the engine.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(pure = true)
	ClientEngine<I, O> engine();
}
