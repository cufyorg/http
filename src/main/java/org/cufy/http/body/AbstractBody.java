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

import org.cufy.http.syntax.HTTPPattern;
import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * <b>Constant</b>
 * <br>
 * A basic implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class AbstractBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2616783801106938511L;

	/**
	 * The content type of this body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	@Nullable
	@Pattern(HTTPRegExp.FIELD_VALUE)
	protected final String contentType;
	/**
	 * The value of this body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	@NotNull
	protected final String value;

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractBody(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		this.value = body.toString();
		this.contentType = body.contentType();
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body from the given {@code object}.
	 *
	 * @param object the object to create a body from.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractBody(@NotNull Object object) {
		Objects.requireNonNull(object, "object");
		this.value = object.toString();
		this.contentType = null;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new body from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed body.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractBody(@NotNull String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
		this.contentType = null;
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body from the given components.
	 *
	 * @param value       the value of the constructed body.
	 * @param contentType the content-type of the constructed body.
	 * @throws NullPointerException     if the given {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code contentType} is both not null
	 *                                  and does not match {@link HTTPRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractBody(@NotNull String value, @Nullable @Pattern(HTTPRegExp.FIELD_VALUE) String contentType) {
		Objects.requireNonNull(value, "value");
		if (contentType != null &&
			!HTTPPattern.FIELD_VALUE.matcher(contentType).matches())
			throw new IllegalArgumentException("illegal content type: " + contentType);
		this.value = value;
		this.contentType = contentType;
	}

	@NotNull
	@Override
	public AbstractBody clone() {
		try {
			return (AbstractBody) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Nullable
	@Pattern(HTTPRegExp.FIELD_VALUE)
	@Override
	public String contentType() {
		return this.contentType;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Body) {
			Body body = (Body) object;

			return Objects.equals(this.contentType, body.contentType()) &&
				   Objects.equals(this.value, body.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode() ^
			   Objects.hashCode(this.contentType);
	}

	@NotNull
	@Override
	public String toString() {
		return this.value;
	}
}
