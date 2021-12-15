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

import org.cufy.http.Call;
import org.cufy.http.Endpoint;
import org.cufy.http.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A delegate implementation of the interface {@link ClientResponse}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public class ClientResDelegate<E extends Endpoint> implements ClientResponse<E> {
	/**
	 * The req cursor to delegate to.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	protected final ClientRequest<E> req;

	/**
	 * Construct a new delegate cursor delegating to the given {@code req} cursor.
	 *
	 * @param req the cursor to delegate to.
	 * @throws NullPointerException if the given {@code req} is null.
	 * @since 0.3.0 ~2021.12.12
	 */
	public ClientResDelegate(@NotNull ClientRequest<E> req) {
		Objects.requireNonNull(req, "req");
		this.req = req;
	}

	// Call

	@NotNull
	@Override
	public Call call() {
		return this.req.call();
	}

	@NotNull
	@Override
	public ClientResponse<E> call(@Nullable Call call) {
		this.req.call(call);
		return this;
	}

	// Client

	@NotNull
	@Override
	public Client client() {
		return this.req.client();
	}

	@NotNull
	@Override
	public ClientResponse<E> client(@Nullable Client client) {
		this.req.client(client);
		return this;
	}

	// Endpoint

	@NotNull
	@Override
	public E endpoint() {
		return this.req.endpoint();
	}

	@NotNull
	@Override
	public ClientResponse<E> endpoint(@NotNull E endpoint) {
		this.req.endpoint(endpoint);
		return this;
	}

	// Req

	@NotNull
	@Override
	public ClientRequest<E> req() {
		return this.req;
	}
}
