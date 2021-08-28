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

import org.cufy.http.request.HttpVersion;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
public interface StatusLine extends Cloneable, Serializable {
	/**
	 * An empty status-line constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	StatusLine EMPTY = new RawStatusLine();

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw status-line with the given {@code value}.
	 *
	 * @param value the value of the constructed status-line.
	 * @return a new raw status-line.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static StatusLine raw(@NotNull String value) {
		return new RawStatusLine(value);
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code statusLine}.
	 *
	 * @param statusLine the status-line to be copied.
	 * @return an unmodifiable copy of the given {@code statusLine}.
	 * @throws NullPointerException if the given {@code statusLine} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static StatusLine raw(@NotNull StatusLine statusLine) {
		return new RawStatusLine(statusLine);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new status-line instance to be a placeholder if a the user has not
	 * specified a status-line.
	 *
	 * @return a new default status-line.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static StatusLine statusLine() {
		return new AbstractStatusLine();
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
	static StatusLine statusLine(@NotNull StatusLine statusLine) {
		return new AbstractStatusLine(statusLine);
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
	static StatusLine statusLine(@NotNull @Pattern(HttpRegExp.STATUS_LINE) String source) {
		return new AbstractStatusLine(source);
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
	static StatusLine statusLine(@NotNull HttpVersion httpVersion, @NotNull StatusCode statusCode, @NotNull ReasonPhrase reasonPhrase) {
		return new AbstractStatusLine(httpVersion, statusCode, reasonPhrase);
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
	static StatusLine statusLine(@NotNull Consumer<StatusLine> builder) {
		Objects.requireNonNull(builder, "builder");
		StatusLine statusLine = new AbstractStatusLine();
		builder.accept(statusLine);
		return statusLine;
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
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its http-version and the given {@code
	 *                                       operator} returned another http-version.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine httpVersion(@NotNull UnaryOperator<HttpVersion> operator) {
		Objects.requireNonNull(operator, "operator");
		HttpVersion hv = this.getHttpVersion();
		HttpVersion httpVersion = operator.apply(hv);

		if (httpVersion != null && httpVersion != hv)
			this.setHttpVersion(httpVersion);

		return this;
	}

	/**
	 * Invoke the given {@code operator} with {@code this} as the parameter and return the
	 * result returned from the operator.
	 *
	 * @param operator the operator to be invoked.
	 * @return the result from invoking the given {@code operator} or {@code this} if the
	 * 		given {@code operator} returned {@code null}.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->new")
	default StatusLine map(UnaryOperator<StatusLine> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusLine mapped = operator.apply(this);
		return mapped == null ? this : mapped;
	}

	/**
	 * Execute the given {@code consumer} with {@code this} as the parameter.
	 *
	 * @param consumer the consumer to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code consumer} is null.
	 * @since 0.2.9 ~2021.08.28
	 */
	@NotNull
	@Contract("_->this")
	default StatusLine peek(Consumer<StatusLine> consumer) {
		Objects.requireNonNull(consumer, "consumer");
		consumer.accept(this);
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
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its method and the given {@code operator}
	 *                                       returned another phrase.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine reasonPhrase(@NotNull UnaryOperator<ReasonPhrase> operator) {
		Objects.requireNonNull(operator, "operator");
		ReasonPhrase m = this.getReasonPhrase();
		ReasonPhrase method = operator.apply(m);

		if (method != null && method != m)
			this.setReasonPhrase(method);

		return this;
	}

	/**
	 * Set the http-version of this status-line to be the given {@code httpVersion}.
	 *
	 * @param httpVersion the new http-version of this status-line.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its http-version.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setHttpVersion(@NotNull HttpVersion httpVersion) {
		throw new UnsupportedOperationException("httpVersion");
	}

	/**
	 * Set the http-version of this from the given {@code httpVersion} literal.
	 *
	 * @param httpVersion the http-version literal to set the http-version of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code httpVersion} is null.
	 * @throws IllegalArgumentException      if the given {@code httpVersion} does not
	 *                                       match {@link HttpRegExp#HTTP_VERSION}.
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its http-version.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setHttpVersion(@NotNull @Pattern(HttpRegExp.HTTP_VERSION) String httpVersion) {
		return this.setHttpVersion(HttpVersion.httpVersion(httpVersion));
	}

	/**
	 * Set the phrase of this from the given {@code phrase} literal.
	 *
	 * @param reasonPhrase the phrase literal to set the phrase of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws IllegalArgumentException      if the given {@code reasonPhrase} does not
	 *                                       match {@link HttpRegExp#REASON_PHRASE}.
	 * @throws UnsupportedOperationException if this response-line does not support
	 *                                       changing its phrase.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setReasonPhrase(@NotNull @Pattern(HttpRegExp.REASON_PHRASE) String reasonPhrase) {
		return this.setReasonPhrase(ReasonPhrase.reasonPhrase(reasonPhrase));
	}

	/**
	 * Set the phrase of this to the given {@code phrase}.
	 *
	 * @param reasonPhrase the phrase to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code reasonPhrase} is null.
	 * @throws UnsupportedOperationException if this status-line does not support changing
	 *                                       its phrase.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setReasonPhrase(@NotNull ReasonPhrase reasonPhrase) {
		throw new UnsupportedOperationException("reasonPhrase");
	}

	/**
	 * Set the status-code of this to be the given {@code statusCode}.
	 *
	 * @param statusCode the new status-code of this.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws UnsupportedOperationException if this status-line does not allow changing
	 *                                       its status-code.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setStatusCode(@NotNull StatusCode statusCode) {
		throw new UnsupportedOperationException("port");
	}

	/**
	 * Set the status-code of this from the given {@code statusCode} literal.
	 *
	 * @param statusCode the status-code literal to set the status-code of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code statusCode} is null.
	 * @throws IllegalArgumentException      if the given {@code statusCode} does not
	 *                                       match {@link HttpRegExp#STATUS_CODE}.
	 * @throws UnsupportedOperationException if this status-line does not allow changing
	 *                                       its status-code.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine setStatusCode(@NotNull @Pattern(HttpRegExp.STATUS_CODE) String statusCode) {
		return this.setStatusCode(StatusCode.statusCode(statusCode));
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
	 * @throws UnsupportedOperationException if this authority does not allow changing its
	 *                                       port and the given {@code operator} returned
	 *                                       another port.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default StatusLine statusCode(@NotNull UnaryOperator<StatusCode> operator) {
		Objects.requireNonNull(operator, "operator");
		StatusCode sc = this.getStatusCode();
		StatusCode statusCode = operator.apply(sc);

		if (statusCode != null && statusCode != sc)
			this.setStatusCode(statusCode);

		return this;
	}

	/**
	 * Capture this status-line into a new object.
	 *
	 * @return a clone of this status-line.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	StatusLine clone();

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
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a status-line is the {@code xor} of the hash codes of its
	 * components. (optional)
	 *
	 * @return the hash code of this status-line.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	String toString();

	/**
	 * Return the http-version of this status-line.
	 *
	 * @return the http version of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	HttpVersion getHttpVersion();

	/**
	 * Return the reason-phrase of this status-line.
	 *
	 * @return the phrase of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	ReasonPhrase getReasonPhrase();

	/**
	 * Return the status-code defined for this.
	 *
	 * @return the status-code of this.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	StatusCode getStatusCode();
}
