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

import org.cufy.http.model.Path;
import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Path}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class PathImpl implements Path {
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
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation path component with its path literal being
	 * the given {@code source}.
	 *
	 * @param source the source of the path literal of the constructed path component.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#PATH}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public PathImpl(@NotNull @Pattern(UriRegExp.PATH) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.PATH.matcher(source).matches())
			throw new IllegalArgumentException("invalid path: " + source);
		this.value = source;
	}

	/**
	 * <b>Parse</b>
	 * <br>
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
	public static Path path(@NotNull @Pattern(UriRegExp.PATH) String source) {
		return new PathImpl(source);
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Path) {
			Path path = (Path) object;

			return Objects.equals(this.value, path.toString());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(UriRegExp.PATH)
	@Override
	public String toString() {
		return this.value;
	}
}
