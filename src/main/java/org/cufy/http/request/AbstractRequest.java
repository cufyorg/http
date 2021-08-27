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

import org.cufy.http.body.Body;
import org.cufy.http.syntax.AbnfPattern;
import org.cufy.http.syntax.HttpParse;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Request}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class AbstractRequest implements Request {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The request body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Body body;
	/**
	 * The request headers.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Headers headers;
	/**
	 * The request line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected RequestLine requestLine;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default request.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequest() {
		this.requestLine = RequestLine.requestLine();
		this.headers = Headers.headers();
		this.body = Body.body();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new request from copying the given {@code request}.
	 *
	 * @param request the request to copy.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequest(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.requestLine = RequestLine.requestLine(request.getRequestLine());
		this.headers = Headers.headers(request.getHeaders());
		this.body = Body.body(request.getBody());
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractRequest(@NotNull @Pattern(HttpRegExp.REQUEST) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.REQUEST.matcher(source).matches())
			throw new IllegalArgumentException("invalid request: " + source);

		Matcher matcher = HttpParse.REQUEST.matcher(source);

		if (matcher.find()) {
			String requestLine = matcher.group("RequestLine");
			String headers = matcher.group("Headers");
			String body = matcher.group("Body");

			this.requestLine = RequestLine.requestLine(requestLine);
			this.headers = headers == null || headers.isEmpty() ?
						   Headers.headers() :
						   Headers.headers(headers);
			this.body = body == null || body.isEmpty() ?
						Body.body() :
						Body.body(body);
		} else {
			this.requestLine = RequestLine.requestLine();
			this.headers = Headers.headers();
			this.body = Body.body();
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new request from the given components.
	 *
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractRequest(@NotNull RequestLine requestLine, @NotNull Headers headers, @NotNull Body body) {
		Objects.requireNonNull(requestLine, "requestLine");
		Objects.requireNonNull(headers, "headers");
		Objects.requireNonNull(body, "body");
		this.requestLine = RequestLine.requestLine(requestLine);
		this.headers = Headers.headers(headers);
		this.body = Body.body(body);
	}

	@NotNull
	@Override
	public AbstractRequest clone() {
		try {
			AbstractRequest clone = (AbstractRequest) super.clone();
			clone.requestLine = this.requestLine.clone();
			clone.headers = this.headers.clone();
			clone.body = this.body.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Request) {
			Request request = (Request) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.requestLine, request.getRequestLine()) &&
				   Objects.equals(this.headers, request.getHeaders()) &&
				   Objects.equals(this.body, request.getBody());
		}

		return false;
	}

	@NotNull
	@Override
	public Body getBody() {
		return this.body;
	}

	@NotNull
	@Override
	public Headers getHeaders() {
		return this.headers;
	}

	@NotNull
	@Override
	public RequestLine getRequestLine() {
		return this.requestLine;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.requestLine.hashCode() ^
			   this.headers.hashCode() ^
			   this.body.hashCode();
	}

	@NotNull
	@Override
	public Request setBody(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		this.body = body;
		return this;
	}

	@NotNull
	@Override
	public Request setHeaders(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		return this;
	}

	@NotNull
	@Override
	public Request setRequestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine = requestLine;
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.REQUEST)
	@Override
	public String toString() {
		String requestLine = this.requestLine.toString();
		String headers = this.headers.toString();
		String body = this.body.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(requestLine);

		if (!AbnfPattern.CRLF.matcher(requestLine).find())
			//if any, it will be at the end
			builder.append("\r\n");

		builder.append(headers);

		if (!body.isEmpty())
			builder.append("\r\n")
				   .append(body);

		return builder.toString();
	}
}
