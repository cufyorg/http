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
package org.cufy.http;

import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * <b>Constant</b> (PCT Encode)
 * <br>
 * The "Fragment" part of a Uri.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public class Fragment implements Serializable {
	/**
	 * Unspecified fragment constant.
	 *
	 * @since 0.1.0 ~2021.08.17
	 */
	public static final Fragment UNSPECIFIED = new Fragment("");

	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 6533793377008910315L;

	/**
	 * The fragment literal.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.FRAGMENT)
	protected final String value;

	/**
	 * Construct a new default-implementation fragment component with its fragment literal
	 * being the given {@code source}.
	 * <br>
	 * Note: No validation will be applied.
	 *
	 * @param source the source of the fragment literal of the constructed fragment
	 *               component.
	 * @throws NullPointerException if the given {@code source} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public Fragment(@NotNull @Pattern(UriRegExp.FRAGMENT) String source) {
		Objects.requireNonNull(source, "source");
		this.value = source;
	}

	/**
	 * Construct a new default-implementation fragment component with its fragment literal
	 * being the given {@code source}.
	 *
	 * @param source the source of the fragment literal of the constructed fragment
	 *               component.
	 * @return a new fragment from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#FRAGMENT}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Fragment parse(@NotNull @Pattern(UriRegExp.FRAGMENT) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.FRAGMENT.matcher(source).matches())
			throw new IllegalArgumentException("illegal fragment: " + source);
		return new Fragment(source);
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
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Fragment) {
			Fragment fragment = (Fragment) object;

			return Objects.equals(this.value, fragment.toString());
		}

		return false;
	}

	/**
	 * The hash code of a fragment is the hash code of its fragment-literal. (optional)
	 *
	 * @return the hash code of this fragment.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.value.hashCode();
	}

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
	@Pattern(UriRegExp.FRAGMENT)
	@Contract(pure = true)
	@Override
	public String toString() {
		return this.value;
	}
}