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

import org.cufy.http.model.ReasonPhrase;
import org.cufy.http.model.StatusCode;
import org.cufy.http.model.StatusLine;
import org.cufy.http.model.HttpVersion;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A raw implementation of the interface {@link StatusLine}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawStatusLine implements StatusLine {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5788252143775840682L;

	/**
	 * The http-version to be returned by {@link #getHttpVersion()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final HttpVersion httpVersion;
	/**
	 * The reason-phrase to be returned by {@link #getReasonPhrase()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final ReasonPhrase reasonPhrase;
	/**
	 * The status-code to be returned by {@link #getStatusCode()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final StatusCode statusCode;
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
	 * Construct a new empty raw status-line.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawStatusLine() {
		this.value = "";
		this.httpVersion = HttpVersion.EMPTY;
		this.statusCode = StatusCode.EMPTY;
		this.reasonPhrase = ReasonPhrase.EMPTY;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code statusLine}.
	 *
	 * @param statusLine the status-line to be copied.
	 * @throws NullPointerException if the given {@code statusLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawStatusLine(@NotNull StatusLine statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.value = statusLine.toString();
		this.httpVersion = statusLine.getHttpVersion();
		this.statusCode = statusLine.getStatusCode();
		this.reasonPhrase = statusLine.getReasonPhrase();
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw status-line with the given {@code value}.
	 *
	 * @param value the value of the constructed status-line.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawStatusLine(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.httpVersion = HttpVersion.EMPTY;
		this.statusCode = StatusCode.EMPTY;
		this.reasonPhrase = ReasonPhrase.EMPTY;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw status-line with the given {@code value}.
	 *
	 * @param value        the value of the constructed status-line.
	 * @param httpVersion  the http-version of the constructed status-line.
	 * @param statusCode   the status-code of the constructed status-line.
	 * @param reasonPhrase the reason-phrase of the constructed status-line.
	 * @throws NullPointerException if the given {@code value} or {@code httpVersion} or
	 *                              {@code statusCode} or {@code reasonPhrase} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawStatusLine(@NotNull String value, @NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		Objects.requireNonNull(statusCode, "statusCode");
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.value = value;
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	@NotNull
	@Override
	public RawStatusLine clone() {
		try {
			return (RawStatusLine) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof StatusLine) {
			StatusLine statusLine = (StatusLine) object;

			return Objects.equals(this.httpVersion, statusLine.getHttpVersion()) &&
				   Objects.equals(this.statusCode, statusLine.getStatusCode()) &&
				   Objects.equals(this.reasonPhrase, statusLine.getReasonPhrase());
		}

		return false;
	}

	@NotNull
	@Override
	public HttpVersion getHttpVersion() {
		return this.httpVersion;
	}

	@NotNull
	@Override
	public ReasonPhrase getReasonPhrase() {
		return this.reasonPhrase;
	}

	@NotNull
	@Override
	public StatusCode getStatusCode() {
		return this.statusCode;
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
