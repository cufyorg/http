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

import org.cufy.http.body.BytesBody;
import org.cufy.http.internal.syntax.AbnfPattern;
import org.cufy.http.internal.syntax.HttpParse;
import org.cufy.http.internal.syntax.HttpPattern;
import org.cufy.http.internal.syntax.HttpRegExp;
import org.cufy.http.mime.Mime;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

/**
 * <b>Components</b>
 * <br>
 * A structure holding the variables of a response from the server.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link StatusLine}</li>
 *     <li>{@link Headers}</li>
 *     <li>{@link Body}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class Response extends Message {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -1585847337938412917L;

	/**
	 * The status line.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected StatusLine statusLine;

	/**
	 * Construct a new response.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Response() {
		this.statusLine = new StatusLine();
	}

	/**
	 * Construct a new response from the given components.
	 *
	 * @param statusLine the status-line of the constructed response.
	 * @param headers    the headers of the constructed response.
	 * @param body       the body of the constructed response.
	 * @throws NullPointerException if the given {@code statusLine} or {@code headers} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public Response(
			@NotNull StatusLine statusLine,
			@NotNull Headers headers,
			@Nullable Body body
	) {
		super(headers, body);
		this.statusLine =
				Objects.requireNonNull(statusLine, "statusLine");
	}

	/**
	 * Construct a new response with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new response.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Response(@NotNull Consumer<@NotNull Response> builder) {
		Objects.requireNonNull(builder, "builder");
		this.statusLine = new StatusLine();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
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
	public static Response parse(@NotNull @Pattern(HttpRegExp.RESPONSE) String source) {
		Objects.requireNonNull(source, "source");

		if (!HttpPattern.RESPONSE.matcher(source).matches())
			throw new IllegalArgumentException("invalid response: " + source);

		Matcher matcher = HttpParse.RESPONSE.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid response " + source);

		String statusLineSrc = matcher.group("StatusLine");
		String headersSrc = matcher.group("Headers");
		String bodySrc = matcher.group("Body");

		StatusLine statusLine =
				statusLineSrc == null || statusLineSrc.isEmpty() ?
				new StatusLine() : StatusLine.parse(statusLineSrc);
		Headers headers =
				headersSrc == null || headersSrc.isEmpty() ?
				new Headers() : Headers.parse(headersSrc);

		String mimeSrc = headers.get(Headers.CONTENT_TYPE);

		Mime mime =
				mimeSrc == null || mimeSrc.isEmpty() ?
				null : Mime.parse(mimeSrc);
		Body body =
				bodySrc == null || bodySrc.isEmpty() ?
				null :
				new BytesBody(mime, bodySrc.getBytes());

		return new Response(
				statusLine,
				headers,
				body
		);
	}

	/**
	 * Capture this response into a new object.
	 *
	 * @return a clone of this response.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Response clone() {
		Response clone = (Response) super.clone();
		clone.statusLine = this.statusLine.clone();
		return clone;
	}

	/**
	 * Two responses are equal when they are the same instance or have an equal {@link
	 * #getStatusLine()}, {@link #getHeaders()} and {@link #getBody()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a response and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
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

	/**
	 * The hash code of a response is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this response.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.statusLine.hashCode() ^
			   this.headers.hashCode() ^
			   (this.body == null ? 0 : this.body.hashCode());
	}

	/**
	 * A string representation of this response. Invoke to get the text representing this
	 * in a response.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Status-Line
	 *     Headers
	 *     <br>
	 *     Body
	 * </pre>
	 * Example:
	 * <pre>
	 *     HTTP/1.1 200 OK
	 *     Content-Type: application/json<br>
	 *     {name:"alex"}<br>
	 *     name=alex&age=22
	 * </pre>
	 *
	 * @return a string representation of this Response.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
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

	/**
	 * Get the status-line of this response.
	 *
	 * @return the status line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	public StatusLine getStatusLine() {
		return this.statusLine;
	}

	/**
	 * Set the statusLine of this from the given {@code statusLine}.
	 *
	 * @param statusLine the statusLine to be set.
	 * @throws NullPointerException          if the given {@code statusLine} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setStatusLine(@NotNull StatusLine statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.statusLine = statusLine;
	}
}
