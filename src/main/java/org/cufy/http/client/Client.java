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
package org.cufy.http.client;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * A client is an object that holds the most basic and essential necessary data and
 * configuration to perform a connection.
 *
 * @param <I> the type of the input parameter (the request).
 * @param <O> the type of the output parameter (the response).
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public class Client<I, O> {
	/**
	 * The client engine.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected ClientEngine<? super I, ? super O> engine;

	/**
	 * Construct a new client with the most none code breaking defaults.
	 * <br>
	 * <br>
	 * Note: the client might not work if some necessary fields are not set. This
	 * constructor was made to allow the user to set the fields later and not to be
	 * invoked because the user don't know what to pass.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	public Client() {
		this.engine = (i, next) -> {
			throw new UnsupportedOperationException("Engine Not Set");
		};
	}

	/**
	 * Construct a new client with the given components.
	 *
	 * @param engine the engine of the client.
	 * @throws NullPointerException if the given {@code engine} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public Client(@NotNull ClientEngine<I, O> engine) {
		Objects.requireNonNull(engine, "engine");
		this.engine = engine;
	}

	/**
	 * Construct a new client with the given {@code builder}.
	 *
	 * @param builder the builder to be invoked with the newly constructed client.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public Client(@NotNull Consumer<Client> builder) {
		Objects.requireNonNull(builder, "builder");
		this.engine = (input, next) -> {
			throw new UnsupportedOperationException("Engine Not Set");
		};
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Return the engine of this client.
	 *
	 * @return the engine of this client.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract(pure = true)
	public ClientEngine<? super I, ? super O> getEngine() {
		return this.engine;
	}

	/**
	 * Set the engine of this client to be the given {@code engine}.
	 *
	 * @param engine the engine to be set.
	 * @throws NullPointerException if the given {@code engine} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	@Contract(mutates = "this")
	public void setEngine(@NotNull ClientEngine<? super I, ? super O> engine) {
		Objects.requireNonNull(engine, "engine");
		this.engine = engine;
	}
}
