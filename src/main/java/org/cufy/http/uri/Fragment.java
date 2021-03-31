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
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * <b>Constant</b>
 * <br>
 * The "Fragment" part of an URI.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Fragment extends Serializable {
	/**
	 * The default fragment constant.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	Fragment DEFAULT = new AbstractFragment();
	/**
	 * An empty fragment constant.
	 *
	 * @since 0.0.6 ~2021.03.21
	 */
	Fragment EMPTY = new RawFragment();

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a fragment instance to be a placeholder if a the user has not specified a
	 * fragment.
	 *
	 * @return the default fragment.
	 * @since 0.0.1 ~2021.03.20
	 */
	static Fragment defaultFragment() {
		return Fragment.DEFAULT;
	}

	/**
	 * <b>Empty</b>
	 * <br>
	 * Return an empty raw fragment.
	 *
	 * @return an empty raw fragment.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Fragment empty() {
		return Fragment.EMPTY;
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new default-implementation fragment component with its fragment literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the fragment literal of the constructed fragment
	 *               component.
	 * @return a new fragment from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#FRAGMENT}.
	 * @since 0.0.1 ~2021.03.21
	 */
	static Fragment parse(@NotNull @NonNls @Pattern(URIRegExp.FRAGMENT) String source) {
		return new AbstractFragment(source);
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw fragment with the given {@code value}.
	 *
	 * @param value the value of the constructed fragment.
	 * @return a new raw fragment.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	static Fragment raw(@NotNull @NonNls String value) {
		return new RawFragment(value);
	}

	/**
	 * Two fragments are equal when they are the same instance or have the same {@code
	 * fragment-literal} (the value returned from {@link #toString()}).
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is a fragment and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of a fragment is the hash code of its fragment-literal. (optional)
	 *
	 * @return the hash code of this fragment.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of the Fragment. Invoke to get the text representing this
	 * in a request.
	 * <br>
	 * Example:
	 * <pre>
	 *     top
	 * </pre>
	 *
	 * @return a string representation of the Fragment.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@NonNls
	@Pattern(URIRegExp.FRAGMENT)
	@Contract(pure = true)
	@Override
	String toString();
}
