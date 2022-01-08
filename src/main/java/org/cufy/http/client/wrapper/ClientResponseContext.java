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

import org.cufy.http.Endpoint;
import org.cufy.http.client.ClientEngine;
import org.cufy.http.concurrent.wrapper.TaskContext;
import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.wrapper.PipelineContext;
import org.cufy.http.wrapper.ResponseContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An extended version of the interface {@link ResponseContext} containing additional
 * client side properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientResponseContext<E extends Endpoint> extends
		ResponseContext<E, ClientRequestContext<E>, ClientResponseContext<E>>,
		ClientEngineContext<ClientEngine<ClientRequestContext<?>, ClientResponseContext<?>>, ClientResponseContext<E>>,
		PipelineContext<ClientResponseContext<E>, ClientResponseContext<E>>,
		TaskContext<ClientResponseContext<E>> {
	/**
	 * Perform the connection.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default ClientResponseContext<E> connect() {
		this.perform(this.req(), ClientRequestContext.CONNECT);
		return this;
	}

	/**
	 * Intercept the response with the given {@code interceptor}.
	 * <br>
	 * This function is an alias for {@link #intercept(Interceptor)}.
	 *
	 * @param interceptor the interceptor to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code interceptor} is null.
	 * @since 0.3.0 ~2022.01.05
	 */
	@NotNull
	@Contract("_->this")
	default ClientResponseContext<E> connected(@NotNull Interceptor<ClientResponseContext<E>> interceptor) {
		return this.intercept(interceptor);
	}
}
