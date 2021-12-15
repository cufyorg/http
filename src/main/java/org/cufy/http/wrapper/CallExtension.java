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
package org.cufy.http.wrapper;

import org.cufy.http.Request;
import org.cufy.http.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A call cursor with shortcut call field accessors.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface CallExtension<Self extends CallExtension<Self>> extends CallWrapper<Self> {
	// Exception

	/**
	 * Return the exception.
	 *
	 * @return the exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Nullable
	@Contract(pure = true)
	default Throwable exception() {
		return this.call().getException();
	}

	/**
	 * Set the exception to the given {@code exception}.
	 *
	 * @param exception the exception to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self exception(@Nullable Throwable exception) {
		this.call().setException(exception);
		return (Self) this;
	}

	/**
	 * Replace the exception to the result of invoking the given {@code operator} with the
	 * argument being the previous exception.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       exception and the given {@code operator}
	 *                                       returned another exception.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self exception(@NotNull UnaryOperator<@Nullable Throwable> operator) {
		Objects.requireNonNull(operator, "operator");
		Objects.requireNonNull(operator, "operator");
		Throwable p = this.exception();
		Throwable exception = operator.apply(p);

		if (exception != p)
			this.exception(exception);

		return (Self) this;
	}

	// Request

	/**
	 * Return the request.
	 *
	 * @return the request.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	default Request request() {
		return this.call().getRequest();
	}

	/**
	 * Set the request to the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       request.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self request(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.call().setRequest(request);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current request as the parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self request(@NotNull Consumer<@NotNull Request> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.request());
		return (Self) this;
	}

	// Response

	/**
	 * Return the response.
	 *
	 * @return the response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(pure = true)
	default Response response() {
		return this.call().getResponse();
	}

	/**
	 * Set the response to the given {@code response}.
	 *
	 * @param response the response to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code response} is null.
	 * @throws UnsupportedOperationException if the call does not support changing its
	 *                                       response.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self response(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.call().setResponse(response);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current response as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self response(@NotNull Consumer<@NotNull Response> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.response());
		return (Self) this;
	}
}
