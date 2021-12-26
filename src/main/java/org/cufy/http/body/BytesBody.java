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
import org.cufy.internal.syntax.HttpRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A byte array implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class BytesBody extends Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2616783801106938511L;

	/**
	 * The value of this body.
	 *
	 * @since 0.0.1 ~2021.03.30
	 */
	protected byte @NotNull [] bytes;

	/**
	 * Construct a new bytes body.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public BytesBody() {
		this.contentType = null;
		//noinspection ZeroLengthArrayAllocation
		this.bytes = new byte[0];
	}

	/**
	 * Construct a new body from the given components.
	 *
	 * @param bytes the value of the constructed body.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public BytesBody(byte @NotNull [] bytes) {
		Objects.requireNonNull(bytes, "value");
		this.contentType = null;
		this.bytes = bytes.clone();
	}

	/**
	 * Construct a new body from the given components.
	 *
	 * @param contentType the content-type of the constructed body.
	 * @param bytes       the value of the constructed body.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public BytesBody(@Nullable @Pattern(HttpRegExp.FIELD_VALUE) String contentType, byte @NotNull [] bytes) {
		Objects.requireNonNull(bytes, "value");
		this.contentType = contentType;
		this.bytes = bytes.clone();
	}

	/**
	 * Construct a new bytes body with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new bytes body.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	public BytesBody(@NotNull Consumer<@NotNull BytesBody> builder) {
		Objects.requireNonNull(builder, "builder");
		this.contentType = null;
		//noinspection ZeroLengthArrayAllocation
		this.bytes = new byte[0];
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	@NotNull
	@Override
	public BytesBody clone() {
		BytesBody clone = (BytesBody) super.clone();
		clone.bytes = this.bytes.clone();
		return clone;
	}

	/**
	 * Two abstract bodies are equal if they are the same object or have an equal {@link
	 * #contentType} and {@link #bytes}.
	 *
	 * @param object {@inheritDoc}
	 * @return {@inheritDoc}
	 * @since 0.3.0 ~2021.11.15
	 */
	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof BytesBody) {
			BytesBody body = (BytesBody) object;

			return Objects.equals(this.contentType, body.contentType) &&
				   Arrays.equals(this.bytes, body.bytes);
		}

		return false;
	}

	@Override
	@Range(from = 0, to = Long.MAX_VALUE)
	public long getContentLength() {
		return this.bytes.length;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.bytes) ^
			   Objects.hashCode(this.contentType);
	}

	@NotNull
	@Override
	public InputStream openInputStream() {
		return new ByteArrayInputStream(this.bytes);
	}

	@NotNull
	@Override
	public String toString() {
		return new String(this.bytes);
	}

	/**
	 * Return the bytes array.
	 *
	 * @return the bytes.
	 * @since 0.3.0 ~2021.11.18
	 */
	@Contract(pure = true)
	public byte @NotNull [] getBytes() {
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		return this.bytes;
	}

	/**
	 * Set the bytes to the given {@code bytes}.
	 *
	 * @param bytes the bytes to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code bytes} is null.
	 * @since 0.3.0 ~2021.11.18
	 */
	@NotNull
	@Contract(value = "_->new", mutates = "this")
	public BytesBody setBytes(byte @NotNull [] bytes) {
		Objects.requireNonNull(bytes, "bytes");
		//noinspection AssignmentOrReturnOfFieldWithMutableType
		this.bytes = bytes;
		return this;
	}
}
