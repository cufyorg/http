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
package org.cufy.http.request;

import org.cufy.http.component.Body;
import org.cufy.http.component.Headers;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A structure holding the variables of a request to the server.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Request extends Cloneable, Serializable {
	/**
	 * Return a new request instance to be a placeholder if a the user has not specified a
	 * request.
	 *
	 * @return a new empty request.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Request defaultRequest() {
		return new AbstractRequest();
	}

	/**
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @return a new request from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Request parse(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) @Subst("GET / HTTP/1.1\n") String source) {
		return new AbstractRequest(source);
	}

	/**
	 * Replace the body of this to be the result of invoking the given {@code operator}
	 * with the argument being the current body. If the {@code operator} returned {@code
	 * null} then nothing happens.
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
		Body b = this.body();
		Body body = operator.apply(b);

		if (body != null && body != b)
			this.body(body);

		return this;
	}

	/**
	 * Set the body of this from the given {@code body} literal.
	 *
	 * @param body the body literal to set the body of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request body(@NotNull @NonNls String body) {
		return this.body(Body.parse(body));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Request body(@NotNull @NonNls String content, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) @Subst("q=0&v=1") String parameters) {
		return this.body(Body.parse(content, parameters));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body content to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Request body(@NotNull @NonNls String content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		this.body(Body.parse(content, parameters));
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
	default Request body(@NotNull Body body) {
		throw new UnsupportedOperationException("body");
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
		Headers h = this.headers();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.headers(headers);

		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers} literal.
	 *
	 * @param headers the headers literal to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if the given {@code headers} does not match
	 *                                       {@link HTTPRegExp#HEADERS}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request headers(@Nullable @NonNls @Pattern(HTTPRegExp.HEADERS) @Subst("X:Y\r\nZ:A\r\n") String headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers} array.
	 *
	 * @param headers the headers array to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code headers}
	 *                                       does not match {@link HTTPRegExp#HEADER}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request headers(@NotNull @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
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
	default Request headers(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
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
		RequestLine rl = this.requestLine();
		RequestLine requestLine = operator.apply(rl);

		if (requestLine != null && requestLine != rl)
			this.requestLine(requestLine);

		return this;
	}

	/**
	 * Set the requestLine of this from the given {@code requestLine} literal.
	 *
	 * @param requestLine the requestLine literal to set the requestLine of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws IllegalArgumentException      if the given {@code requestLine} does not
	 *                                       match {@link HTTPRegExp#REQUEST_LINE}.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Request requestLine(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST_LINE) @Subst("GET / HTTP/1.1") String requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine(RequestLine.parse(requestLine));
		return this;
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
	default Request requestLine(@NotNull RequestLine requestLine) {
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
	 * #requestLine()}, {@link #headers()} and {@link #body()}.
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
	@NonNls
	@Contract(pure = true)
	@Pattern(HTTPRegExp.REQUEST)
	@Override
	String toString();

	/**
	 * Get the body of this request.
	 *
	 * @return the body of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	Body body();

	/**
	 * Get the headers of this request.
	 *
	 * @return the headers of this request.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers headers();

	/**
	 * Get the request-line of this request.
	 *
	 * @return the request line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	RequestLine requestLine();
}
