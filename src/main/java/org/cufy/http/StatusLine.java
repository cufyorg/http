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
package org.cufy.http;

import org.cufy.http.syntax.HttpParse;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * <b>Components</b>
 * <br>
 * The status-line; an object describing the first line of an http-response.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link HttpVersion}</li>
 *     <li>{@link StatusCode}</li>
 *     <li>{@link ReasonPhrase}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class StatusLine implements Cloneable, Serializable {
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
	 * Construct a new status-line.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public StatusLine() {
		this.httpVersion = HttpVersion.HTTP1_1;
		this.statusCode = StatusCode.OK;
		this.reasonPhrase = ReasonPhrase.OK;
	}

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
	public StatusLine(@NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		Objects.requireNonNull(statusCode, "statusCode");
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Construct a new status line with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new status line.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public StatusLine(@NotNull Consumer<@NotNull StatusLine> builder) {
		Objects.requireNonNull(builder, "builder");
		this.httpVersion = HttpVersion.HTTP1_1;
		this.statusCode = StatusCode.OK;
		this.reasonPhrase = ReasonPhrase.OK;
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
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
	public static StatusLine parse(@NotNull @Pattern(HttpRegExp.STATUS_LINE) String source) {
		Objects.requireNonNull(source, "source");

		if (!HttpPattern.STATUS_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid status-line: " + source);

		Matcher matcher = HttpParse.STATUS_LINE.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid status-line " + source);

		String httpVersionSrc = matcher.group("HttpVersion");
		String statusCodeSrc = matcher.group("StatusCode");
		String reasonPhraseSrc = matcher.group("ReasonPhrase");

		HttpVersion httpVersion =
				httpVersionSrc == null || httpVersionSrc.isEmpty() ?
				HttpVersion.HTTP1_1 : HttpVersion.parse(httpVersionSrc);
		StatusCode statusCode =
				statusCodeSrc == null || statusCodeSrc.isEmpty() ?
				StatusCode.OK : StatusCode.parse(statusCodeSrc);
		ReasonPhrase reasonPhrase =
				reasonPhraseSrc == null || reasonPhraseSrc.isEmpty() ?
				ReasonPhrase.OK : ReasonPhrase.parse(reasonPhraseSrc);

		return new StatusLine(
				httpVersion,
				statusCode,
				reasonPhrase
		);
	}

	/**
	 * Capture this status-line into a new object.
	 *
	 * @return a clone of this status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public StatusLine clone() {
		try {
			return (StatusLine) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two status-lines are equal when they are the same instance or have an equal {@link
	 * #getHttpVersion()}, {@link #getStatusCode()} and {@link #getReasonPhrase()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a status-line and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
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

	/**
	 * The hash code of a status-line is the {@code xor} of the hash codes of its
	 * components. (optional)
	 *
	 * @return the hash code of this status-line.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.httpVersion.hashCode() ^
			   this.statusCode.hashCode() ^
			   this.reasonPhrase.hashCode();
	}

	/**
	 * A string representation of this Status-Line. Invoke to get the text representing
	 * this in a request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Http-Version STATUS-CODE STATUS-PHRASE
	 * </pre>
	 * Example:
	 * <pre>
	 *     HTTP/1.1 200 OK
	 * </pre>
	 *
	 * @return a string representation of this status-line.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(HttpRegExp.STATUS_LINE)
	@Contract(pure = true)
	@Override
	public String toString() {
		String httpVersion = this.httpVersion.toString();
		String statusCode = this.statusCode.toString();
		String reasonPhrase = this.reasonPhrase.toString();

		return httpVersion + " " + statusCode + " " + reasonPhrase;
	}

	/**
	 * Return the http-version of this status-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	public HttpVersion getHttpVersion() {
		return this.httpVersion;
	}

	/**
	 * Return the reason-phrase of this status-line.
	 *
	 * @return the phrase of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	public ReasonPhrase getReasonPhrase() {
		return this.reasonPhrase;
	}

	/**
	 * Return the status-code defined for this.
	 *
	 * @return the status-code of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	public StatusCode getStatusCode() {
		return this.statusCode;
	}

	/**
	 * Set the http-version of this status-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this status-line.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its http-version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@Contract(mutates = "this")
	public void setHttpVersion(@NotNull HttpVersion httpVersion) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.httpVersion = httpVersion;
	}

	/**
	 * Set the phrase of this to the given {@code phrase}.
	 *
	 * @param reasonPhrase the phrase to be set.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its phrase.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setReasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.reasonPhrase = reasonPhrase;
	}

	/**
	 * Set the status-code of this to be the given {@code statusCode}.
	 *
	 * @param statusCode the new status-code of this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws UnsupportedOperationException if this status-line does not allow changing
	 *                                       its status-code.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setStatusCode(@NotNull StatusCode statusCode) {
		Objects.requireNonNull(statusCode, "statusCode");
		this.statusCode = statusCode;
	}
}
