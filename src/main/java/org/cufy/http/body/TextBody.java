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

import org.cufy.http.syntax.HTTPRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * <b>Appendable</b>
 * <br>
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
	protected StringBuilder value;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new empty body.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	public TextBody() {
		this.value = new StringBuilder();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new text-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #contentType()} of the given
	 * {@code body} and might not have the exact content. (the content might get
	 * reformatted, rearranged, compressed, encoded or encrypted/decrypted)
	 *
	 * @param body the body to be copied.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public TextBody(@NotNull Body body) {
		Objects.requireNonNull(body, "body");
		this.value = new StringBuilder(body.toString());
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its content set from the given {@code object}.
	 *
	 * @param object the content of the constructed body.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public TextBody(@NotNull Object object) {
		Objects.requireNonNull(object, "object");
		this.value = new StringBuilder(object.toString());
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new text-body from parsing the given {@code source}.
	 *
	 * @param source the text to construct this body from.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public TextBody(@NotNull @NonNls String source) {
		Objects.requireNonNull(source, "source");
		this.value = new StringBuilder(source);
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new text-body copying the given {@code body}.
	 * <br>
	 * Note: The constructed body will NOT have the {@link #contentType()} of the given
	 * {@code body} and might not have the exact content. (the content might get
	 * reformatted, rearranged, compressed, encoded or encrypted/decrypted)
	 *
	 * @param body the body to be copied.
	 * @return a new text body from copying the given {@code body}.
	 * @throws NullPointerException if the given {@code body} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public static TextBody copy(@NotNull TextBody body) {
		return new TextBody(body);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new empty body.
	 *
	 * @return a new default text body.
	 * @since 0.0.6 ~2021.03.29
	 */
	public static TextBody defaultBody() {
		return new TextBody();
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new body with its content set from the given {@code object}.
	 *
	 * @param object the content of the constructed body.
	 * @return a new body from the given {@code object}.
	 * @throws NullPointerException if the given {@code object} is null.
	 * @since 0.0.6 ~2021.03.29
	 */
	public static TextBody from(@NotNull Object object) {
		return new TextBody(object);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new text-body from parsing the given {@code source}.
	 *
	 * @param source the text to construct this body from.
	 * @return a new text body from parsing the given {@code source}.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.30
	 */
	public static TextBody parse(@NotNull @NonNls String source) {
		return new TextBody(source);
	}

	@NotNull
	@Override
	public TextBody clone() {
		try {
			TextBody clone = (TextBody) super.clone();
			clone.value = new StringBuilder(this.value);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@NonNls
	@Pattern(HTTPRegExp.FIELD_VALUE)
	@Override
	public String contentType() {
		return "text/plain; charset=utf-8";
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof TextBody) {
			TextBody body = (TextBody) object;

			//noinspection NonFinalFieldReferenceInEquals
			return this.value.toString().equals(body.value.toString());
		}
		if (object instanceof Body) {
			Body body = (Body) object;

			return Objects.equals(this.contentType(), body.contentType()) &&
				   Objects.equals(this.toString(), object.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.value.hashCode();
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return this.value.toString();
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
				this.value.append(c);
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
		String c = this.value.toString();
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
		this.value = new StringBuilder();
		for (Object c : content)
			if (c != null)
				this.value.append(c);
		return this;
	}
}
