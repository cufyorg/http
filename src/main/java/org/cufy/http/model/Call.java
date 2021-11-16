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
package org.cufy.http.model;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A call represents a single connection.
 *
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.11.16
 */
public interface Call extends Cloneable, Serializable {
	/**
	 * Replace the exception of this to be the result of invoking the given {@code
	 * operator} with the argument being the current exception. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this call does not allow changing its
	 *                                       exception and the given {@code operator}
	 *                                       returned another exception.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call exception(@NotNull UnaryOperator<Throwable> operator) {
		Objects.requireNonNull(operator, "operator");
		Throwable p = this.getException();
		Throwable exception = operator.apply(p);

		if (exception != p)
			this.setException(exception);

		return this;
	}

	/**
	 * Replace the extras map of this call to be the result of invoking the given {@code
	 * operator} with the current extras map of this call. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the extras map of this call cannot be
	 *                                       changed and the returned extras map from the
	 *                                       given {@code operator} is different from the
	 *                                       current extras map.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call extras(@NotNull UnaryOperator<Map<@Nullable String, @Nullable Object>> operator) {
		Objects.requireNonNull(operator, "operator");
		Map<String, Object> e = this.getExtras();
		Map<String, Object> extras = operator.apply(e);

		if (extras != null && extras != e)
			this.setExtras(extras);

		return this;
	}

	/**
	 * Replace the request of this to be the result of invoking the given {@code operator}
	 * with the argument being the current request. If the {@code operator} returned
	 * {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this call does not allow changing its
	 *                                       request and the given {@code operator}
	 *                                       returned another request.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call request(@NotNull UnaryOperator<Request> operator) {
		Objects.requireNonNull(operator, "operator");
		Request p = this.getRequest();
		Request request = operator.apply(p);

		if (request != null && request != p)
			this.setRequest(request);

		return this;
	}

	/**
	 * Replace the response of this to be the result of invoking the given {@code
	 * operator} with the argument being the current response. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this call does not allow changing its
	 *                                       response and the given {@code operator}
	 *                                       returned another response.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call response(@NotNull UnaryOperator<Response> operator) {
		Objects.requireNonNull(operator, "operator");
		Response p = this.getResponse();
		Response response = operator.apply(p);

		if (response != null && response != p)
			this.setResponse(response);

		return this;
	}

	/**
	 * Set the exception of this from the given {@code exception}.
	 *
	 * @param exception the exception to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       exception.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call setException(@Nullable Throwable exception) {
		throw new UnsupportedOperationException("exception");
	}

	/**
	 * Set the extras map to be the given map.
	 *
	 * @param extras the new extras map.
	 * @return this.
	 * @throws NullPointerException          if the given {@code extras} is null.
	 * @throws UnsupportedOperationException if the extras map of this call cannot be
	 *                                       changed.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		throw new UnsupportedOperationException("setExtras");
	}

	/**
	 * Set the request of this from the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       request.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call setRequest(@NotNull Request request) {
		throw new UnsupportedOperationException("request");
	}

	/**
	 * Set the response of this from the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if this call does not support changing its
	 *                                       response.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Call setResponse(@NotNull Response response) {
		throw new UnsupportedOperationException("response");
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
	Call clone();

	/**
	 * Return the exception of this call. (if any)
	 *
	 * @return the exception of this call.
	 * @since 0.3.0 ~2021.11.16
	 */
	@Nullable
	@Contract(pure = true)
	Throwable getException();

	/**
	 * Return the extras map.
	 *
	 * @return the extras map.
	 * @since 0.3.0 ~2021.11.16
	 */
	@NotNull
	@Contract(pure = true)
	Map<@Nullable String, @Nullable Object> getExtras();

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
	Request getRequest();

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
	Response getResponse();
}
