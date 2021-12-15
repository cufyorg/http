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

import org.cufy.http.Endpoint;
import org.cufy.http.Response;
import org.cufy.http.wrapper.CallExtension;
import org.cufy.http.wrapper.EndpointResponse;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * An extended version of the interface {@link EndpointResponse} containing additional client side
 * properties.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ClientResponse<E extends Endpoint> extends ClientExtension<ClientResponse<E>>, CallExtension<ClientResponse<E>>, EndpointResponse<E, ClientRequest<E>, ClientResponse<E>> {
	@NotNull
	@Override
	default Response response() {
		return CallExtension.super.response();
	}

	@NotNull
	@Override
	default ClientResponse<E> response(@NotNull Consumer<@NotNull Response> operator) {
		return CallExtension.super.response(operator);
	}

	@NotNull
	@Override
	default ClientResponse<E> response(@NotNull Response response) {
		return CallExtension.super.response(response);
	}
}
