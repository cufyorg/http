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
package org.cufy.http.client.cursor;

import org.cufy.http.client.Client;
import org.cufy.http.Endpoint;
import org.cufy.http.Request;
import org.cufy.http.Response;
import org.cufy.http.concurrent.Performer;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * An implementation of the interface {@link ClientRes}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public class ClientResImpl<E extends Endpoint> implements ClientRes<E> {
	/**
	 * The twin request cursor of this.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected final ClientReq<E> req;
	/**
	 * The current set client.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Client<ClientReq<E>, ClientRes<E>> client;
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
	protected Next<ClientRes<E>> next;
	/**
	 * The current set performer.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@Nullable
	protected Performer performer;
	/**
	 * The current set pipe.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Pipe<ClientRes<E>> pipe;
	/**
	 * The current set response.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Response response;

	/**
	 * Construct a new client response wrapper with the most none code breaking defaults.
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
	public ClientResImpl(
			@NotNull E endpoint
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		this.endpoint = endpoint;
		this.client = new Client<>();
		this.pipe = (parameter, next) -> {
		};
		this.next = error -> {
		};
		this.performer = null;
		this.response = new Response();
		this.req = new ClientReqDelegate();
	}

	/**
	 * Construct a new client response wrapper with the given components.
	 *
	 * @param endpoint the endpoint to be used.
	 * @param response the initial response instance.
	 * @param request  the initial request instance to be set (for {@link #req()}).
	 * @throws NullPointerException if the given {@code endpoint} or {@code response} or
	 *                              {@code request} is null.
	 * @since 0.3.0 ~2021.12.23
	 */
	public ClientResImpl(
			@NotNull E endpoint,
			@NotNull Response response,
			@NotNull Request request
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.endpoint = endpoint;
		this.client = new Client<>();
		this.pipe = (parameter, next) -> {
		};
		this.next = error -> {
		};
		this.performer = null;
		this.req = new ClientReqDelegate(request);
		this.response = response;
	}

	@NotNull
	@Override
	public ClientRes<E> client(@Nullable Client client) {
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
	public ClientRes<E> endpoint(@NotNull E endpoint) {
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
	public ClientRes<E> next(@NotNull Next<ClientRes<E>> next) {
		Objects.requireNonNull(next, "next");
		this.next = next;
		return this;
	}

	@NotNull
	@Override
	public Next<ClientRes<E>> next() {
		return this.next;
	}

	@NotNull
	@Override
	public ClientRes<E> performer(@Nullable Performer performer) {
		this.performer = performer;
		return this;
	}

	@NotNull
	@Override
	public Performer performer() {
		return this.performer;
	}

	@NotNull
	@Override
	public ClientRes<E> pipe(@NotNull Pipe<ClientRes<E>> pipe) {
		Objects.requireNonNull(pipe, "pipe");
		this.pipe = pipe;
		return this;
	}

	@NotNull
	@Override
	public Pipe<ClientRes<E>> pipe() {
		return this.pipe;
	}

	@NotNull
	@Override
	public ClientReq<E> req() {
		return this.req;
	}

	@NotNull
	@Override
	public ClientRes<E> response(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.response = response;
		return this;
	}

	@NotNull
	@Override
	public Response response() {
		return this.response;
	}

	/**
	 * A client request wrapper delegating to the hosting client response wrapper.
	 *
	 * @author LSafer
	 * @version 0.3.0
	 * @since 0.3.0 ~2021.12.23
	 */
	public class ClientReqDelegate implements ClientReq<E> {
		/**
		 * The current set request.
		 *
		 * @since 0.3.0 ~2021.12.23
		 */
		@NotNull
		protected Request request;

		/**
		 * Construct a new client request wrapper delegating to the variables in hosting
		 * response wrapper.
		 *
		 * @since 0.3.0 ~2021.12.23
		 */
		protected ClientReqDelegate() {
			this.request = new Request();
		}

		/**
		 * Construct a new client request wrapper delegating to the variables in hosting
		 * response wrapper.
		 *
		 * @param request the initial request instance to be set.
		 * @throws NullPointerException if the given {@code request} is null.
		 * @since 0.3.0 ~2021.12.23
		 */
		protected ClientReqDelegate(@NotNull Request request) {
			Objects.requireNonNull(request, "request");
			this.request = request;
		}

		@NotNull
		@Override
		public ClientReq<E> client(@Nullable Client<ClientReq<E>, ClientRes<E>> client) {
			Objects.requireNonNull(client, "client");
			ClientResImpl.this.client = client;
			return this;
		}

		@NotNull
		@Override
		public Client<ClientReq<E>, ClientRes<E>> client() {
			return ClientResImpl.this.client;
		}

		@NotNull
		@Override
		public ClientReq<E> endpoint(@NotNull E endpoint) {
			Objects.requireNonNull(endpoint, "endpoint");
			ClientResImpl.this.endpoint = endpoint;
			return this;
		}

		@NotNull
		@Override
		public E endpoint() {
			return ClientResImpl.this.endpoint;
		}

		@NotNull
		@Override
		public ClientReq<E> next(@NotNull Next<ClientRes<E>> next) {
			Objects.requireNonNull(next, "next");
			ClientResImpl.this.next = next;
			return this;
		}

		@NotNull
		@Override
		public Next<ClientRes<E>> next() {
			return ClientResImpl.this.next;
		}

		@NotNull
		@Override
		public ClientReq<E> performer(@Nullable Performer performer) {
			ClientResImpl.this.performer = performer;
			return this;
		}

		@NotNull
		@Override
		public Performer performer() {
			return ClientResImpl.this.performer;
		}

		@NotNull
		@Override
		public ClientReq<E> pipe(@NotNull Pipe<ClientRes<E>> pipe) {
			Objects.requireNonNull(pipe, "pipe");
			ClientResImpl.this.pipe = pipe;
			return this;
		}

		@NotNull
		@Override
		public Pipe<ClientRes<E>> pipe() {
			return ClientResImpl.this.pipe;
		}

		@NotNull
		@Override
		public ClientReq<E> request(@NotNull Request request) {
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
		public ClientRes<E> res() {
			return ClientResImpl.this;
		}
	}
}
