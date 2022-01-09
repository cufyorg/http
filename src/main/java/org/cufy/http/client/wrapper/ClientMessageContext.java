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

import org.cufy.http.Endpoint;
import org.cufy.http.Message;
import org.cufy.http.client.ClientEngine;
import org.cufy.http.concurrent.wrapper.StrategyContext;
import org.cufy.http.pipeline.Catcher;
import org.cufy.http.pipeline.Interceptor;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.cufy.http.pipeline.wrapper.PipelineContext;
import org.cufy.http.wrapper.MessageContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A multipurpose client-side message wrapper.
 *
 * @param <E>    the type of the endpoint.
 * @param <M>    the type of the message.
 * @param <Self> the type of this wrapper.
 * @author LSafer
 * @version 1.0.0
 * @since 1.0.0 ~2022.01.08
 */
public interface ClientMessageContext<
		E extends Endpoint,
		M extends Message,
		Self extends ClientMessageContext<E, M, Self>
		> extends
		MessageContext<E, M, ClientRequestContext<E>, ClientResponseContext<E>, Self>,
		ClientEngineContext<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>, Self>,
		PipelineContext<ClientResponseContext<E>, Self>,
		StrategyContext<Self> {
	/**
	 * Perform the connection.
	 *
	 * @return this.
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	@Contract("->this")
	default Self connect() {
		return this.perform((context, callback) -> {
			ClientEngine<ClientRequestContext<?>, ClientResponseContext<?>> engine = context.engine();
			ClientRequestContext<E> req = context.req();
			ClientResponseContext<E> res = context.res();
			Pipe<ClientResponseContext<E>> pipe = context.pipe();
			Next<ClientResponseContext<E>> next = context.next();

			try {
				engine.connect(req, error -> {
					if (error == null)
						try {
							pipe.invoke(res, next);
						} catch (Throwable e) {
							next.invoke(e);
						} finally {
							callback.run();
						}
					else
						try {
							next.invoke(error);
						} finally {
							callback.run();
						}
				});
			} catch (Throwable e) {
				try {
					next.invoke(e);
				} finally {
					callback.run();
				}
			}
		});
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
	@Contract(value = "_->this", mutates = "this")
	default Self connected(@NotNull Interceptor<ClientResponseContext<E>> interceptor) {
		return this.intercept(interceptor);
	}

	/**
	 * If the connection fails. Execute the given {@code next} function.
	 * <br>
	 * This function is an alias for {@link #handle(Catcher)}.
	 *
	 * @param catcher the catcher to be used.
	 * @return this.
	 * @throws NullPointerException if the given {@code catcher} is null.
	 * @since 1.0.0 ~2022.01.09
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self failed(@NotNull Catcher<ClientResponseContext<E>> catcher) {
		return this.handle(catcher);
	}
}
