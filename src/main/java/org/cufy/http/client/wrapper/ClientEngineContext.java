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

/**
 * An extended version of {@link ClientEngineWrapper}.
 *
 * @param <N>    the type of the engine.
 * @param <Self> the type of this wrapper.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.08
 */
public interface ClientEngineContext<N extends ClientEngine<?, ?>, Self extends ClientEngineContext<N, Self>> extends ClientEngineWrapper<N, Self> {
	/**
	 * Make the given {@code engine} executes after the current engine.
	 *
	 * @param engine the engine to be combined with the current engine.
	 * @return this.
	 * @throws NullPointerException if the given {@code engine} is null.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self post(@NotNull N engine) {
		Objects.requireNonNull(engine, "engine");
		return this.engine(e -> (N) ClientEngine.combine(
				(ClientEngine<Object, Object>) e,
				(ClientEngine<Object, Object>) engine
		));
	}

	/**
	 * Make the given {@code engine} executes before the current engine.
	 *
	 * @param engine the engine to be combined with the current engine.
	 * @return this.
	 * @throws NullPointerException if the given {@code engine} is null.
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self pre(@NotNull N engine) {
		Objects.requireNonNull(engine, "engine");
		return this.engine(e -> (N) ClientEngine.combine(
				(ClientEngine<Object, Object>) engine,
				(ClientEngine<Object, Object>) e
		));
	}
}
