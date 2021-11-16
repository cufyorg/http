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
package org.cufy.http.impl;

import org.cufy.http.model.Body;
import org.cufy.http.model.Headers;
import org.cufy.http.model.Request;
import org.cufy.http.model.RequestLine;
import org.cufy.http.syntax.AbnfPattern;
import org.cufy.http.syntax.HttpParse;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Request}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class RequestImpl implements Request {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The request body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@Nullable
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
	public RequestImpl() {
		this.requestLine = new RequestLineImpl();
		this.headers = new HeadersImpl();
		this.body = null;
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
	public RequestImpl(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.requestLine = RequestLineImpl.requestLine(request.getRequestLine());
		this.headers = HeadersImpl.headers(request.getHeaders());

		Body body = request.getBody();
		this.body = body == null ? null : BodyImpl.body(body);
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
	public RequestImpl(@NotNull @Pattern(HttpRegExp.REQUEST) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.REQUEST.matcher(source).matches())
			throw new IllegalArgumentException("invalid request: " + source);

		Matcher matcher = HttpParse.REQUEST.matcher(source);

		if (matcher.find()) {
			String requestLine = matcher.group("RequestLine");
			String headers = matcher.group("Headers");
			String body = matcher.group("Body");

			this.requestLine = RequestLineImpl.requestLine(requestLine);
			this.headers = headers == null || headers.isEmpty() ?
						   new HeadersImpl() :
						   HeadersImpl.headers(headers);
			this.body = body == null || body.isEmpty() ?
						null :
						BodyImpl.body(body.getBytes(), this.headers.get(Headers.CONTENT_TYPE));
		} else {
			this.requestLine = new RequestLineImpl();
			this.headers = new HeadersImpl();
			this.body = null;
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
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers}.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RequestImpl(@NotNull RequestLine requestLine, @NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(requestLine, "requestLine");
		Objects.requireNonNull(headers, "headers");
		this.requestLine = RequestLineImpl.requestLine(requestLine);
		this.headers = HeadersImpl.headers(headers);
		this.body = body == null ? null : BodyImpl.body(body);
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new request from copying the given {@code request}.
	 *
	 * @param request the request to copy.
	 * @return a new copy of the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Request request(@NotNull Request request) {
		return new RequestImpl(request);
	}

	/**
	 * <b>Parse</b>
	 * <br>
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
	public static Request request(@NotNull @Pattern(HttpRegExp.REQUEST) String source) {
		return new RequestImpl(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new request from the given components.
	 *
	 * @param requestLine the request-line of the constructed request.
	 * @param headers     the headers of the constructed request.
	 * @param body        the body of the constructed request.
	 * @return a new request from the given components.
	 * @throws NullPointerException if the given {@code requestLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static Request request(@NotNull RequestLine requestLine, @NotNull Headers headers, @Nullable Body body) {
		return new RequestImpl(requestLine, headers, body);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new request with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new request.
	 * @return the request constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Request request(@NotNull Consumer<Request> builder) {
		Objects.requireNonNull(builder, "builder");
		Request request = new RequestImpl();
		builder.accept(request);
		return request;
	}

	@NotNull
	@Override
	public RequestImpl clone() {
		try {
			RequestImpl clone = (RequestImpl) super.clone();
			clone.requestLine = this.requestLine.clone();
			clone.headers = this.headers.clone();
			clone.body = this.body == null ? null : this.body.clone();
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

	@Nullable
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
		return this.requestLine.hashCode() ^
			   this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

	@NotNull
	@Override
	public Request setBody(@Nullable Body body) {
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
}
