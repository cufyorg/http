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

import org.cufy.http.component.HTTPVersion;
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
	protected HTTPVersion httpVersion = HTTPVersion.defaultHTTPVersion();
	/**
	 * The reason-phrase component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected ReasonPhrase reasonPhrase = ReasonPhrase.defaultReasonPhrase();
	/**
	 * The status-code component.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected StatusCode statusCode = StatusCode.defaultStatusCode();

	/**
	 * Construct an empty status-line.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractStatusLine() {
	}

	/**
	 * Construct a new status-line from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed status-line.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#STATUS_LINE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractStatusLine(@NotNull @NonNls @Pattern(HTTPRegExp.STATUS_LINE) String source) {
		Objects.requireNonNull(source, "source");
		if (!HTTPPattern.STATUS_LINE.matcher(source).matches())
			throw new IllegalArgumentException("invalid status-line: " + source);

		Matcher matcher = HTTPParse.STATUS_LINE.matcher(source);

		if (matcher.find()) {
			@Subst("HTTP/1.1") String httpVersion = matcher.group("HTTPVersion");
			@Subst("200") String statusCode = matcher.group("StatusCode");
			@Subst("OK") String reasonPhrase = matcher.group("ReasonPhrase");

			this.httpVersion = HTTPVersion.parse(httpVersion);
			this.statusCode = StatusCode.parse(statusCode);
			this.reasonPhrase = ReasonPhrase.parse(reasonPhrase);
		}
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
			return Objects.equals(this.httpVersion, statusLine.httpVersion()) &&
				   Objects.equals(this.statusCode, statusLine.statusCode()) &&
				   Objects.equals(this.reasonPhrase, statusLine.reasonPhrase());
		}

		return false;
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
	public HTTPVersion httpVersion() {
		return this.httpVersion;
	}

	@NotNull
	@Override
	public StatusLine httpVersion(@NotNull HTTPVersion httpVersion) {
		Objects.requireNonNull(httpVersion, "httpVersion");
		this.httpVersion = httpVersion;
		return this;
	}

	@NotNull
	@Override
	public ReasonPhrase reasonPhrase() {
		return this.reasonPhrase;
	}

	@NotNull
	@Override
	public StatusLine reasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		Objects.requireNonNull(reasonPhrase, "reasonPhrase");
		this.reasonPhrase = reasonPhrase;
		return this;
	}

	@NotNull
	@Override
	public StatusCode statusCode() {
		return this.statusCode;
	}

	@NotNull
	@Override
	public StatusLine statusCode(@NotNull StatusCode statusCode) {
		Objects.requireNonNull(statusCode, "statusCode");
		this.statusCode = statusCode;
		return this;
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.STATUS_LINE)
	@Override
	public String toString() {
		String httpVersion = this.httpVersion.toString();
		String statusCode = this.statusCode.toString();
		String reasonPhrase = this.reasonPhrase.toString();

		@Subst("HTTP/1.1 200 OK") String s =
				httpVersion + " " + statusCode + " " + reasonPhrase;
		return s;
	}
}
