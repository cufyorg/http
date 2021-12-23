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
package org.cufy.http.client.wrapper;

import org.cufy.http.client.ClientEngine;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A client cursor with shortcut client field accessors.
 *
 * @param <I>    the type of the input parameter (the request).
 * @param <O>    the type of the output parameter (the response).
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientExtension<I, O, Self extends ClientExtension<I, O, Self>> extends ClientWrapper<I, O, Self> {
	// Engine

	/**
	 * Return the engine.
	 *
	 * @return the engine.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	default ClientEngine<? super I, ? super O> engine() {
		return this.client().getEngine();
	}

	/**
	 * Set the engine to the given {@code engine}.
	 *
	 * @param engine the engine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code engine} is null.
	 * @throws UnsupportedOperationException if the client does not support changing its
	 *                                       engine.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self engine(@NotNull ClientEngine<? super I, ? super O> engine) {
		Objects.requireNonNull(engine, "engine");
		this.client().setEngine(engine);
		return (Self) this;
	}

	/**
	 * Replace the engine to the result of invoking the given {@code operator} with the
	 * argument being the previous engine.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the client does not support changing its
	 *                                       engine and the given {@code operator}
	 *                                       returned another engine.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self engine(@NotNull UnaryOperator<@NotNull ClientEngine<? super I, ? super O>> operator) {
		Objects.requireNonNull(operator, "operator");
		ClientEngine<? super I, ? super O> p = this.engine();
		ClientEngine<? super I, ? super O> engine = operator.apply(p);

		if (engine != p)
			this.engine(engine);

		return (Self) this;
	}
}
