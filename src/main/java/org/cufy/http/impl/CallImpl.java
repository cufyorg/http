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
package org.cufy.http.impl;

import org.cufy.http.model.Call;
import org.cufy.http.model.Request;
import org.cufy.http.model.Response;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Call}.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.16
 */
public class CallImpl implements Call {
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
	 * Construct a new call from the given components.
	 *
	 * @param request  the request object of the constructed call.
	 * @param response the response object of the constructed call.
	 * @throws NullPointerException if the given {@code request} or {@code response} is
	 *                              null.
	 * @since 0.3.0 ~2021.11.17
	 */
	@ApiStatus.Internal
	public CallImpl(@NotNull Request request, @NotNull Response response) {
		Objects.requireNonNull(request, "request");
		Objects.requireNonNull(response, "response");
		this.extras = new LinkedHashMap<>();
		this.request = request;
		this.response = response;
	}

	@NotNull
	@Override
	public CallImpl clone() {
		try {
			CallImpl clone = (CallImpl) super.clone();
			clone.request = this.request.clone();
			clone.response = this.response.clone();
			clone.exception = this.exception;
			clone.extras = new LinkedHashMap<>(this.extras);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Nullable
	@Override
	public Throwable getException() {
		return this.exception;
	}

	@NotNull
	@Override
	public Map<@Nullable String, @Nullable Object> getExtras() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.extras;
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

	@NotNull
	@Override
	public Call setException(@Nullable Throwable exception) {
		this.exception = exception;
		return this;
	}

	@NotNull
	@Override
	public Call setExtras(@NotNull Map<String, Object> extras) {
		Objects.requireNonNull(extras, "extras");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.extras = extras;
		return this;
	}

	@NotNull
	@Override
	public Call setRequest(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
		return this;
	}

	@NotNull
	@Override
	public Call setResponse(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.response = response;
		return this;
	}

	@Override
	public String toString() {
		return "Call " + System.identityHashCode(this);
	}
}
