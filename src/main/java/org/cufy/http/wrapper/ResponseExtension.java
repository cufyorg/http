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
package org.cufy.http.wrapper;

import org.cufy.http.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A call cursor with shortcut response field accessors.
 *
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface ResponseExtension<Self extends ResponseExtension<Self>> extends ResponseWrapper<Self>, MessageExtension<Response, Self> {
	// HttpVersion

	/**
	 * Return the http version.
	 *
	 * @return the http version.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default HttpVersion httpVersion() {
		return this.response().getStatusLine().getHttpVersion();
	}

	/**
	 * Set the httpVersion to the given {@code httpVersion}.
	 *
	 * @param httpVersion the httpVersion to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its http-version.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self httpVersion(@NotNull HttpVersion httpVersion) {
		this.response().getStatusLine().setHttpVersion(httpVersion);
		return (Self) this;
	}

	/**
	 * Replace the httpVersion to the result of invoking the given {@code operator} with
	 * the argument being the previous httpVersion.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its http-version and the given {@code
	 *                                       operator} returned another http-version.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self httpVersion(@NotNull UnaryOperator<@NotNull HttpVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		HttpVersion hv = this.httpVersion();
		HttpVersion httpVersion = operator.apply(hv);

		if (httpVersion != hv)
			this.httpVersion(httpVersion);

		return (Self) this;
	}

	// ReasonPhrase

	/**
	 * Return the reason phrase.
	 *
	 * @return the reason phrase.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default ReasonPhrase reasonPhrase() {
		return this.response().getStatusLine().getReasonPhrase();
	}

	/**
	 * Set the reason phrase to the given {@code reasonPhrase}.
	 *
	 * @param reasonPhrase the reason phrase to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its reason-phrase.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self reasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		this.response().getStatusLine().setReasonPhrase(reasonPhrase);
		return (Self) this;
	}

	/**
	 * Replace the reason phrase to the result of invoking the given {@code operator} with
	 * the argument being the previous reason phrase.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its reason-phrase and the given {@code
	 *                                       operator} returned another reason-phrase.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self reasonPhrase(@NotNull UnaryOperator<@NotNull ReasonPhrase> operator) {
		Objects.requireNonNull(operator, "operator");
		ReasonPhrase rp = this.reasonPhrase();
		ReasonPhrase reasonPhrase = operator.apply(rp);

		if (reasonPhrase != rp)
			this.reasonPhrase(reasonPhrase);

		return (Self) this;
	}

	// StatusCode

	/**
	 * Return the status code.
	 *
	 * @return the status code.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default StatusCode statusCode() {
		return this.response().getStatusLine().getStatusCode();
	}

	/**
	 * Set the status code to the given {@code statusCode}.
	 *
	 * @param statusCode the status code to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its status-code.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self statusCode(@NotNull StatusCode statusCode) {
		this.response().getStatusLine().setStatusCode(statusCode);
		return (Self) this;
	}

	/**
	 * Replace the status code to the result of invoking the given {@code operator} with
	 * the argument being the previous status code.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null; if the
	 *                                       given {@code operator} returned null.
	 * @throws UnsupportedOperationException if the status-line does not support changing
	 *                                       its status-code and the given {@code
	 *                                       operator} returned another status-code.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self statusCode(@NotNull UnaryOperator<@NotNull StatusCode> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusCode sc = this.statusCode();
		StatusCode statusCode = operator.apply(sc);

		if (statusCode != sc)
			this.statusCode(statusCode);

		return (Self) this;
	}

	// StatusLine

	/**
	 * Return the status line.
	 *
	 * @return the status line.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default StatusLine statusLine() {
		return this.response().getStatusLine();
	}

	/**
	 * Set the status line to the given {@code statusLine}.
	 *
	 * @param statusLine the status line to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusLine} is null.
	 * @throws UnsupportedOperationException if the response does not support changing its
	 *                                       status-line.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self statusLine(@NotNull StatusLine statusLine) {
		this.response().setStatusLine(statusLine);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current status line as the parameter.
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
	default Self statusLine(@NotNull Consumer<@NotNull StatusLine> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.statusLine());
		return (Self) this;
	}
}