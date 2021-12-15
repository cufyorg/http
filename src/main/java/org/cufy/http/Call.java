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
package org.cufy.http;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A call represents a single connection.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.16
 */
public class Call implements Cloneable, Serializable {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -2619372842480368368L;

	/**
	 * The exception of this call. (null, if none)
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	@Nullable
	protected Throwable exception;
	/**
	 * The extras map.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	protected Map<@Nullable String, @Nullable Object> extras;
	/**
	 * The request of this call.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	protected Request request;
	/**
	 * The response of this call.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	protected Response response;

	/**
	 * Construct a new call.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Call() {
		this.extras = new LinkedHashMap<>();
		this.request = new Request();
		this.response = new Response();
	}

	/**
	 * Construct a new call from the given components.
	 *
	 * @param request  the request object of the constructed call.
	 * @param response the response object of the constructed call.
	 * @throws NullPointerException if the given {@code request} or {@code response} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.17
	 */
	public Call(@NotNull Request request, @NotNull Response response) {
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.extras = new LinkedHashMap<>();
		this.request = request;
		this.response = response;
	}

	/**
	 * Construct a new call with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new call.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.16
	 */
	public Call(@NotNull Consumer<@NotNull Call> builder) {
		Objects.requireNonNull(builder, "builder");
		this.extras = new LinkedHashMap<>();
		this.request = new Request();
		this.response = new Response();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Capture this call into a new object.
	 * <br>
	 * The clone must have a cloned request and response of this.
	 * <br>
	 * Note: the cloned call will not be {@link #equals(Object) equal} to this call.
	 *
	 * @return a clone of this call.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Call clone() {
		try {
			Call clone = (Call) super.clone();
			clone.request = this.request.clone();
			clone.response = this.response.clone();
			clone.exception = this.exception;
			clone.extras = new LinkedHashMap<>(this.extras);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	/**
	 * Return a string representation of this call.
	 *
	 * @return a string representation of this call.
	 * @since 0.3.0 ~2021.11.26
	 */
	@NotNull
	@Contract(pure = true)
	@Override
	public String toString() {
		return "Call " + System.identityHashCode(this);
	}

	/**
	 * Return the exception of this call. (if any)
	 *
	 * @return the exception of this call.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Nullable
	@Contract(pure = true)
	public Throwable getException() {
		return this.exception;
	}

	/**
	 * Return the extras map.
	 *
	 * @return the extras map.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(pure = true)
	public Map<@Nullable String, @Nullable Object> getExtras() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.extras;
	}

	/**
	 * Return the request of this call.
	 * <p>
	 * The request will initially contain default data until filled manually or
	 * automatically.
	 * <p>
	 * A call will always return the same request reference unless changed by {@link
	 * #setRequest(Request)}.
	 *
	 * @return the request of this call.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(pure = true)
	public Request getRequest() {
		return this.request;
	}

	/**
	 * Return the response of this call.
	 * <p>
	 * The response will initially contain default data until filled manually or
	 * automatically.
	 * <p>
	 * A call will always return the same response reference unless changed by {@link
	 * #setResponse(Response)}.
	 *
	 * @return the response of this call.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(pure = true)
	public Response getResponse() {
		return this.response;
	}

	/**
	 * Set the exception of this from the given {@code exception}.
	 *
	 * @param exception the exception to be set.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       exception.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Contract(mutates = "this")
	public void setException(@Nullable Throwable exception) {
		this.exception = exception;
	}

	/**
	 * Set the extras map to be the given map.
	 *
	 * @param extras the new extras map.
	 * @throws NullPointerException          if the given {@code extras} is null.
	 * @throws UnsupportedOperationException if the extras map of this call cannot be
	 *                                       changed.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Contract(mutates = "this")
	public void setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		Objects.requireNonNull(extras, "extras");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.extras = extras;
	}

	/**
	 * Set the request of this from the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       request.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Contract(mutates = "this")
	public void setRequest(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
	}

	/**
	 * Set the response of this from the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       response.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Contract(mutates = "this")
	public void setResponse(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.response = response;
	}
}
