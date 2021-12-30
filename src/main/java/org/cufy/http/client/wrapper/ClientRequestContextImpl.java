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
import org.cufy.http.Request;
import org.cufy.http.Response;
import org.cufy.http.client.Client;
import org.cufy.http.concurrent.Strategy;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An implementation of the interface {@link ClientRequestContext}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public class ClientRequestContextImpl<E extends Endpoint> implements ClientRequestContext<E> {
	/**
	 * The twin response cursor of this.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected final ClientResponseContext<E> res;
	/**
	 * The current set client.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Client<ClientRequestContext<E>, ClientResponseContext<E>> client;
	/**
	 * The endpoint to be used.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected E endpoint;
	/**
	 * The current set next function.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Next<ClientResponseContext<E>> next;
	/**
	 * The current set performer.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@Nullable
	protected Strategy performer;
	/**
	 * The current set pipe.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Pipe<ClientResponseContext<E>> pipe;
	/**
	 * The current set request.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Request request;

	/**
	 * Construct a new client request wrapper with the most none code breaking defaults.
	 * <br>
	 * <br>
	 * Note: the wrapper might not work if some necessary values are not set. This
	 * constructor was made to allow the user to set the fields later and not to be
	 * invoked because the user don't know what to pass.
	 *
	 * @param endpoint the endpoint to be used.
	 * @throws NullPointerException if the given {@code endpoint} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public ClientRequestContextImpl(
			@NotNull E endpoint
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		this.endpoint = endpoint;
		this.client = new Client<>();
		this.pipe = (parameter, next) -> next.invoke();
		this.next = error -> {
			if (error != null)
				//noinspection CallToPrintStackTrace
				error.printStackTrace();
		};
		this.performer = null;
		this.request = new Request();
		this.res = new ClientResponseContextDelegate();
	}

	/**
	 * Construct a new client request wrapper with the given components.
	 *
	 * @param endpoint the endpoint to be used.
	 * @param request  the initial request instance to be set.
	 * @param response the initial response instance (for {@link #res()}).
	 * @throws NullPointerException if the given {@code endpoint} or {@code request} or
	 *                              {@code response} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public ClientRequestContextImpl(
			@NotNull E endpoint,
			@NotNull Request request,
			@NotNull Response response
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.endpoint = endpoint;
		this.client = new Client<>();
		this.pipe = (parameter, next) -> next.invoke();
		this.next = error -> {
			if (error != null)
				//noinspection CallToPrintStackTrace
				error.printStackTrace();
		};
		this.performer = null;
		this.request = request;
		this.res = new ClientResponseContextDelegate(response);
	}

	@NotNull
	@Override
	public ClientRequestContext<E> client(@Nullable Client client) {
		Objects.requireNonNull(client, "client");
		this.client = client;
		return this;
	}

	@NotNull
	@Override
	public Client client() {
		return this.client;
	}

	@NotNull
	@Override
	public ClientRequestContext<E> endpoint(@NotNull E endpoint) {
		Objects.requireNonNull(endpoint, "endpoint");
		this.endpoint = endpoint;
		return this;
	}

	@NotNull
	@Override
	public E endpoint() {
		return this.endpoint;
	}

	@NotNull
	@Override
	public ClientRequestContext<E> next(@NotNull Next<ClientResponseContext<E>> next) {
		Objects.requireNonNull(next, "next");
		this.next = next;
		return this;
	}

	@NotNull
	@Override
	public Next<ClientResponseContext<E>> next() {
		return this.next;
	}

	@NotNull
	@Override
	public ClientRequestContext<E> pipe(@NotNull Pipe<ClientResponseContext<E>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
		return this;
	}

	@NotNull
	@Override
	public Pipe<ClientResponseContext<E>> pipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public ClientRequestContext<E> request(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
		return this;
	}

	@NotNull
	@Override
	public Request request() {
		return this.request;
	}

	@NotNull
	@Override
	public ClientResponseContext<E> res() {
		return this.res;
	}

	@NotNull
	@Override
	public ClientRequestContext<E> strategy(@Nullable Strategy strategy) {
		this.performer = strategy;
		return this;
	}

	@NotNull
	@Override
	public Strategy strategy() {
		return this.performer;
	}

	/**
	 * A client response wrapper delegating to the hosting client request wrapper.
	 *
	 * @author LSafer
	 * @version 0.3.0
	 * @since 0.3.0 ~2021.12.23
	 */
	protected class ClientResponseContextDelegate implements ClientResponseContext<E> {
		/**
		 * The current set response.
		 *
		 * @since 0.3.0 ~2021.12.23
		 */
		@NotNull
		protected Response response;

		/**
		 * Construct a new client response wrapper delegating to the variables in hosting
		 * request wrapper.
		 *
		 * @since 0.3.0 ~2021.12.23
		 */
		protected ClientResponseContextDelegate() {
			this.response = new Response();
		}

		/**
		 * Construct a new client response wrapper delegating to the variables in hosting
		 * request wrapper.
		 *
		 * @param response the initial response instance to be set.
		 * @throws NullPointerException if the given {@code response} is null.
		 * @since 0.3.0 ~2021.12.23
		 */
		protected ClientResponseContextDelegate(@NotNull Response response) {
			Objects.requireNonNull(response, "response");
			this.response = response;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> client(@Nullable Client<ClientRequestContext<E>, ClientResponseContext<E>> client) {
			Objects.requireNonNull(client, "client");
			ClientRequestContextImpl.this.client = client;
			return this;
		}

		@NotNull
		@Override
		public Client<ClientRequestContext<E>, ClientResponseContext<E>> client() {
			return ClientRequestContextImpl.this.client;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> endpoint(@NotNull E endpoint) {
			Objects.requireNonNull(endpoint, "endpoint");
			ClientRequestContextImpl.this.endpoint = endpoint;
			return this;
		}

		@NotNull
		@Override
		public E endpoint() {
			return ClientRequestContextImpl.this.endpoint;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> next(@NotNull Next<ClientResponseContext<E>> next) {
			Objects.requireNonNull(next, "next");
			ClientRequestContextImpl.this.next = next;
			return this;
		}

		@NotNull
		@Override
		public Next<ClientResponseContext<E>> next() {
			return ClientRequestContextImpl.this.next;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> pipe(@NotNull Pipe<ClientResponseContext<E>> pipe) {
			Objects.requireNonNull(pipe, "pipe");
			ClientRequestContextImpl.this.pipe = pipe;
			return this;
		}

		@NotNull
		@Override
		public Pipe<ClientResponseContext<E>> pipe() {
			return ClientRequestContextImpl.this.pipe;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> req() {
			return ClientRequestContextImpl.this;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> response(@NotNull Response response) {
			Objects.requireNonNull(response, "response");
			this.response = response;
			return this;
		}

		@NotNull
		@Override
		public Response response() {
			return this.response;
		}

		@NotNull
		@Override
		public ClientResponseContext<E> strategy(@Nullable Strategy strategy) {
			ClientRequestContextImpl.this.performer = strategy;
			return this;
		}

		@NotNull
		@Override
		public Strategy strategy() {
			return ClientRequestContextImpl.this.performer;
		}
	}
}
