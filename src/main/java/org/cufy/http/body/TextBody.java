/*
 *	Copyright 2021-2022 Cufy
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
import org.cufy.http.internal.util.StreamUtil;
import org.cufy.http.mime.Mime;
import org.cufy.http.mime.MimeSubtype;
import org.cufy.http.mime.MimeType;
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
 * A body implementation for raw appendable text.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.29
 */
public class TextBody extends Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2001135441513755561L;

	/**
	 * The content of this body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	@SuppressWarnings("StringBufferField")
	@NotNull
	protected StringBuilder value;

	/**
	 * Construct a new text-body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	public TextBody() {
		this.mime = new Mime(
				MimeType.TEXT,
				MimeSubtype.PLAIN
		);
		this.value = new StringBuilder();
	}

	/**
	 * Construct a new text-body with the given {@code value}.
	 *
	 * @param value the text to construct the body with.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public TextBody(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.mime = new Mime(
				MimeType.TEXT,
				MimeSubtype.PLAIN
		);
		this.value = new StringBuilder(value);
	}

	/**
	 * Construct a new text-body with the given {@code value}.
	 *
	 * @param mime  the mime of the constructed body.
	 * @param value the text to construct the body with.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public TextBody(@Nullable Mime mime, @NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.mime = mime;
		this.value = new StringBuilder(value);
	}

	/**
	 * Construct a new text body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new text body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public TextBody(@NotNull Consumer<@NotNull TextBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.mime = new Mime(
				MimeType.TEXT,
				MimeSubtype.PLAIN
		);
		this.value = new StringBuilder();
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct a new text body from copying the given {@code body}.
	 *
	 * @param body the body to copy.
	 * @return a new text body copy of the given {@code body}.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @throws IOError              if any I/O occurs while reading the content of the
	 *                              given {@code body}.
	 * @since 0.3.0 ~2021.11.24
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static TextBody from(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		try (InputStream stream = body.openInputStream()) {
			String string = new String(StreamUtil.readAllBytes(stream));

			return new TextBody(
					body.getMime(),
					string
			);
		} catch (IOException e) {
			throw new IOError(e);
		}
	}

	@NotNull
	@Override
	public TextBody clone() {
		TextBody clone = (TextBody) super.clone();
		if (this.mime != null)
			this.mime = this.mime.clone();
		clone.value = new StringBuilder(this.value);
		return clone;
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof TextBody) {
			TextBody body = (TextBody) object;

			return Objects.equals(this.mime, body.mime) &&
				   Objects.equals(this.value.toString(), body.value.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(
				this.value.toString().getBytes(StandardCharsets.UTF_8)
		);
	}

	@NotNull
	@Override
	public String toString() {
		return this.value.toString();
	}

	/**
	 * Append the given {@code content} to the content of this body.
	 *
	 * @param content the content to be appended.
	 * @throws NullPointerException          if the given {@code content} is null.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       appended.
	 * @since 0.0.6 ~2021.03.29
	 */
	@Contract(mutates = "this")
	public void append(@Nullable Object @NotNull ... content) {
		Objects.requireNonNull(content, "content");
		for (Object c : content)
			if (c != null)
				this.value.append(c);
	}
}
