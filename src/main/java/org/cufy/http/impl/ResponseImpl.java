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
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

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
	@ApiStatus.Internal
	public ResponseImpl(@NotNull StatusLine statusLine, @NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(statusLine, "statusLine");
		Objects.requireNonNull(headers, "headers");
		this.statusLine = statusLine;
		this.headers = headers;
		this.body = body;
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
