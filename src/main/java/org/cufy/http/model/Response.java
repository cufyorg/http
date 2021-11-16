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
package org.cufy.http.model;

import org.cufy.http.raw.RawResponse;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * <b>Components</b>
 * <br>
 * A structure holding the variables of a response from the server.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link StatusLine}</li>
 *     <li>{@link Headers}</li>
 *     <li>{@link Body}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Response extends Cloneable, Serializable {
	/**
	 * An empty response constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Response EMPTY = new RawResponse();

	/**
	 * Replace the body of this to be the result of invoking the given {@code operator}
	 * with the argument being the current body.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response body(@NotNull UnaryOperator<Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.getBody();
		Body body = operator.apply(b);

		if (body != b)
			this.setBody(body);

		return this;
	}

	/**
	 * Return the http-version of this status-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default HttpVersion getHttpVersion() {
		return this.getStatusLine().getHttpVersion();
	}

	/**
	 * Return the reason-phrase of this status-line.
	 *
	 * @return the phrase of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default ReasonPhrase getReasonPhrase() {
		return this.getStatusLine().getReasonPhrase();
	}

	/**
	 * Return the status-code defined for this.
	 *
	 * @return the status-code of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default StatusCode getStatusCode() {
		return this.getStatusLine().getStatusCode();
	}

	/**
	 * Replace the headers of this to be the result of invoking the given {@code operator}
	 * with the argument being the current headers. If the {@code operator} returned
	 * {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers and the given {@code operator}
	 *                                       returned another headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.getHeaders();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.setHeaders(headers);

		return this;
	}

	/**
	 * Replace the http-version of this to be the result of invoking the given {@code
	 * operator} with the argument being the current http-version. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its http-version and the given
	 *                                       {@code operator} returned another
	 *                                       http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response httpVersion(@NotNull UnaryOperator<HttpVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.getStatusLine();
		HttpVersion hv = s.getHttpVersion();
		HttpVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			s.setHttpVersion(httpVersion);

		return this;
	}

	/**
	 * Replace the phrase of this to be the result of invoking the given {@code operator}
	 * with the argument being the current phrase. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its method and the given {@code
	 *                                       operator} returned another phrase.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response reasonPhrase(@NotNull UnaryOperator<ReasonPhrase> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.getStatusLine();
		ReasonPhrase m = s.getReasonPhrase();
		ReasonPhrase method = operator.apply(m);

		if (method != null && method != m)
			s.setReasonPhrase(method);

		return this;
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setBody(@Nullable Body body) {
		throw new UnsupportedOperationException("body");
	}

	/**
	 * Set the headers of this from the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setHeaders(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
	}

	/**
	 * Set the http-version of this status-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this status-line.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setHttpVersion(@NotNull HttpVersion httpVersion) {
		this.getStatusLine().setHttpVersion(httpVersion);
		return this;
	}

	/**
	 * Set the phrase of this to the given {@code phrase}.
	 *
	 * @param reasonPhrase the phrase to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its phrase.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setReasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		this.getStatusLine().setReasonPhrase(reasonPhrase);
		return this;
	}

	/**
	 * Set the status-code of this to be the given {@code statusCode}.
	 *
	 * @param statusCode the new status-code of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its status-code.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setStatusCode(@NotNull StatusCode statusCode) {
		this.getStatusLine().setStatusCode(statusCode);
		return this;
	}

	/**
	 * Set the statusLine of this from the given {@code statusLine}.
	 *
	 * @param statusLine the statusLine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusLine} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response setStatusLine(@NotNull StatusLine statusLine) {
		throw new UnsupportedOperationException("statusLine");
	}

	/**
	 * Replace the port of this to be the result of invoking the given {@code operator}
	 * with the argument being the current port. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its port and the given {@code
	 *                                       operator} returned another port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusCode(@NotNull UnaryOperator<StatusCode> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.getStatusLine();
		StatusCode sc = s.getStatusCode();
		StatusCode statusCode = operator.apply(sc);

		if (statusCode != null && statusCode != sc)
			s.setStatusCode(statusCode);

		return this;
	}

	/**
	 * Replace the status-line of this to be the result of invoking the given {@code
	 * operator} with the argument being the current status-line. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status and the {@code operator} returned
	 *                                       another status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusLine(@NotNull UnaryOperator<StatusLine> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine sl = this.getStatusLine();
		StatusLine statusLine = operator.apply(sl);

		if (statusLine != null && statusLine != sl)
			this.setStatusLine(statusLine);

		return this;
	}

	/**
	 * Capture this response into a new object.
	 *
	 * @return a clone of this response.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Response clone();

	/**
	 * Two responses are equal when they are the same instance or have an equal {@link
	 * #getStatusLine()}, {@link #getHeaders()} and {@link #getBody()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a response and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a response is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this response.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this response. Invoke to get the text representing this
	 * in a response.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Status-Line
	 *     Headers
	 *     <br>
	 *     Body
	 * </pre>
	 * Example:
	 * <pre>
	 *     HTTP/1.1 200 OK
	 *     Content-Type: application/json<br>
	 *     {name:"alex"}<br>
	 *     name=alex&age=22
	 * </pre>
	 *
	 * @return a string representation of this Response.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(HttpRegExp.RESPONSE)
	@Override
	String toString();

	/**
	 * Get the body of this response.
	 *
	 * @return the body of this response.
	 * @since 0.0.1 ~2021.03.22
	 */
	@Nullable
	@Contract(pure = true)
	Body getBody();

	/**
	 * Get the headers of this response.
	 *
	 * @return the headers of this response.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers getHeaders();

	/**
	 * Get the status-line of this response.
	 *
	 * @return the status line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	StatusLine getStatusLine();
}
