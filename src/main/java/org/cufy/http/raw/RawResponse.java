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
package org.cufy.http.raw;

import org.cufy.http.impl.BodyImpl;
import org.cufy.http.model.Body;
import org.cufy.http.model.Response;
import org.cufy.http.model.StatusLine;
import org.cufy.http.model.Headers;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link Response}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawResponse implements Response {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The body to be returned by {@link #getBody()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@Nullable
	@UnmodifiableView
	protected final Body body;
	/**
	 * The headers to be returned by {@link #getHeaders()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Headers headers;
	/**
	 * The statusLine to be returned by {@link #getStatusLine()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final StatusLine statusLine;
	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw response.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawResponse() {
		this.value = "";
		this.statusLine = StatusLine.EMPTY;
		this.headers = Headers.EMPTY;
		this.body = null;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code response}.
	 *
	 * @param response the response to be copied.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawResponse(@NotNull Response response) {
		Objects.requireNonNull(response, "response");
		this.value = response.toString();
		this.statusLine = new RawStatusLine(response.getStatusLine());
		this.headers = new RawHeaders(response.getHeaders());

		Body body = response.getBody();
		this.body = body == null ? null : BodyImpl.body(body);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw response with the given {@code value}.
	 *
	 * @param value the value of the constructed response.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawResponse(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.statusLine = StatusLine.EMPTY;
		this.headers = Headers.EMPTY;
		this.body = null;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw response with the given {@code value}.
	 *
	 * @param value      the value of the constructed response.
	 * @param statusLine the status-line of the constructed response.
	 * @param headers    the headers of the constructed response.
	 * @param body       the body of the constructed response.
	 * @throws NullPointerException if the given {@code value} or {@code statusLine} or
	 *                              {@code headers} or {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawResponse(@NotNull String value, @NotNull StatusLine statusLine, @NotNull Headers headers, @Nullable Body body) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(statusLine, "statusLine");
		Objects.requireNonNull(headers, "headers");
		this.value = value;
		this.statusLine = new RawStatusLine(statusLine);
		this.headers = new RawHeaders(headers);
		this.body = body == null ? null : BodyImpl.body(body);
	}

	@NotNull
	@Override
	public RawResponse clone() {
		try {
			return (RawResponse) super.clone();
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
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}
}
