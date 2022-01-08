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
import org.cufy.http.concurrent.Task;
import org.cufy.http.concurrent.wrapper.TaskContext;
import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.cufy.http.pipeline.wrapper.PipelineContext;
import org.cufy.http.wrapper.RequestContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * An extended version of the interface {@link RequestContext} containing additional
 * client side properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientRequestContext<E extends Endpoint> extends
		RequestContext<E, ClientResponseContext<E>, ClientRequestContext<E>>,
		ClientEngineContext<ClientEngine<ClientRequestContext<?>, ClientResponseContext<?>>, ClientRequestContext<E>>,
		PipelineContext<ClientResponseContext<E>, ClientRequestContext<E>>,
		TaskContext<ClientRequestContext<E>> {
	/**
	 * A performance that performs the necessary steps to start a connection.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	Task<ClientRequestContext<?>> CONNECT = (req, callback) -> {
		ClientEngine engine = req.engine();
		ClientResponseContext res = req.res();
		Pipe pipe = req.pipe();
		Next next = req.next();

		engine.connect(req, error -> {
			if (error != null) {
				try {
					next.invoke(error);
				} finally {
					callback.run();
				}
				return;
			}

			try {
				pipe.invoke(res, next);
			} catch (Throwable throwable) {
				next.invoke(throwable);
			} finally {
				callback.run();
			}
		});
	};

	/**
	 * Perform the connection.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default ClientRequestContext<E> connect() {
		this.perform(ClientRequestContext.CONNECT);
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
	default ClientRequestContext<E> connected(@NotNull Interceptor<ClientResponseContext<E>> interceptor) {
		return this.intercept(interceptor);
	}
}
