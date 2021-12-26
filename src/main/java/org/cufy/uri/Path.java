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
package org.cufy.uri;

import org.cufy.internal.syntax.UriPattern;
import org.cufy.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Constant</b> (PCT Encode)
 * <br>
 * The "Path" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Path implements Serializable {
	/**
	 * Unspecified path constant.
	 *
	 * @since 0.1.0 ~2021.08.17
	 */
	public static final Path UNSPECIFIED = new Path("");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = -5564283348900202148L;

	/**
	 * The path literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.PATH)
	protected final String value;

	/**
	 * Construct a new default-implementation path component with its path literal being
	 * the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source of the path literal of the constructed path component.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public Path(@NotNull @Pattern(UriRegExp.PATH) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Construct a new default-implementation path component with its path literal being
	 * the given {@code source}.
	 *
	 * @param source the source of the path literal of the constructed path component.
	 * @return a new path from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#PATH}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Path parse(@NotNull @Pattern(UriRegExp.PATH) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.PATH.matcher(source).matches())
			throw new IllegalArgumentException("invalid path: " + source);
		return new Path(source);
	}

	/**
	 * Two paths are equal when they are the same instance or have the same {@code
	 * path-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a path and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Path) {
			Path path = (Path) object;

			return Objects.equals(this.value, path.toString());
		}

		return false;
	}

	/**
	 * The hash code of a path is the hash code of its path-literal. (optional)
	 *
	 * @return the hash code of this path.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

	/**
	 * A string representation of the Path. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Example:
	 * <pre>
	 *     /forum/questions/
	 * </pre>
	 *
	 * @return a string representation of the Path.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Contract(pure = true)
	@Pattern(UriRegExp.PATH)
	@Override
	public String toString() {
		return this.value;
	}
}
