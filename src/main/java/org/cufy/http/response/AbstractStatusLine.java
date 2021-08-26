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

import org.cufy.http.request.HTTPVersion;
import org.cufy.http.syntax.HTTPParse;
import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link StatusLine}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class AbstractStatusLine implements StatusLine {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5788252143775840682L;

	/**
	 * The http-version component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected HTTPVersion httpVersion;
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
	public AbstractStatusLine() {
		this.httpVersion = HTTPVersion.httpVersion();
		this.statusCode = StatusCode.statusCode();
		this.reasonPhrase = ReasonPhrase.reasonPhrase();
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
	public AbstractStatusLine(@NotNull StatusLine statusLine) {
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
	 *                                  HTTPRegExp#STATUS_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractStatusLine(@NotNull @Pattern(HTTPRegExp.STATUS_LINE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.STATUS_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid status-line: " + source);

		Matcher matcher = HTTPParse.STATUS_LINE.matcher(source);

		if (matcher.find()) {
			String httpVersion = matcher.group("HTTPVersion");
			String statusCode = matcher.group("StatusCode");
			String reasonPhrase = matcher.group("ReasonPhrase");

			this.httpVersion = HTTPVersion.httpVersion(httpVersion);
			this.statusCode = StatusCode.statusCode(statusCode);
			this.reasonPhrase = ReasonPhrase.reasonPhrase(reasonPhrase);
		} else {
			this.httpVersion = HTTPVersion.httpVersion();
			this.statusCode = StatusCode.statusCode();
			this.reasonPhrase = ReasonPhrase.reasonPhrase();
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
	public AbstractStatusLine(@NotNull HTTPVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		Objects.requireNonNull(statusCode, "statusCode");
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.httpVersion = httpVersion;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	@NotNull
	@Override
	public AbstractStatusLine clone() {
		try {
			return (AbstractStatusLine) super.clone();
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
	public HTTPVersion getHttpVersion() {
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
		//noinspection NonFinalFieldReferencedInHashCode
		return this.httpVersion.hashCode() ^
			   this.statusCode.hashCode() ^
			   this.reasonPhrase.hashCode();
	}

	@NotNull
	@Override
	public StatusLine setHttpVersion(@NotNull HTTPVersion httpVersion) {
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
	@Pattern(HTTPRegExp.STATUS_LINE)
	@Override
	public String toString() {
		String httpVersion = this.httpVersion.toString();
		String statusCode = this.statusCode.toString();
		String reasonPhrase = this.reasonPhrase.toString();

		return httpVersion + " " + statusCode + " " + reasonPhrase;
	}
}
