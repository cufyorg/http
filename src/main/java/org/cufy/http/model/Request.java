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
 * A structure holding the variables of a request to the server.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link RequestLine}</li>
 *     <li>{@link Headers}</li>
 *     <li>{@link Body}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Request extends Cloneable, Serializable {
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
	default Request body(@NotNull UnaryOperator<Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.getBody();
		Body body = operator.apply(b);

		if (body != b)
			this.setBody(body);

		return this;
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
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers and the given {@code operator}
	 *                                       returned another headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.getHeaders();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.setHeaders(headers);

		return this;
	}

	/**
	 * Replace the request-line of this to be the result of invoking the given {@code
	 * operator} with the argument being the current request-line. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line and the {@code operator}
	 *                                       returned another request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request requestLine(@NotNull UnaryOperator<RequestLine> operator) {
		Objects.requireNonNull(operator, "operator");
		RequestLine rl = this.getRequestLine();
		RequestLine requestLine = operator.apply(rl);

		if (requestLine != null && requestLine != rl)
			this.setRequestLine(requestLine);

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
	default Request setBody(@Nullable Body body) {
		throw new UnsupportedOperationException("body");
	}

	/**
	 * Set the headers of this from the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request setHeaders(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
	}

	/**
	 * Set the requestLine of this from the given {@code requestLine}.
	 *
	 * @param requestLine the requestLine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request setRequestLine(@NotNull RequestLine requestLine) {
		throw new UnsupportedOperationException("requestLine");
	}

	/**
	 * Capture this request into a new object.
	 *
	 * @return a clone of this request.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Request clone();

	/**
	 * Two requests are equal when they are the same instance or have an equal {@link
	 * #getRequestLine()}, {@link #getHeaders()} and {@link #getBody()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a request and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a request is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this request.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this request. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Request-Line
	 *     Headers
	 *     <br>
	 *     Body
	 * </pre>
	 * Example:
	 * <pre>
	 *     POST github.com HTTP/1.1
	 *     Content-Type: application/json<br>
	 *     {name:"alex"}<br>
	 *     name=alex&age=22
	 * </pre>
	 *
	 * @return a string representation of this Request.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(HttpRegExp.REQUEST)
	@Override
	String toString();

	/**
	 * Get the body of this request.
	 *
	 * @return the body of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@Nullable
	@Contract(pure = true)
	Body getBody();

	/**
	 * Get the headers of this request.
	 *
	 * @return the headers of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers getHeaders();

	/**
	 * Get the request-line of this request.
	 *
	 * @return the request line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	RequestLine getRequestLine();
}
