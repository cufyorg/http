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
package org.cufy.http.impl;

import org.cufy.http.model.Body;
import org.cufy.http.syntax.HttpPattern;
import org.cufy.http.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.ByteArrayInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
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
public class BodyImpl implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2616783801106938511L;

	/**
	 * The content type of this body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	protected final String contentType;
	/**
	 * The value of this body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	protected final byte @NotNull [] value;

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @throws IOError              if any I/O error occurs while reading the content of
	 *                              the given {@code body}.
	 * @since 0.0.6 ~2021.03.30
	 */
	public BodyImpl(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		try (InputStream is = body.openInputStream()) {
			this.value = is.readAllBytes();
			this.contentType = body.getContentType();
		} catch (IOException e) {
			throw new IOError(e);
		}
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
	 *                                  and does not match {@link HttpRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	public BodyImpl(byte @NotNull [] value, @Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType) {
		Objects.requireNonNull(value, "value");
		if (contentType != null &&
			!HttpPattern.FIELD_VALUE.matcher(contentType).matches())
			throw new IllegalArgumentException("illegal content type: " + contentType);
		this.value = value.clone();
		this.contentType = contentType;
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new copy of the given {@code body}.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Body body(@NotNull Body body) {
		return new BodyImpl(body);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new body from the given components.
	 *
	 * @param value       the value of the constructed body.
	 * @param contentType the content-type of the constructed body.
	 * @return a new body from the given components.
	 * @throws NullPointerException     if the given {@code value} is null.
	 * @throws IllegalArgumentException if the given {@code contentType} is both not null
	 *                                  and does not match {@link HttpRegExp#FIELD_VALUE}.
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@Contract(value = "_,_->new", pure = true)
	public static Body body(byte @NotNull [] value, @Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType) {
		return new BodyImpl(value, contentType);
	}

	@NotNull
	@Override
	public BodyImpl clone() {
		try {
			return (BodyImpl) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	/**
	 * Two abstract bodies are equal if they are the same object or have an equal {@link
	 * #contentType} and {@link #value}.
	 *
	 * @param object {@inheritDoc}
	 * @return {@inheritDoc}
	 * @since 0.3.0 ~2021.11.15
	 */
	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof BodyImpl) {
			BodyImpl body = (BodyImpl) object;

			return Objects.equals(this.contentType, body.contentType) &&
				   Arrays.equals(this.value, body.value);
		}

		return false;
	}

	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getContentLength() {
		return this.value.length;
	}

	@Nullable
	@Pattern(HttpRegExp.FIELD_VALUE)
	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.value) ^
			   Objects.hashCode(this.contentType);
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(this.value);
	}

	@NotNull
	@Override
	public String toString() {
		return new String(this.value);
	}
}
