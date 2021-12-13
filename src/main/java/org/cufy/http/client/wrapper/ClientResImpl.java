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
 * Basic implementation of the interface {@link ClientRequest}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public class ClientResImpl<E extends Endpoint> implements ClientResponse<E> {
	/**
	 * The current call.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	protected Call call;
	/**
	 * The current client.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	protected Client client;
	/**
	 * The endpoint.
	 *
	 * @since 0.3.0 ~2021.12.12
	 */
	@NotNull
	protected E endpoint;
	/**
	 * The request wrapper instance. (lazy initialized)
	 *
	 * @since 0.3.0 ~2021.12.13
	 */
	@Nullable
	protected ClientRequest<E> req;

	/**
	 * Construct a new response cursor with default values.
	 *
	 * @param endpoint the endpoint to be used.
	 * @throws NullPointerException if the given {@code endpoint} is null.
	 * @since 0.3.0 ~2021.12.12
	 */
	public ClientResImpl(@NotNull E endpoint) {
		this.endpoint = endpoint;
		this.client = new Client();
		this.call = new Call();
	}

	/**
	 * Construct a new response cursor with the given {@code client} and {@code call}.
	 *
	 * @param endpoint the endpoint to be used.
	 * @param client   the initial client to be set.
	 * @param call     the initial call to be set.
	 * @throws NullPointerException if the given {@code  endpoint} or {@code client} or
	 *                              {@code call} is null.
	 * @since 0.3.0 ~2021.12.12
	 */
	public ClientResImpl(@NotNull E endpoint, @NotNull Client client, @NotNull Call call) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(client, "client");
		Objects.requireNonNull(call, "call");
		this.endpoint = endpoint;
		this.client = client;
		this.call = call;
	}

	// Call

	@NotNull
	@Override
	public Call call() {
		return this.call;
	}

	@NotNull
	@Override
	public ClientResponse<E> call(@Nullable Call call) {
		Objects.requireNonNull(call, "call");
		this.call = call;
		return this;
	}

	// Client

	@NotNull
	@Override
	public Client client() {
		return this.client;
	}

	@NotNull
	@Override
	public ClientResponse<E> client(@Nullable Client client) {
		Objects.requireNonNull(client, "client");
		this.client = client;
		return this;
	}

	// Endpoint

	@NotNull
	@Override
	public E endpoint() {
		return this.endpoint;
	}

	@NotNull
	@Override
	public ClientResponse<E> endpoint(@NotNull E endpoint) {
		Objects.requireNonNull(endpoint, "endpoint");
		this.endpoint = endpoint;
		return this;
	}

	// Req

	@NotNull
	@Override
	public ClientRequest<E> req() {
		if (this.req == null)
			this.req = new ClientReqDelegate<>(this);
		return this.req;
	}
}
