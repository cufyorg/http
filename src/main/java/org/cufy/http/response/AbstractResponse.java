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
package org.cufy.http.response;

import org.cufy.http.body.Body;
import org.cufy.http.request.Headers;
import org.cufy.http.syntax.ABNFPattern;
import org.cufy.http.syntax.HTTPParse;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Response}.
 *
 * @param <B> the type of the body of this.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class AbstractResponse<B extends Body> implements Response<B> {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The response body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@SuppressWarnings("unchecked")
	@NotNull
	protected B body = (B) Body.defaultBody();
	/**
	 * The response headers.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Headers headers = Headers.defaultHeaders();
	/**
	 * The status line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected StatusLine statusLine = StatusLine.defaultStatusLine();

	/**
	 * Construct a new empty response.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractResponse() {

	}

	/**
	 * Construct a new response from the given {@code source}.
	 *
	 * @param source the source for the constructed response.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#RESPONSE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractResponse(@NotNull @NonNls @Pattern(HTTPRegExp.RESPONSE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.RESPONSE.matcher(source).matches())
			throw new IllegalArgumentException("invalid response: " + source);

		Matcher matcher = HTTPParse.RESPONSE.matcher(source);

		if (matcher.find()) {
			@Subst("HTTP/1.1 200 OK") String statusLine = matcher.group("StatusLine");
			@Subst("X:Y\r\nZ:B\r\n") String headers = matcher.group("Headers");
			String body = matcher.group("Body");

			this.statusLine = StatusLine.parse(statusLine);
			if (headers != null && !headers.isEmpty())
				this.headers = Headers.parse(headers);
			if (body != null && !body.isEmpty())
				//noinspection unchecked
				this.body = (B) Body.from(body);
		}
	}

	@NotNull
	@Override
	public B body() {
		return this.body;
	}

	@NotNull
	@Override
	public <BB extends Body> Response<BB> body(@NotNull BB body) {
		Objects.requireNonNull(body, "body");
		//noinspection unchecked
		this.body = (B) body;
		//noinspection unchecked
		return (Response<BB>) this;
	}

	@NotNull
	@Override
	public AbstractResponse<B> clone() {
		try {
			//noinspection unchecked
			AbstractResponse<B> clone = (AbstractResponse) super.clone();
			clone.statusLine = this.statusLine.clone();
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
		if (object instanceof Response) {
			Response response = (Response) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.statusLine, response.statusLine()) &&
				   Objects.equals(this.headers, response.headers()) &&
				   Objects.equals(this.body, response.body());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.statusLine.hashCode() ^
			   this.headers.hashCode() ^
			   this.body.hashCode();
	}

	@NotNull
	@Override
	public Headers headers() {
		return this.headers;
	}

	@NotNull
	@Override
	public Response<B> headers(@NotNull Headers headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers = headers;
		return this;
	}

	@NotNull
	@Override
	public StatusLine statusLine() {
		return this.statusLine;
	}

	@NotNull
	@Override
	public Response<B> statusLine(@NotNull StatusLine statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.statusLine = statusLine;
		return this;
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.RESPONSE)
	@Override
	public String toString() {
		String requestLine = this.statusLine.toString();
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

		@Subst("HTTP/1.1 200 OK\r\n") String s = builder.toString();
		return s;
	}
}
