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
package org.cufy.http.component;

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Body}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.22
 */
public class AbstractBody implements Body {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 5239809259228299444L;

	/**
	 * The content of this body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@SuppressWarnings("StringBufferField")
	@NotNull
	protected StringBuilder content = new StringBuilder();
	/**
	 * The request in-body parameters. (if any)
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	@NotNull
	protected Query parameters = Query.defaultQuery();

	/**
	 * Construct a new empty body.
	 *
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractBody() {

	}

	/**
	 * Construct a new body with its content set from the given {@code content}.
	 *
	 * @param content the content of the constructed body.
	 * @throws NullPointerException if the given {@code content} is null.
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractBody(@NotNull @NonNls Object content) {
		Objects.requireNonNull(content, "content");
		this.content = new StringBuilder(content.toString());
	}

	/**
	 * Construct a new body with its content set from the given {@code content} and its
	 * parameters from the given {@code parameters}.
	 *
	 * @param content    the content of the constructed body.
	 * @param parameters the parameters of the constructed body.
	 * @throws NullPointerException     if the given {@code content} or {@code parameters}
	 *                                  is null.
	 * @throws IllegalArgumentException if the given {@code parameters} does not match
	 *                                  {@link URIRegExp#QUERY}.
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractBody(@NotNull @NonNls Object content, @Nullable @NonNls @Pattern(URIRegExp.QUERY) @Subst("q=parameters") String parameters) {
		Objects.requireNonNull(content, "content");
		Objects.requireNonNull(parameters, "parameters");
		this.content = new StringBuilder(content.toString());
		this.parameters = Query.parse(parameters);
	}

	/**
	 * Construct a new body with its content set from the given {@code content} and its
	 * parameters from combining the given {@code parameters}.
	 *
	 * @param content    the content of the constructed body.
	 * @param parameters the parameters segments of the constructed body.
	 * @throws NullPointerException     if the given {@code content} or {@code parameters}
	 *                                  is null.
	 * @throws IllegalArgumentException if an element in the given {@code parameters} does
	 *                                  not match {@link URIRegExp#ATTR_VALUE}.
	 * @since 0.0.1 ~2021.03.22
	 */
	public AbstractBody(@NotNull @NonNls Object content, @Nullable @NonNls @Pattern(URIRegExp.ATTR_VALUE) String @NotNull ... parameters) {
		Objects.requireNonNull(content, "content");
		Objects.requireNonNull(parameters, "parameters");
		this.content = new StringBuilder(content.toString());
		this.parameters = Query.parse(parameters);
	}

	@NotNull
	@Override
	public Body append(@NotNull @NonNls Object... content) {
		Objects.requireNonNull(content, "content");
		for (Object c : content)
			this.content.append(c);
		return this;
	}

	@NotNull
	@Override
	public AbstractBody clone() {
		try {
			AbstractBody clone = (AbstractBody) super.clone();
			clone.parameters = this.parameters.clone();
			clone.content = new StringBuilder(this.content);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@NonNls
	@Override
	public String content() {
		return this.content.toString();
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Body) {
			Body body = (Body) object;

			//noinspection NonFinalFieldReferenceInEquals
			return Objects.equals(this.content.toString(), body.content()) &&
				   Objects.equals(this.parameters, body.parameters());
		}

		return false;
	}

	@Override
	public int hashCode() {
		//noinspection NonFinalFieldReferencedInHashCode
		return this.content.hashCode() ^
			   this.parameters.hashCode();
	}

	@Range(from = 0, to = Long.MAX_VALUE)
	@Override
	public long length() {
		return this.toString().codePoints()
				.map(cp -> cp <= 0x7ff ? cp <= 0x7f ? 1 : 2 : cp <= 0xffff ? 3 : 4)
				.sum();
	}

	@NotNull
	@Override
	public Query parameters() {
		return this.parameters;
	}

	@NotNull
	@Override
	public Body parameters(@NotNull Query parameters) {
		Objects.requireNonNull(parameters, "parameters");
		this.parameters = parameters;
		return this;
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		String content = this.content.toString();
		String parameters = this.parameters.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(content);

		if (!parameters.isEmpty()) {
			if (!content.isEmpty())
				builder.append("\r\n");

			builder.append(parameters);
		}

		return builder.toString();
	}

	@NotNull
	@Override
	public Body write(@NotNull Object... content) {
		Objects.requireNonNull(content, "content");
		this.content = new StringBuilder();
		for (Object c : content)
			this.content.append(c);
		return this;
	}
}
