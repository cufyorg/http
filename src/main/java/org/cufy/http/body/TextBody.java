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

import org.jetbrains.annotations.*;

import java.util.Objects;
import java.util.function.Function;

/**
 * A body implementation for raw appendable text.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.29
 */
public class TextBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 2001135441513755561L;

	/**
	 * The content of this body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	@SuppressWarnings("StringBufferField")
	@NotNull
	protected StringBuilder content = new StringBuilder();

	/**
	 * Construct a new empty body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public TextBody() {

	}

	/**
	 * Construct a new body with its content set from the given {@code content}.
	 *
	 * @param content the content of the constructed body.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public TextBody(@NotNull Object content) {
		Objects.requireNonNull(content, "content");
		this.content = new StringBuilder(content.toString());
	}

	@NotNull
	@Override
	public TextBody clone() {
		try {
			TextBody clone = (TextBody) super.clone();
			clone.content = new StringBuilder(this.content);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof TextBody) {
			TextBody body = (TextBody) object;

			//noinspection NonFinalFieldReferenceInEquals
			return this.content.toString().equals(body.content.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.content.hashCode();
	}

	@Range(from = 0, to = Long.MAX_VALUE)
	@Override
	public long length() {
		return this.content.codePoints()
				.map(cp -> cp <= 0x7ff ? cp <= 0x7f ? 1 : 2 : cp <= 0xffff ? 3 : 4)
				.sum();
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return this.content.toString();
	}

	/**
	 * Append the given {@code content} to the content of this body.
	 *
	 * @param content the content to be appended.
	 * @return this.
	 * @throws NullPointerException          if the given {@code content} is null.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       appended.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public TextBody append(@Nullable Object @NotNull ... content) {
		Objects.requireNonNull(content, "content");
		for (Object c : content)
			if (c != null)
				this.content.append(c);
		return this;
	}

	/**
	 * Replace the content of this body to the result of invoking the given {@code
	 * operator} with the current content of this. If the {@code operator} returned {@code
	 * null} then the body will be set to an empty string.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       overwritten/appended and the returned
	 *                                       parameters from the given {@code operator} is
	 *                                       different from the current parameters;
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public TextBody compute(@NotNull Function<@NonNls String, @Nullable Object> operator) {
		Objects.requireNonNull(operator, "operator");
		String c = this.content.toString();
		Object content = operator.apply(c);

		if (content == null)
			this.write("");
		else {
			String text = content.toString();

			if (!c.equals(text))
				this.write(text);
		}

		return this;
	}

	/**
	 * Set the content of this body to be the given {@code content}.
	 *
	 * @param content the content to be set.
	 * @return this.
	 * @throws UnsupportedOperationException if the content of this body cannot be
	 *                                       overwritten.
	 * @since 0.0.6 ~2021.03.29
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	public TextBody write(@Nullable Object @NotNull ... content) {
		Objects.requireNonNull(content, "content");
		this.content = new StringBuilder();
		for (Object c : content)
			if (c != null)
				this.content.append(c);
		return this;
	}
}
