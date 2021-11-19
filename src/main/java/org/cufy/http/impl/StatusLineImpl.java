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

import org.cufy.http.model.HttpVersion;
import org.cufy.http.model.ReasonPhrase;
import org.cufy.http.model.StatusCode;
import org.cufy.http.model.StatusLine;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link StatusLine}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class StatusLineImpl implements StatusLine {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5788252143775840682L;

	/**
	 * The http-version component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected HttpVersion httpVersion;
	/**
	 * The reason-phrase component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected ReasonPhrase reasonPhrase;
	/**
	 * The status-code component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected StatusCode statusCode;

	/**
	 * Construct a new status-line from the given components.
	 *
	 * @param httpVersion  the http-version of the constructed status-line.
	 * @param statusCode   the status-code of the constructed status-line.
	 * @param reasonPhrase the reason-phrase of the constructed status-line.
	 * @throws NullPointerException if the given {@code httpVersion} or {@code statusCode}
	 *                              or {@code reasonPhrase} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@ApiStatus.Internal
	public StatusLineImpl(@NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		Objects.requireNonNull(statusCode, "statusCode");
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	@NotNull
	@Override
	public StatusLineImpl clone() {
		try {
			return (StatusLineImpl) super.clone();
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
		return this.httpVersion.hashCode() ^
			   this.statusCode.hashCode() ^
			   this.reasonPhrase.hashCode();
	}

	@NotNull
	@Override
	public StatusLine setHttpVersion(@NotNull HttpVersion httpVersion) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.httpVersion = httpVersion;
		return this;
	}

	@NotNull
	@Override
	public StatusLine setReasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.reasonPhrase = reasonPhrase;
		return this;
	}

	@NotNull
	@Override
	public StatusLine setStatusCode(@NotNull StatusCode statusCode) {
		Objects.requireNonNull(statusCode, "statusCode");
		this.statusCode = statusCode;
		return this;
	}

	@NotNull
	@Pattern(HttpRegExp.STATUS_LINE)
	@Override
	public String toString() {
		String httpVersion = this.httpVersion.toString();
		String statusCode = this.statusCode.toString();
		String reasonPhrase = this.reasonPhrase.toString();

		return httpVersion + " " + statusCode + " " + reasonPhrase;
	}
}
