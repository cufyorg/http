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

import org.cufy.http.component.Body;
import org.cufy.http.component.HTTPVersion;
import org.cufy.http.component.Headers;
import org.cufy.http.component.Query;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A structure holding the variables of a response from the server.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Response extends Cloneable, Serializable {
	/**
	 * Return a new response instance to be a placeholder if a the user has not specified
	 * a response.
	 *
	 * @return a new empty response.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Response defaultResponse() {
		return new AbstractResponse();
	}

	/**
	 * Construct a new response from the given {@code source}.
	 *
	 * @param source the source for the constructed response.
	 * @return a new response from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#RESPONSE}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Response parse(@NotNull @NonNls @Pattern(HTTPRegExp.RESPONSE) @Subst("HTTP/1.1 200 OK\n") String source) {
		return new AbstractResponse(source);
	}

	/**
	 * Replace the body of this to be the result of invoking the given {@code operator}
	 * with the argument being the current body. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response body(@NotNull UnaryOperator<Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.body();
		Body body = operator.apply(b);

		if (body != null && body != b)
			this.body(body);

		return this;
	}

	/**
	 * Set the body of this from the given {@code body} literal.
	 *
	 * @param body the body literal to set the body of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response body(@NotNull @NonNls String body) {
		return this.body(Body.parse(body));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Response body(@NotNull @NonNls String content, @NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) @Subst("q=0&v=1") String parameters) {
		return this.body(Body.parse(content, parameters));
	}

	/**
	 * Set the body of this from the given {@code content} and {@code parameters}.
	 *
	 * @param content    the body content to set the body of this from.
	 * @param parameters the parameters.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} or {@code
	 *                                       parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Response body(@NotNull @NonNls String content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		this.body(Body.parse(content, parameters));
		return this;
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response body(@NotNull Body body) {
		throw new UnsupportedOperationException("body");
	}

	/**
	 * Replace the headers of this to be the result of invoking the given {@code operator}
	 * with the argument being the current headers. If the {@code operator} returned
	 * {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers and the given {@code operator}
	 *                                       returned another headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.headers();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.headers(headers);

		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers} literal.
	 *
	 * @param headers the headers literal to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if the given {@code headers} does not match
	 *                                       {@link HTTPRegExp#HEADERS}.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response headers(@Nullable @NonNls @Pattern(HTTPRegExp.HEADERS) @Subst("X:Y\r\nZ:A\r\n") String headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers} array.
	 *
	 * @param headers the headers array to set the headers of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code headers}
	 *                                       does not match {@link HTTPRegExp#HEADER}.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response headers(@NotNull @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
	}

	/**
	 * Set the headers of this from the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its headers.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response headers(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
	}

	/**
	 * Replace the http-version of this to be the result of invoking the given {@code
	 * operator} with the argument being the current http-version. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its http-version and the given
	 *                                       {@code operator} returned another
	 *                                       http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response httpVersion(@NotNull UnaryOperator<HTTPVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		HTTPVersion hv = s.httpVersion();
		HTTPVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			s.httpVersion(httpVersion);

		return this;
	}

	/**
	 * Set the http-version of this from the given {@code httpVersion} literal.
	 *
	 * @param httpVersion the http-version literal to set the http-version of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws IllegalArgumentException      if the given {@code httpVersion} does not
	 *                                       match {@link HTTPRegExp#HTTP_VERSION}.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response httpVersion(@NotNull @NonNls @Pattern(HTTPRegExp.HTTP_VERSION) @Subst("HTTP/1.1") String httpVersion) {
		this.statusLine().httpVersion(httpVersion);
		return this;
	}

	/**
	 * Set the http-version of this status-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this status-line.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its http-version.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response httpVersion(@NotNull HTTPVersion httpVersion) {
		this.statusLine().httpVersion(httpVersion);
		return this;
	}

	/**
	 * Return the http-version of this status-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default HTTPVersion httpVersion() {
		return this.statusLine().httpVersion();
	}

	/**
	 * Set the parameters of this from the given {@code parameters} literal.
	 *
	 * @param parameters the parameters literal to set the parameters of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if the given {@code parameters} does not
	 *                                       match {@link URIRegExp#QUERY}.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Set the parameters of this to the product of combining the given {@code parameters}
	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
	 * parameters} array will be skipped.
	 *
	 * @param parameters the values of the new parameters of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Set the parameters of this from the given {@code parameters}.
	 *
	 * @param parameters the parameters to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code parameters} is null.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response parameters(@NotNull Query parameters) {
		this.body().parameters(parameters);
		return this;
	}

	/**
	 * Replace the parameters of this body to be the result of invoking the given {@code
	 * operator} with the current parameters of this body. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the parameters of this cannot be changed
	 *                                       and the returned parameters from the given
	 *                                       {@code operator} is different from the
	 *                                       current parameters.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response parameters(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.body();
		Query p = b.parameters();
		Query parameters = operator.apply(p);

		if (parameters != null && parameters != p)
			b.parameters(parameters);

		return this;
	}

	/**
	 * Return the parameters defined for this.
	 *
	 * @return the parameters of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default Query parameters() {
		return this.body().parameters();
	}

	/**
	 * Replace the phrase of this to be the result of invoking the given {@code operator}
	 * with the argument being the current phrase. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its method and the given {@code
	 *                                       operator} returned another phrase.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response reasonPhrase(@NotNull UnaryOperator<ReasonPhrase> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		ReasonPhrase m = s.reasonPhrase();
		ReasonPhrase method = operator.apply(m);

		if (method != null && method != m)
			s.reasonPhrase(method);

		return this;
	}

	/**
	 * Set the phrase of this from the given {@code phrase} literal.
	 *
	 * @param reasonPhrase the phrase literal to set the phrase of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws IllegalArgumentException      if the given {@code reasonPhrase} does not
	 *                                       match {@link HTTPRegExp#REASON_PHRASE}.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its phrase.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response reasonPhrase(@NotNull @NonNls @Pattern(HTTPRegExp.REASON_PHRASE) @Subst("OK") String reasonPhrase) {
		this.statusLine().reasonPhrase(reasonPhrase);
		return this;
	}

	/**
	 * Set the phrase of this to the given {@code phrase}.
	 *
	 * @param reasonPhrase the phrase to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not support
	 *                                       changing its phrase.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response reasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		this.statusLine().reasonPhrase(reasonPhrase);
		return this;
	}

	/**
	 * Return the reason-phrase of this status-line.
	 *
	 * @return the phrase of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default ReasonPhrase reasonPhrase() {
		return this.statusLine().reasonPhrase();
	}

	/**
	 * Replace the port of this to be the result of invoking the given {@code operator}
	 * with the argument being the current port. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its port and the given {@code
	 *                                       operator} returned another port.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusCode(@NotNull UnaryOperator<StatusCode> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		StatusCode sc = s.statusCode();
		StatusCode statusCode = operator.apply(sc);

		if (statusCode != null && statusCode != sc)
			s.statusCode(statusCode);

		return this;
	}

	/**
	 * Set the status-code of this from the given {@code statusCode} literal.
	 *
	 * @param statusCode the status-code literal to set the status-code of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws IllegalArgumentException      if the given {@code statusCode} does not
	 *                                       match {@link HTTPRegExp#STATUS_CODE}.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its status-code.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusCode(@NotNull @NonNls @Pattern(HTTPRegExp.STATUS_CODE) @Subst("200") String statusCode) {
		this.statusLine().statusCode(statusCode);
		return this;
	}

	/**
	 * Set the status-code of this from the given {@code statusCode} number.
	 *
	 * @param statusCode the status-code number to set the status-code of this from.
	 * @return this.
	 * @throws IllegalArgumentException      if the given {@code statusCode} is negative.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its status-code.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusCode(@Range(from = 0, to = 999) int statusCode) {
		this.statusLine().statusCode(statusCode);
		return this;
	}

	/**
	 * Set the status-code of this to be the given {@code statusCode}.
	 *
	 * @param statusCode the new status-code of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws UnsupportedOperationException if the status-line of this does not allow
	 *                                       changing its status-code.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusCode(@NotNull StatusCode statusCode) {
		this.statusLine().statusCode(statusCode);
		return this;
	}

	/**
	 * Return the status-code defined for this.
	 *
	 * @return the status-code of this.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	default StatusCode statusCode() {
		return this.statusLine().statusCode();
	}

	/**
	 * Replace the status-line of this to be the result of invoking the given {@code
	 * operator} with the argument being the current status-line. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status and the {@code operator} returned
	 *                                       another status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusLine(@NotNull UnaryOperator<StatusLine> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine sl = this.statusLine();
		StatusLine statusLine = operator.apply(sl);

		if (statusLine != null && statusLine != sl)
			this.statusLine(statusLine);

		return this;
	}

	/**
	 * Set the statusLine of this from the given {@code statusLine} literal.
	 *
	 * @param statusLine the statusLine literal to set the statusLine of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusLine} is null.
	 * @throws IllegalArgumentException      if the given {@code statusLine} does not
	 *                                       match {@link HTTPRegExp#STATUS_LINE}.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusLine(@NotNull @NonNls @Pattern(HTTPRegExp.STATUS_LINE) @Subst("HTTP/1.1 200 OK") String statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.statusLine(StatusLine.parse(statusLine));
		return this;
	}

	/**
	 * Set the statusLine of this from the given {@code statusLine}.
	 *
	 * @param statusLine the statusLine to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusLine} is null.
	 * @throws UnsupportedOperationException if this response does not support changing
	 *                                       its status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response statusLine(@NotNull StatusLine statusLine) {
		throw new UnsupportedOperationException("statusLine");
	}

	/**
	 * Capture this response into a new object.
	 *
	 * @return a clone of this response.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Response clone();

	/**
	 * Two responses are equal when they are the same instance or have an equal {@link
	 * #statusLine()}, {@link #headers()} and {@link #body()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a response and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a response is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this response.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	@NonNls
	@Contract(pure = true)
	@Pattern(HTTPRegExp.RESPONSE)
	@Override
	String toString();

	/**
	 * Get the body of this response.
	 *
	 * @return the body of this response.
	 * @since 0.0.1 ~2021.03.22
	 */
	Body body();

	/**
	 * Get the headers of this response.
	 *
	 * @return the headers of this response.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	Headers headers();

	/**
	 * Get the status-line of this response.
	 *
	 * @return the status line of this.
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	@Contract(pure = true)
	StatusLine statusLine();
}
