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

import org.cufy.http.Call;
import org.cufy.http.Endpoint;
import org.cufy.http.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A delegate implementation of the interface {@link ClientRequest}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public class ClientReqDelegate<E extends Endpoint> implements ClientRequest<E> {
	/**
	 * The req cursor to delegate to.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	protected final ClientResponse<E> res;

	/**
	 * Construct a new delegate cursor delegating to the given {@code res} cursor.
	 *
	 * @param res the cursor to delegate to.
	 * @throws NullPointerException if the given {@code res} is null.
	 * @since 0.3.0 ~2021.12.12
	 */
	public ClientReqDelegate(@NotNull ClientResponse<E> res) {
		Objects.requireNonNull(res, "res");
		this.res = res;
	}

	// Call

	@NotNull
	@Override
	public Call call() {
		return this.res.call();
	}

	@NotNull
	@Override
	public ClientRequest<E> call(@Nullable Call call) {
		this.res.call(call);
		return this;
	}

	// Client

	@NotNull
	@Override
	public Client client() {
		return this.res.client();
	}

	@NotNull
	@Override
	public ClientRequest<E> client(@Nullable Client client) {
		this.res.client(client);
		return this;
	}

	// Endpoint

	@NotNull
	@Override
	public E endpoint() {
		return this.res.endpoint();
	}

	@NotNull
	@Override
	public ClientRequest<E> endpoint(@NotNull E endpoint) {
		this.res.endpoint(endpoint);
		return this;
	}

	// Res

	@NotNull
	@Override
	public ClientResponse<E> res() {
		return this.res;
	}
}
