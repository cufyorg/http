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

import org.cufy.http.body.Body;
import org.cufy.http.request.HTTPVersion;
import org.cufy.http.request.Headers;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

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
 * @param <B> the type of the body of this response.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Response<B extends Body> extends Cloneable, Serializable {
	/**
	 * An empty response constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Response<Body> EMPTY = new RawResponse();

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new response from copying the given {@code response}.
	 *
	 * @param response the response to copy.
	 * @return a new copy of the given {@code response}.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Response<Body> copy(@NotNull Response<?> response) {
		return new AbstractResponse<>(response);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new response instance to be a placeholder if a the user has not specified
	 * a response.
	 *
	 * @return a new default response.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Response<Body> defaultResponse() {
		return new AbstractResponse<>();
	}

	/**
	 * <b>Empty</b>
	 * <br>
	 * Return an empty unmodifiable response.
	 *
	 * @return an empty unmodifiable response.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Response<Body> empty() {
		return Response.EMPTY;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new response from the given {@code source}.
	 *
	 * @param source the source for the constructed response.
	 * @return a new response from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  HTTPRegExp#RESPONSE}.
	 * @since 0.0.1 ~2021.03.22
	 */
	static Response<Body> parse(@NotNull @NonNls @Pattern(HTTPRegExp.RESPONSE) String source) {
		return new AbstractResponse<>(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw response with the given {@code value}.
	 *
	 * @param value the value of the constructed response.
	 * @return a new raw response.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Response<Body> raw(@NotNull @NonNls String value) {
		return new RawResponse(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code response}.
	 *
	 * @param response the response to be copied.
	 * @return an unmodifiable copy of the given {@code response}.
	 * @throws NullPointerException if the given {@code response} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Response<Body> unmodifiable(@NotNull Response<?> response) {
		return new RawResponse(response);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new response from the given components.
	 *
	 * @param statusLine the status-line of the constructed response.
	 * @param headers    the headers of the constructed response.
	 * @param body       the body of the constructed response.
	 * @return a new response from the given components.
	 * @throws NullPointerException if the given {@code statusLine} or {@code headers} or
	 *                              {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Response<Body> with(@NotNull StatusLine statusLine, @NotNull Headers headers, @NotNull Body body) {
		return new AbstractResponse<>(statusLine, headers, body);
	}

	/**
	 * Set the body of this from the given {@code body}.
	 *
	 * @param body    the body to be set.
	 * @param <BB>    the type of the new body of this.
	 * @param ignored an array that will be completely ignored by this method. It was put
	 *                just to remove the "Ambiguous method call" error between this method
	 *                and {@link #body(Function)}. It was chosen to be an {@code int} to
	 *                reduce the header size of the array so it takes as little as much
	 *                performance-wise. (if the error got fixed, this method might get
	 *                deprecated and another default method will delegate to it!)
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default <BB extends Body> Response<BB> body(@NotNull BB body, int... ignored) {
		throw new UnsupportedOperationException("body");
	}

	/**
	 * Set the body of this from the given {@code body} literal.
	 *
	 * @param body the body literal to set the body of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code body} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Response<Body> body(@NotNull String body) {
		return this.body(Body.parse(body));
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
	 * @param <BB>     the type of the new body of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this request does not support changing its
	 *                                       body.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default <BB extends Body> Response<BB> body(@NotNull Function<B, BB> operator) {
		Objects.requireNonNull(operator, "operator");
		B b = this.body();
		BB body = operator.apply(b);

		if (body != null && body != b)
			this.body(body);

		//noinspection unchecked
		return (Response<BB>) this;
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
	default Response<B> headers(@NotNull Headers headers) {
		throw new UnsupportedOperationException("headers");
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
	default Response<B> headers(@Nullable @NonNls @Pattern(HTTPRegExp.HEADERS) String headers) {
		Objects.requireNonNull(headers, "headers");
		this.headers(Headers.parse(headers));
		return this;
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
	default Response<B> headers(@NotNull UnaryOperator<Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		Headers h = this.headers();
		Headers headers = operator.apply(h);

		if (headers != null && headers != h)
			this.headers(headers);

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
	default Response<B> httpVersion(@NotNull HTTPVersion httpVersion) {
		this.statusLine().httpVersion(httpVersion);
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
	default Response<B> httpVersion(@NotNull @NonNls @Pattern(HTTPRegExp.HTTP_VERSION) String httpVersion) {
		this.statusLine().httpVersion(httpVersion);
		return this;
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
	default Response<B> httpVersion(@NotNull UnaryOperator<HTTPVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		HTTPVersion hv = s.httpVersion();
		HTTPVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			s.httpVersion(httpVersion);

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
	default Response<B> reasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		this.statusLine().reasonPhrase(reasonPhrase);
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
	default Response<B> reasonPhrase(@NotNull @NonNls @Pattern(HTTPRegExp.REASON_PHRASE) String reasonPhrase) {
		this.statusLine().reasonPhrase(reasonPhrase);
		return this;
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
	default Response<B> reasonPhrase(@NotNull UnaryOperator<ReasonPhrase> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		ReasonPhrase m = s.reasonPhrase();
		ReasonPhrase method = operator.apply(m);

		if (method != null && method != m)
			s.reasonPhrase(method);

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
	default Response<B> statusCode(@NotNull StatusCode statusCode) {
		this.statusLine().statusCode(statusCode);
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
	default Response<B> statusCode(@NotNull @NonNls @Pattern(HTTPRegExp.STATUS_CODE) String statusCode) {
		this.statusLine().statusCode(statusCode);
		return this;
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
	default Response<B> statusCode(@NotNull UnaryOperator<StatusCode> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine s = this.statusLine();
		StatusCode sc = s.statusCode();
		StatusCode statusCode = operator.apply(sc);

		if (statusCode != null && statusCode != sc)
			s.statusCode(statusCode);

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
	default Response<B> statusLine(@NotNull StatusLine statusLine) {
		throw new UnsupportedOperationException("statusLine");
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
	default Response<B> statusLine(@NotNull @NonNls @Pattern(HTTPRegExp.STATUS_LINE) String statusLine) {
		Objects.requireNonNull(statusLine, "statusLine");
		this.statusLine(StatusLine.parse(statusLine));
		return this;
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
	default Response<B> statusLine(@NotNull UnaryOperator<StatusLine> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine sl = this.statusLine();
		StatusLine statusLine = operator.apply(sl);

		if (statusLine != null && statusLine != sl)
			this.statusLine(statusLine);

		return this;
	}

	/**
	 * Capture this response into a new object.
	 *
	 * @return a clone of this response.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Response<B> clone();

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
	@NotNull
	@Contract(pure = true)
	B body();

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
//
//	/**
//	 * Set the parameters of this from the given {@code parameters} literal.
//	 *
//	 * @param parameters the parameters literal to set the parameters of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if the given {@code parameters} does not
//	 *                                       match {@link URIRegExp#QUERY}.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response parameters(@NotNull @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=v&v=q") String parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this to the product of combining the given {@code parameters}
//	 * array with the and-sign "&" as the delimiter. The null elements in the given {@code
//	 * parameters} array will be skipped.
//	 *
//	 * @param parameters the values of the new parameters of this.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code parameters}
//	 *                                       does not match {@link URIRegExp#ATTR_VALUE}.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response parameters(@NotNull @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Set the parameters of this from the given {@code parameters}.
//	 *
//	 * @param parameters the parameters to be set.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code parameters} is null.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response parameters(@NotNull Query parameters) {
//		this.body().parameters(parameters);
//		return this;
//	}
//
//	/**
//	 * Replace the parameters of this body to be the result of invoking the given {@code
//	 * operator} with the current parameters of this body. If the {@code operator}
//	 * returned null then nothing happens.
//	 * <br>
//	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
//	 *
//	 * @param operator the computing operator.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code operator} is null.
//	 * @throws UnsupportedOperationException if the parameters of this cannot be changed
//	 *                                       and the returned parameters from the given
//	 *                                       {@code operator} is different from the
//	 *                                       current parameters.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response parameters(@NotNull UnaryOperator<Query> operator) {
//		Objects.requireNonNull(operator, "operator");
//		Body b = this.body();
//		Query p = b.parameters();
//		Query parameters = operator.apply(p);
//
//		if (parameters != null && parameters != p)
//			b.parameters(parameters);
//
//		return this;
//	}
//
//	/**
//	 * Return the parameters defined for this.
//	 *
//	 * @return the parameters of this.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(pure = true)
//	default Query parameters() {
//		return this.body().parameters();
//	}
//
//	/**
//	 * Set the headers of this from the given {@code headers} array.
//	 *
//	 * @param headers the headers array to set the headers of this from.
//	 * @return this.
//	 * @throws NullPointerException          if the given {@code headers} is null.
//	 * @throws IllegalArgumentException      if an element in the given {@code headers}
//	 *                                       does not match {@link HTTPRegExp#HEADER}.
//	 * @throws UnsupportedOperationException if this response does not support changing
//	 *                                       its headers.
//	 * @since 0.0.1 ~2021.03.21
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response<B> headers(@NotNull @NonNls @Pattern(HTTPRegExp.HEADER) String @NotNull ... headers) {
//		Objects.requireNonNull(headers, "headers");
//		this.headers(Headers.parse(headers));
//		return this;
//	}
//
//	/**
//	 * Set the status-code of this from the given {@code statusCode} number.
//	 *
//	 * @param statusCode the status-code number to set the status-code of this from.
//	 * @return this.
//	 * @throws IllegalArgumentException      if the given {@code statusCode} is negative.
//	 * @throws UnsupportedOperationException if the status-line of this does not allow
//	 *                                       changing its status-code.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	@NotNull
//	@Contract(value = "_->this", mutates = "this")
//	default Response<B> statusCode(@Range(from = 0, to = 999) int statusCode) {
//		this.statusLine().statusCode(statusCode);
//		return this;
//	}
