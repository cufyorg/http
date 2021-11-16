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
	 * <b>Default</b>
	 * <br>
	 * Construct a new default status-line.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public StatusLineImpl() {
		this.httpVersion = HttpVersion.HTTP1_1;
		this.statusCode = StatusCode.OK;
		this.reasonPhrase = ReasonPhrase.OK;
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new status-line from copying the given {@code statusLine}.
	 *
	 * @param statusLine the status-line to copy.
	 * @throws NullPointerException if the given {@code statusLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public StatusLineImpl(@NotNull StatusLine statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.httpVersion = statusLine.getHttpVersion();
		this.statusCode = statusLine.getStatusCode();
		this.reasonPhrase = statusLine.getReasonPhrase();
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new status-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed status-line.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#STATUS_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public StatusLineImpl(@NotNull @Pattern(HttpRegExp.STATUS_LINE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HttpPattern.STATUS_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid status-line: " + source);

		Matcher matcher = HttpParse.STATUS_LINE.matcher(source);

		if (matcher.find()) {
			String httpVersion = matcher.group("HttpVersion");
			String statusCode = matcher.group("StatusCode");
			String reasonPhrase = matcher.group("ReasonPhrase");

			this.httpVersion = HttpVersionImpl.httpVersion(httpVersion);
			this.statusCode = StatusCodeImpl.statusCode(statusCode);
			this.reasonPhrase = ReasonPhraseImpl.reasonPhrase(reasonPhrase);
		} else {
			this.httpVersion = HttpVersion.HTTP1_1;
			this.statusCode = StatusCode.OK;
			this.reasonPhrase = ReasonPhrase.OK;
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new status-line from the given components.
	 *
	 * @param httpVersion  the http-version of the constructed status-line.
	 * @param statusCode   the status-code of the constructed status-line.
	 * @param reasonPhrase the reason-phrase of the constructed status-line.
	 * @throws NullPointerException if the given {@code httpVersion} or {@code statusCode}
	 *                              or {@code reasonPhrase} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public StatusLineImpl(@NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		Objects.requireNonNull(statusCode, "statusCode");
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new status-line from copying the given {@code statusLine}.
	 *
	 * @param statusLine the status-line to copy.
	 * @return a new copy of the given {@code statusLine}.
	 * @throws NullPointerException if the given {@code statusLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static StatusLine statusLine(@NotNull StatusLine statusLine) {
		return new StatusLineImpl(statusLine);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new status-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed status-line.
	 * @return a new status-line from the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HttpRegExp#STATUS_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static StatusLine statusLine(@NotNull @Pattern(HttpRegExp.STATUS_LINE) String source) {
		return new StatusLineImpl(source);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new status-line from the given components.
	 *
	 * @param httpVersion  the http-version of the constructed status-line.
	 * @param statusCode   the status-code of the constructed status-line.
	 * @param reasonPhrase the reason-phrase of the constructed status-line.
	 * @return a new status-line from the given components.
	 * @throws NullPointerException if the given {@code httpVersion} or {@code statusCode}
	 *                              or {@code reasonPhrase} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_,_->new", pure = true)
	public static StatusLine statusLine(@NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		return new StatusLineImpl(httpVersion, statusCode, reasonPhrase);
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new status line with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new status line.
	 * @return the status line constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static StatusLine statusLine(@NotNull Consumer<StatusLine> builder) {
		Objects.requireNonNull(builder, "builder");
		StatusLine statusLine = new StatusLineImpl();
		builder.accept(statusLine);
		return statusLine;
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

			//noinspection NonFinalFieldReferenceInEquals
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
