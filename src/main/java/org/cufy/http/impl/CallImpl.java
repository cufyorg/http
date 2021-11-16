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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

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
	 * <b>Default</b>
	 * <br>
	 * Construct a new default call.
	 *
	 * @since 0.3.0 ~2021.11.16
	 */
	public CallImpl() {
		this.extras = new LinkedHashMap<>();
		this.request = new RequestImpl();
		this.response = new ResponseImpl();
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new call with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new call.
	 * @return the call constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static CallImpl call(@NotNull Consumer<Call> builder) {
		Objects.requireNonNull(builder, "builder");
		CallImpl call = new CallImpl();
		builder.accept(call);
		return call;
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
