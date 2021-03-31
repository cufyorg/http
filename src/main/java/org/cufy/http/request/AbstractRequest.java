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
import org.cufy.http.syntax.ABNFPattern;
import org.cufy.http.syntax.HTTPParse;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Request}.
 *
 * @param <B> the type of the body of this request.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class AbstractRequest<B extends Body> implements Request<B> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The request body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected B body;
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
		this.requestLine = RequestLine.defaultRequestLine();
		this.headers = Headers.defaultHeaders();
		//noinspection unchecked
		this.body = (B) Body.defaultBody();
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
	public AbstractRequest(@NotNull Request<?> request) {
		Objects.requireNonNull(request, "request");
		this.requestLine = RequestLine.copy(request.requestLine());
		this.headers = Headers.copy(request.headers());
		//noinspection unchecked
		this.body = (B) Body.copy(request.body());
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
		this.requestLine = RequestLine.copy(requestLine);
		this.headers = Headers.copy(headers);
		//noinspection unchecked
		this.body = (B) Body.copy(body);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new request from the given {@code source}.
	 *
	 * @param source the source for the constructed request.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#REQUEST}.
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractRequest(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.REQUEST.matcher(source).matches())
			throw new IllegalArgumentException("invalid request: " + source);

		Matcher matcher = HTTPParse.REQUEST.matcher(source);

		if (matcher.find()) {
			String requestLine = matcher.group("RequestLine");
			String headers = matcher.group("Headers");
			String body = matcher.group("Body");

			this.requestLine = RequestLine.parse(requestLine);
			this.headers = headers == null || headers.isEmpty() ?
						   Headers.defaultHeaders() :
						   Headers.parse(headers);
			//noinspection unchecked
			this.body = body == null || body.isEmpty() ?
						(B) Body.defaultBody() :
						(B) Body.parse(body);
		} else {
			this.requestLine = RequestLine.defaultRequestLine();
			this.headers = Headers.defaultHeaders();
			//noinspection unchecked
			this.body = (B) Body.defaultBody();
		}
	}

	@NotNull
	@Override
	public <BB extends Body> Request<BB> body(@NotNull BB body, int... ignored) {
		Objects.requireNonNull(body, "body");
		//noinspection unchecked
		this.body = (B) body;
		//noinspection unchecked
		return (Request<BB>) this;
	}

	@NotNull
	@Override
	public B body() {
		return this.body;
	}

	@NotNull
	@Override
	public AbstractRequest<B> clone() {
		try {
			//noinspection unchecked
			AbstractRequest<B> clone = (AbstractRequest) super.clone();
			clone.requestLine = this.requestLine.clone();
			clone.headers = this.headers.clone();
			//noinspection unchecked
			clone.body = (B) this.body.clone();
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
			return Objects.equals(this.requestLine, request.requestLine()) &&
				   Objects.equals(this.headers, request.headers()) &&
				   Objects.equals(this.body, request.body());
		}

		return false;
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
	public Request<B> headers(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		return this;
	}

	@NotNull
	@Override
	public Headers headers() {
		return this.headers;
	}

	@NotNull
	@Override
	public Request<B> requestLine(@NotNull RequestLine requestLine) {
		Objects.requireNonNull(requestLine, "requestLine");
		this.requestLine = requestLine;
		return this;
	}

	@NotNull
	@Override
	public RequestLine requestLine() {
		return this.requestLine;
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.REQUEST)
	@Override
	public String toString() {
		String requestLine = this.requestLine.toString();
		String headers = this.headers.toString();
		String body = this.body.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(requestLine);

		if (!ABNFPattern.CRLF.matcher(requestLine).find())
			//if any, it will be at the end
			builder.append("\r\n");

		builder.append(headers);

		if (!body.isEmpty())
			builder.append("\r\n")
					.append(body);

		return builder.toString();
	}
}
