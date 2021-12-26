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
package org.cufy.http.body;

import org.cufy.http.Body;
import org.cufy.uri.Query;
import org.cufy.internal.syntax.HttpRegExp;
import org.cufy.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A body implementation that follows the {@code application/x-www-form-urlencoded}
 * content type.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.29
 */
public class ParametersBody extends Body {
	/**
	 * The typical content type for a parameters body.
	 *
	 * @since 0.3.0 ~2021.11.18
	 */
	@Pattern(HttpRegExp.FIELD_VALUE)
	public static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=utf-8";
	/**
	 * A regex catching most typical parameter body mimes.
	 *
	 * @since 0.3.0 ~2021.11.24
	 */
	@Language("RegExp")
	public static final String CONTENT_TYPE_PATTERN = "^application\\/x-www-form-urlencoded.*$";

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -4568284231778109146L;

	/**
	 * The request in-body parameters. (if any)
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	protected Query query;

	/**
	 * Construct a new parameters body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public ParametersBody() {
		this.contentType = ParametersBody.CONTENT_TYPE;
		this.query = new Query();
	}

	/**
	 * Construct a new body with its values set to the given {@code query}.
	 *
	 * @param query the values of the constructed body.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.contentType = ParametersBody.CONTENT_TYPE;
		this.query = query;
	}

	/**
	 * Construct a new body with its values set to the given {@code query}.
	 *
	 * @param contentType the content type of the constructed body.
	 * @param query       the values of the constructed body.
	 * @throws NullPointerException if the given {@code query} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public ParametersBody(@Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType, @NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.contentType = contentType;
		this.query = query;
	}

	/**
	 * Construct a new parameters body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new parameters body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public ParametersBody(@NotNull Consumer<@NotNull ParametersBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.contentType = ParametersBody.CONTENT_TYPE;
		this.query = new Query();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new parameters body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new parameters body copy of the given {@code body}.
	 * @throws NullPointerException     if the given {@code body} is null.
	 * @throws IllegalArgumentException if the content of the given {@code body} is not
	 *                                  valid parameters.
	 * @throws IOError                  if any I/O occurs while reading the content of the
	 *                                  given {@code body}.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody from(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		try (InputStream stream = body.openInputStream()) {
			String string = new String(stream.readAllBytes());

			return new ParametersBody(
					body.getContentType(),
					Query.parse(string)
			);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	/**
	 * Construct a new body with its value set from the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @return a new parameters body from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#QUERY}.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static ParametersBody parse(@NotNull @Pattern(UriRegExp.QUERY) String source) {
		return new ParametersBody(
				ParametersBody.CONTENT_TYPE,
				Query.parse(source)
		);
	}

	@NotNull
	@Override
	public ParametersBody clone() {
		ParametersBody clone = (ParametersBody) super.clone();
		clone.query = this.query.clone();
		return clone;
	}

	/**
	 * Two parameters bodies are equal if they are the same object or have an equal {@link
	 * #query}.
	 *
	 * @param object {@inheritDoc}
	 * @return {@inheritDoc}
	 * @since 0.3.0 ~2021.11.15
	 */
	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof ParametersBody) {
			ParametersBody body = (ParametersBody) object;

			return Objects.equals(this.contentType, body.contentType) &&
				   Objects.equals(this.query, body.query);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.query.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(
				this.query.toString().getBytes(StandardCharsets.UTF_8)
		);
	}

	@NotNull
	@Override
	public String toString() {
		return this.query.toString();
	}

	/**
	 * Get the value assigned to the given {@code name}.
	 *
	 * @param name the name of the value to be returned. Or {@code null} if no such
	 *             value.
	 * @return the value assigned to the given {@code name}.
	 * @throws NullPointerException     if the given {@code name} is null.
	 * @throws IllegalArgumentException if the given {@code name} does not match {@link
	 *                                  UriRegExp#ATTR_NAME}.
	 * @since 0.0.6 ~2021.03.31
	 */
	@Nullable
	@Contract(pure = true)
	@Pattern(UriRegExp.ATTR_VALUE)
	public String get(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		return this.query.get(name);
	}

	/**
	 * Set the value of the attribute with the given {@code name} to the given {@code
	 * value}.
	 *
	 * @param name  the name of the attribute to be set.
	 * @param value the new value for to set to the attribute.
	 * @throws NullPointerException          if the given {@code name} or {@code value} is
	 *                                       null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}; if the given
	 *                                       {@code value} does not match {@link
	 *                                       UriRegExp#ATTR_VALUE}.
	 * @throws UnsupportedOperationException if query of this is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@Contract(mutates = "this")
	public void put(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name, @NotNull @Pattern(UriRegExp.ATTR_VALUE) String value) {
		this.query.put(name, value);
	}

	/**
	 * Remove the attribute with the given {@code name}.
	 *
	 * @param name the name of the attribute to be removed.
	 * @throws NullPointerException          if the given {@code name} is null.
	 * @throws IllegalArgumentException      if the given {@code name} does not match
	 *                                       {@link UriRegExp#ATTR_NAME}.
	 * @throws UnsupportedOperationException if query is unmodifiable.
	 * @since 0.0.6 ~2021.03.31
	 */
	@Contract(mutates = "this")
	public void remove(@NotNull @Pattern(UriRegExp.ATTR_NAME) String name) {
		this.query.remove(name);
	}
}
