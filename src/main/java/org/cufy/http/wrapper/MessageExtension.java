/*
 *	Copyright 2021 Cufy and ProgSpaceSA
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
package org.cufy.http.wrapper;

import org.cufy.http.Body;
import org.cufy.http.Headers;
import org.cufy.http.Message;
import org.cufy.http.internal.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A call cursor with shortcut message field accessors.
 *
 * @param <M>    the type of the message.
 * @param <Self> the type of this.
 * @author LSafer
 * @version 0.3.0
 * @since 0.3.0 ~2021.12.12
 */
public interface MessageExtension<M extends Message, Self extends MessageExtension<M, Self>> extends MessageWrapper<M, Self> {
	// Body

	/**
	 * Return the body.
	 *
	 * @return the body.
	 * @since 0.3.0 ~2021.11.20
	 */
	@Nullable
	@Contract(pure = true)
	default Body body() {
		return this.message().getBody();
	}

	/**
	 * Set the body to the given {@code body}.
	 *
	 * @param body the body to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if the message does not support changing its
	 *                                       body.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self body(@Nullable Body body) {
		this.message().setBody(body);
		return (Self) this;
	}

	/**
	 * Replace the body to the result of invoking the given {@code operator} with the
	 * argument being the previous body.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the message does not support changing its
	 *                                       body and the given {@code operator} returned
	 *                                       another body.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self body(@NotNull UnaryOperator<@Nullable Body> operator) {
		Objects.requireNonNull(operator, "operator");
		Body b = this.body();
		Body body = operator.apply(b);

		if (body != b)
			this.body(body);

		return (Self) this;
	}

	// Header

	/**
	 * Return the header with the given {@code name}.
	 *
	 * @param name the name of the header.
	 * @return the header with the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  HttpRegExp#FIELD_NAME}.
	 * @since 0.3.0 ~2021.11.20
	 */
	@Nullable
	@Contract(pure = true)
	default String header(@NotNull @Pattern(HttpRegExp.FIELD_NAME) String name) {
		return this.message().getHeaders().get(name);
	}

	/**
	 * Set the value of the attribute with the given {@code name} of the headers to the
	 * given {@code value}.
	 * <br>
	 * If the given {@code value} is null, the value will be removed.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @return this.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link HttpRegExp#FIELD_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       HttpRegExp#FIELD_VALUE}.
	 * @throws UnsupportedOperationException if the headers is unmodifiable.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Self header(@NotNull @Pattern(HttpRegExp.FIELD_NAME) String name, @Nullable @Pattern(HttpRegExp.FIELD_VALUE) String value) {
		if (value == null)
			this.message().getHeaders().remove(name);
		else
			this.message().getHeaders().put(name, value);
		return (Self) this;
	}

	/**
	 * Return the headers.
	 *
	 * @return the headers.
	 * @since 0.3.0 ~2021.11.20
	 */
	@NotNull
	@Contract(pure = true)
	default Headers headers() {
		return this.message().getHeaders();
	}

	/**
	 * Set the headers to the given {@code headers}.
	 *
	 * @param headers the headers to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code headers} is null.
	 * @throws UnsupportedOperationException if the message does not support changing its
	 *                                       headers.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self headers(@NotNull Headers headers) {
		this.message().setHeaders(headers);
		return (Self) this;
	}

	/**
	 * Invoke the given {@code operator} with the current headers as its parameter.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the operator to be invoked.
	 * @return this.
	 * @throws NullPointerException if the given {@code operator} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Self headers(@NotNull Consumer<@NotNull Headers> operator) {
		Objects.requireNonNull(operator, "operator");
		operator.accept(this.headers());
		return (Self) this;
	}
}
