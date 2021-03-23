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
package org.cufy.http.uri;

import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b>
 * <br>
 * The "Path" part of an URI.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Path extends Serializable {
	/**
	 * An empty path constant.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	Path EMPTY = new AbstractPath("");

	/**
	 * Return a path instance to be a placeholder if a the user has not specified a path.
	 *
	 * @return an empty path.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Path defaultPath() {
		return Path.EMPTY;
	}

	/**
	 * Construct a new default-implementation path component with its path literal being
	 * the given {@code source}.
	 *
	 * @param source the source of the path literal of the constructed path component.
	 * @return a new path from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#PATH}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Path parse(@NotNull @NonNls @Pattern(URIRegExp.PATH) @Subst("/search") String source) {
		return new AbstractPath(source);
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
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a path is the hash code of its path-literal. (optional)
	 *
	 * @return the hash code of this path.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

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
	@NonNls
	@Contract(pure = true)
	@Pattern(URIRegExp.PATH)
	@Override
	String toString();
}
