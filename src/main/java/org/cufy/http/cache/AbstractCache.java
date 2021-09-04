/*
 *	Copyright 2021 Cufy
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
package org.cufy.http.cache;

import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Cache}.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11 ~2021.09.04
 */
public class AbstractCache implements Cache {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 3279387250117767741L;

	/**
	 * The creation instant.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected Instant creationInstant;
	/**
	 * The cached request.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected Request request;
	/**
	 * The cached response.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected Response response;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default cache.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	public AbstractCache() {
		this.request = Request.request();
		this.response = Response.response();
		this.creationInstant = Instant.now();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new cache from copying the given {@code cache}.
	 *
	 * @param cache the cache to copy.
	 * @throws NullPointerException if the given {@code cache} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public AbstractCache(@NotNull Cache cache) {
		Objects.requireNonNull(cache, "cache");
		this.request = Request.request(cache.getRequest());
		this.response = Response.response(cache.getResponse());
		this.creationInstant = cache.getCreationInstant();
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new cache from the given components.
	 *
	 * @param request         the cached request.
	 * @param response        the cached response.
	 * @param creationInstant the creation instant.
	 * @throws NullPointerException if the given {@code request} or {@code response} or
	 *                              {@code creationInstant} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public AbstractCache(@NotNull Request request, @NotNull Response response, @NotNull Instant creationInstant) {
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.request = request;
		this.response = response;
		this.creationInstant = creationInstant;
	}

	@NotNull
	@Override
	public AbstractCache clone() {
		try {
			AbstractCache clone = (AbstractCache) super.clone();
			clone.request = this.request.clone();
			clone.response = this.response.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Cache) {
			Cache cache = (Cache) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.request, cache.getRequest()) &&
				   Objects.equals(this.response, cache.getResponse());
		}

		return false;
	}

	@NotNull
	@Override
	public Instant getCreationInstant() {
		return this.creationInstant;
	}

	@NotNull
	@Override
	public Request getRequest() {
		return this.request;
	}

	@NotNull
	@Override
	public Response getResponse() {
		return this.response;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.request.hashCode() ^
			   this.response.hashCode();
	}

	@NotNull
	@Override
	public Cache setCreationInstant(@NotNull Instant creationInstant) {
		Objects.requireNonNull(creationInstant, "creationInstant");
		this.creationInstant = creationInstant;
		return this;
	}

	@NotNull
	@Override
	public Cache setRequest(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
		return this;
	}

	@NotNull
	@Override
	public Cache setResponse(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.response = response;
		return this;
	}

	@NotNull
	@Override
	public String toString() {
		return "Cache " + System.identityHashCode(this);
	}
}
