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
import org.cufy.http.model.Response;
import org.cufy.http.model.StatusLine;
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
 * A basic implementation of the interface {@link Response}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class ResponseImpl implements Response {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The response body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@Nullable
	protected Body body;
	/**
	 * The response headers.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Headers headers;
	/**
	 * The status line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected StatusLine statusLine;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default response.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public ResponseImpl() {
		this.statusLine = new StatusLineImpl();
		this.headers = new HeadersImpl();
		this.body = null;
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new response from copying the given {@code response}.
	 *
	 * @param response the response to copy.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public ResponseImpl(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.statusLine = StatusLineImpl.statusLine(response.getStatusLine());
		this.headers = HeadersImpl.headers(response.getHeaders());

		Body body = response.getBody();
		this.body = body == null ? null : BodyImpl.body(body);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new response from the given {@code source}.
	 *
	 * @param source the source for the constructed response.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#RESPONSE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	public ResponseImpl(@NotNull @Pattern(HttpRegExp.RESPONSE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.RESPONSE.matcher(source).matches())
			throw new IllegalArgumentException("invalid response: " + source);

		Matcher matcher = HttpParse.RESPONSE.matcher(source);

		if (matcher.find()) {
			String statusLine = matcher.group("StatusLine");
			String headers = matcher.group("Headers");
			String body = matcher.group("Body");

			this.statusLine = StatusLineImpl.statusLine(statusLine);
			this.headers = headers == null || headers.isEmpty() ?
						   new HeadersImpl() :
						   HeadersImpl.headers(headers);
			this.body = body == null || body.isEmpty() ?
						null :
						BodyImpl.body(body.getBytes(), this.headers.get(Headers.CONTENT_TYPE));
		} else {
			this.statusLine = new StatusLineImpl();
			this.headers = new HeadersImpl();
			this.body = null;
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new response from the given components.
	 *
	 * @param statusLine the status-line of the constructed response.
	 * @param headers    the headers of the constructed response.
	 * @param body       the body of the constructed response.
	 * @throws NullPointerException if the given {@code statusLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public ResponseImpl(@NotNull StatusLine statusLine, @NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(statusLine, "statusLine");
		Objects.requireNonNull(headers, "headers");
		this.statusLine = StatusLineImpl.statusLine(statusLine);
		this.headers = HeadersImpl.headers(headers);
		this.body = body == null ? null : BodyImpl.body(body);
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new response from copying the given {@code response}.
	 *
	 * @param response the response to copy.
	 * @return a new copy of the given {@code response}.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ResponseImpl response(@NotNull Response response) {
		return new ResponseImpl(response);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new response from the given {@code source}.
	 *
	 * @param source the source for the constructed response.
	 * @return a new response from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#RESPONSE}.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ResponseImpl response(@NotNull @Pattern(HttpRegExp.RESPONSE) String source) {
		return new ResponseImpl(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new response from the given components.
	 *
	 * @param statusLine the status-line of the constructed response.
	 * @param headers    the headers of the constructed response.
	 * @param body       the body of the constructed response.
	 * @return a new response from the given components.
	 * @throws NullPointerException if the given {@code statusLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static ResponseImpl response(@NotNull StatusLine statusLine, @NotNull Headers headers, @Nullable Body body) {
		return new ResponseImpl(statusLine, headers, body);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new response with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new response.
	 * @return the response constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ResponseImpl response(@NotNull Consumer<Response> builder) {
		Objects.requireNonNull(builder, "builder");
		ResponseImpl response = new ResponseImpl();
		builder.accept(response);
		return response;
	}

	@NotNull
	@Override
	public ResponseImpl clone() {
		try {
			ResponseImpl clone = (ResponseImpl) super.clone();
			clone.statusLine = this.statusLine.clone();
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
		if (object instanceof Response) {
			Response response = (Response) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.statusLine, response.getStatusLine()) &&
				   Objects.equals(this.headers, response.getHeaders()) &&
				   Objects.equals(this.body, response.getBody());
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
	public StatusLine getStatusLine() {
		return this.statusLine;
	}

	@Override
	public int hashCode() {
		return this.statusLine.hashCode() ^
			   this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

	@NotNull
	@Override
	public Response setBody(@Nullable Body body) {
		this.body = body;
		return this;
	}

	@NotNull
	@Override
	public Response setHeaders(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		return this;
	}

	@NotNull
	@Override
	public Response setStatusLine(@NotNull StatusLine statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.statusLine = statusLine;
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.RESPONSE)
	@Override
	public String toString() {
		String requestLine = this.statusLine.toString();
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
