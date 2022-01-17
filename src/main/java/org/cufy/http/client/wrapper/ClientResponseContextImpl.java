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
import org.cufy.http.Request;
import org.cufy.http.Response;
import org.cufy.http.client.ClientEngine;
import org.cufy.http.concurrent.Performer;
import org.cufy.http.pipeline.Next;
import org.cufy.http.pipeline.Pipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * An implementation of the interface {@link ClientResponseContext}.
 *
 * @param <E> the type of the endpoint.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.23
 */
public class ClientResponseContextImpl<E extends Endpoint> implements ClientResponseContext<E> {
	/**
	 * The twin request cursor of this.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected final ClientRequestContext<E> req;
	/**
	 * The endpoint to be used.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected E endpoint;
	/**
	 * The current set engine.
	 *
	 * @since 1.0.0 ~2022.01.08
	 */
	@NotNull
	protected ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine;
	/**
	 * The extras map.
	 */
	@NotNull
	protected Map<String, Object> extras;
	/**
	 * The current set next function.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Next<ClientResponseContext<E>> next;
	/**
	 * The current set pipe.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Pipe<ClientResponseContext<E>> pipe;
	/**
	 * The current set response.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@NotNull
	protected Response response;
	/**
	 * The current set performer.
	 *
	 * @since 0.3.0 ~2021.12.23
	 */
	@Nullable
	protected Performer performer;

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
	public ClientResponseContextImpl(
			@NotNull E endpoint
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		this.endpoint = endpoint;
		this.engine = (i, next) -> {
			throw new UnsupportedOperationException("Engine Not Set");
		};
		this.pipe = (parameter, next) -> next.invoke();
		this.next = error -> {
		};
		this.performer = null;
		this.response = new Response();
		this.extras = new HashMap<>();
		this.req = new ClientRequestContextDelegate();
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
	public ClientResponseContextImpl(
			@NotNull E endpoint,
			@NotNull Response response,
			@NotNull Request request
	) {
		Objects.requireNonNull(endpoint, "endpoint");
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.endpoint = endpoint;
		this.engine = (i, next) -> {
			throw new UnsupportedOperationException("Engine Not Set");
		};
		this.pipe = (parameter, next) -> next.invoke();
		this.next = error -> {
		};
		this.performer = null;
		this.response = response;
		this.extras = new HashMap<>();
		this.req = new ClientRequestContextDelegate(request);
	}

	@NotNull
	@Override
	public ClientResponseContext<E> endpoint(@NotNull E endpoint) {
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
	public ClientResponseContext<E> extras(@NotNull Map<String, Object> extras) {
		Objects.requireNonNull(extras, "extras");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.extras = extras;
		return this;
	}

	@NotNull
	@Override
	public Map<String, Object> extras() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.extras;
	}

	@NotNull
	@Override
	public ClientResponseContext<E> next(@NotNull Next<ClientResponseContext<E>> next) {
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
	public ClientResponseContext<E> pipe(@NotNull Pipe<ClientResponseContext<E>> pipe) {
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
	public ClientRequestContext<E> req() {
		return this.req;
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
	public ClientResponseContext<E> performer(@Nullable Performer performer) {
		this.performer = performer;
		return this;
	}

	@Nullable
	@Override
	public Performer performer() {
		return this.performer;
	}

	@NotNull
	@Override
	public ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine() {
		return this.engine;
	}

	@NotNull
	@Override
	public ClientResponseContext<E> engine(@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine) {
		Objects.requireNonNull(engine, "engine");
		this.engine = engine;
		return this;
	}

	/**
	 * A client request wrapper delegating to the hosting client response wrapper.
	 *
	 * @author LSafer
	 * @version 0.3.0
	 * @since 0.3.0 ~2021.12.23
	 */
	protected class ClientRequestContextDelegate implements ClientRequestContext<E> {
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
		protected ClientRequestContextDelegate() {
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
		protected ClientRequestContextDelegate(@NotNull Request request) {
			Objects.requireNonNull(request, "request");
			this.request = request;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> endpoint(@NotNull E endpoint) {
			Objects.requireNonNull(endpoint, "endpoint");
			ClientResponseContextImpl.this.endpoint = endpoint;
			return this;
		}

		@NotNull
		@Override
		public E endpoint() {
			return ClientResponseContextImpl.this.endpoint;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> extras(@NotNull Map<String, Object> extras) {
			Objects.requireNonNull(extras, "extras");
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			ClientResponseContextImpl.this.extras = extras;
			return this;
		}

		@NotNull
		@Override
		public Map<String, Object> extras() {
			//noinspection AssignmentOrReturnOfFieldWithMutableType
			return ClientResponseContextImpl.this.extras;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> next(@NotNull Next<ClientResponseContext<E>> next) {
			Objects.requireNonNull(next, "next");
			ClientResponseContextImpl.this.next = next;
			return this;
		}

		@NotNull
		@Override
		public Next<ClientResponseContext<E>> next() {
			return ClientResponseContextImpl.this.next;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> pipe(@NotNull Pipe<ClientResponseContext<E>> pipe) {
			Objects.requireNonNull(pipe, "pipe");
			ClientResponseContextImpl.this.pipe = pipe;
			return this;
		}

		@NotNull
		@Override
		public Pipe<ClientResponseContext<E>> pipe() {
			return ClientResponseContextImpl.this.pipe;
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
			return ClientResponseContextImpl.this;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> performer(@Nullable Performer performer) {
			ClientResponseContextImpl.this.performer = performer;
			return this;
		}

		@Nullable
		@Override
		public Performer performer() {
			return ClientResponseContextImpl.this.performer;
		}

		@NotNull
		@Override
		public ClientRequestContext<E> engine(@NotNull ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine) {
			Objects.requireNonNull(engine, "engine");
			ClientResponseContextImpl.this.engine = engine;
			return this;
		}

		@NotNull
		@Override
		public ClientEngine<ClientRequestContext<? extends Endpoint>, ClientResponseContext<? extends Endpoint>> engine() {
			return ClientResponseContextImpl.this.engine;
		}
	}
}
