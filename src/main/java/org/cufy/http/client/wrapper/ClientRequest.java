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
package org.cufy.http.client.wrapper;

import org.cufy.http.Endpoint;
import org.cufy.http.Request;
import org.cufy.http.client.Emit;
import org.cufy.http.client.Performer;
import org.cufy.http.wrapper.CallExtension;
import org.cufy.http.wrapper.EndpointRequest;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * An extended version of the interface {@link EndpointRequest} containing additional client side
 * properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientRequest<E extends Endpoint> extends ClientExtension<ClientRequest<E>>, CallExtension<ClientRequest<E>>, EndpointRequest<E, ClientResponse<E>, ClientRequest<E>> {
	// Request

	@NotNull
	@Override
	default Request request() {
		return CallExtension.super.request();
	}

	@NotNull
	@Override
	default ClientRequest<E> request(@NotNull Request request) {
		return CallExtension.super.request(request);
	}

	@NotNull
	@Override
	default ClientRequest<E> request(@NotNull Consumer<@NotNull Request> operator) {
		return CallExtension.super.request(operator);
	}

	// .connect

	/**
	 * Perform the connection asynchronously.
	 *
	 * @return the response wrapper instance of this.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(mutates = "this")
	default ClientResponse<E> connect() {
		return this.emit(Emit.CONNECT, this)
				   .res();
	}

	/**
	 * Perform the connection using the given {@code performer}.
	 * <br>
	 * Exceptions thrown by the given {@code performer} will be caught safely. But,
	 * exception by a thread created by the performer is left for the callback to handle.
	 *
	 * @param performer the performer to be used to perform the connection.
	 * @return the response wrapper instance of this.
	 * @throws NullPointerException if the given {@code performer} is null.
	 * @since 0.3.0 ~2021.12.13
	 */
	@NotNull
	@Contract(mutates = "this")
	default ClientResponse<E> connect(@NotNull Performer<ClientRequest<?>> performer) {
		return this.perform(performer)
				   .res();
	}
}
