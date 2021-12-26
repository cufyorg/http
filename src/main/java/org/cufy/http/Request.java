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
package org.cufy.http;

import org.cufy.http.body.BytesBody;
import org.cufy.internal.syntax.AbnfPattern;
import org.cufy.internal.syntax.HttpParse;
import org.cufy.internal.syntax.HttpPattern;
import org.cufy.internal.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

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
public class Request extends Message {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The request line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected RequestLine requestLine;

	/**
	 * Construct a new request.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Request() {
		this.requestLine = new RequestLine();
	}

	/**
	 * Construct a new request from the given components.
	 *
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public Request(
			@NotNull RequestLine requestLine,
			@NotNull Headers headers,
			@Nullable Body body
	) {
		super(headers, body);
		this.requestLine =
				Objects.requireNonNull(requestLine, "requestLine");
	}

	/**
	 * Construct a new request with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new request.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Request(@NotNull Consumer<@NotNull Request> builder) {
		Objects.requireNonNull(builder, "builder");
		this.requestLine = new RequestLine();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @return a new request from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Request parse(@NotNull @Pattern(HttpRegExp.REQUEST) String source) {
		Objects.requireNonNull(source, "source");

		if (!HttpPattern.REQUEST.matcher(source).matches())
			throw new IllegalArgumentException("invalid request: " + source);

		Matcher matcher = HttpParse.REQUEST.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid request " + source);

		String requestLineSrc = matcher.group("RequestLine");
		String headersSrc = matcher.group("Headers");
		String bodySrc = matcher.group("Body");

		RequestLine requestLine =
				requestLineSrc == null || requestLineSrc.isEmpty() ?
				new RequestLine() : RequestLine.parse(requestLineSrc);
		Headers headers =
				headersSrc == null || headersSrc.isEmpty() ?
				new Headers() : Headers.parse(headersSrc);
		Body body =
				bodySrc == null || bodySrc.isEmpty() ?
				null :
				new BytesBody(headers.get(Headers.CONTENT_TYPE), bodySrc.getBytes());

		return new Request(
				requestLine,
				headers,
				body
		);
	}

	/**
	 * Capture this request into a new object.
	 *
	 * @return a clone of this request.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Request clone() {
		Request clone = (Request) super.clone();
		clone.requestLine = this.requestLine.clone();
		return clone;
	}

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
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Request) {
			Request request = (Request) object;

			return Objects.equals(this.requestLine, request.getRequestLine()) &&
				   Objects.equals(this.headers, request.getHeaders()) &&
				   Objects.equals(this.body, request.getBody());
		}

		return false;
	}

	/**
	 * The hash code of a request is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this request.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.requestLine.hashCode() ^
			   this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

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
	public String toString() {
		String requestLine = this.requestLine.toString();
		String headers = this.headers.toString();
		String body = this.body == null ? null : this.body.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(requestLine);

		if (!AbnfPattern.CRLF.matcher(requestLine).find())
			//if any, it will be at the end
			builder.append("\r\n");

		builder.append(headers);

		if (body != null)
			builder.append("\r\n")
				   .append(body);

		return builder.toString();
	}

	/**
	 * Get the request-line of this request.
	 *
	 * @return the request line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	public RequestLine getRequestLine() {
		return this.requestLine;
	}

	/**
	 * Set the requestLine of this from the given {@code requestLine}.
	 *
	 * @param requestLine the requestLine to be set.
	 * @throws NullPointerException          if the given {@code requestLine} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       request-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setRequestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine = requestLine;
	}
}
