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
package org.cufy.http.model;

import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * <b>Components</b>
 * <br>
 * A modifiable (optional) representation of an uri.
 * <br>
 * Components:
 * <ol>
 *     <li>{@link Scheme}</li>
 *     <li>{@link Authority}</li>
 *     <li>{@link Path}</li>
 *     <li>{@link Query}</li>
 *     <li>{@link Fragment}</li>
 * </ol>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.20
 */
public interface Uri extends Cloneable, Serializable {
	/**
	 * Replace the authority of this to be the result of invoking the given {@code
	 * operator} with the argument being the current authority. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority and the given {@code operator}
	 *                                       returned another authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri authority(@NotNull UnaryOperator<Authority> operator) {
		Objects.requireNonNull(operator, "operator");
		Authority a = this.getAuthority();
		Authority authority = operator.apply(a);

		if (authority != null && authority != a)
			this.setAuthority(authority);

		return this;
	}

	/**
	 * Replace the fragment of this to be the result of invoking the given {@code
	 * operator} with the argument being the current fragment. If the {@code operator}
	 * returned {@code null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment and the given {@code operator}
	 *                                       returned another fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri fragment(@NotNull UnaryOperator<Fragment> operator) {
		Objects.requireNonNull(operator, "operator");
		Fragment f = this.getFragment();
		Fragment fragment = operator.apply(f);

		if (fragment != null && fragment != f)
			this.setFragment(fragment);

		return this;
	}

	/**
	 * Replace the path of this to be the result of invoking the given {@code operator}
	 * with the argument being the current path. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path and the given {@code operator} returned
	 *                                       another path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri path(@NotNull UnaryOperator<Path> operator) {
		Objects.requireNonNull(operator, "operator");
		Path p = this.getPath();
		Path path = operator.apply(p);

		if (path != null && path != p)
			this.setPath(path);

		return this;
	}

	/**
	 * Replace the query of this to be the result of invoking the given {@code operator}
	 * with the argument being the current query. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query and the given {@code operator} returned
	 *                                       another query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri query(@NotNull UnaryOperator<Query> operator) {
		Objects.requireNonNull(operator, "operator");
		Query q = this.getQuery();
		Query query = operator.apply(q);

		if (query != null && query != q)
			this.setQuery(query);

		return this;
	}

	/**
	 * Replace the scheme of this to be the result of invoking the given {@code operator}
	 * with the argument being the current scheme. If the {@code operator} returned {@code
	 * null} then nothing happens.
	 * <br>
	 * Any exceptions thrown by the given {@code operator} will fall throw this method
	 * unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme and the given {@code operator}
	 *                                       returned another scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri scheme(@NotNull UnaryOperator<Scheme> operator) {
		Objects.requireNonNull(operator, "operator");
		Scheme s = this.getScheme();
		Scheme scheme = operator.apply(s);

		if (scheme != null && scheme != s)
			this.setScheme(scheme);

		return this;
	}

	/**
	 * Set the authority of this from the given {@code authority}.
	 *
	 * @param authority the authority to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setAuthority(@NotNull Authority authority) {
		throw new UnsupportedOperationException("authority");
	}

	/**
	 * Set the fragment of this from the given {@code fragment}.
	 *
	 * @param fragment the fragment to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setFragment(@NotNull Fragment fragment) {
		throw new UnsupportedOperationException("fragment");
	}

	/**
	 * Set the path of this from the given {@code path}.
	 *
	 * @param path the path to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setPath(@NotNull Path path) {
		throw new UnsupportedOperationException("path");
	}

	/**
	 * Set the query of this from the given {@code query}.
	 *
	 * @param query the query to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setQuery(@NotNull Query query) {
		throw new UnsupportedOperationException("query");
	}

	/**
	 * Set the scheme of this from the given {@code scheme}.
	 *
	 * @param scheme the scheme to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Uri setScheme(@NotNull Scheme scheme) {
		throw new UnsupportedOperationException("scheme");
	}

	/**
	 * Capture this uri into a new object.
	 *
	 * @return a clone of this uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	Uri clone();

	/**
	 * Two uris are equal when they are the same instance or have an equal {@link
	 * #getScheme()}, {@link #getAuthority()}, {@link #getPath()}, {@link #getQuery()} and
	 * {@link #getFragment()}.
	 *
	 * @param object the object to be checked.
	 * @return if the given {@code object} is an uri and equals this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(value = "null->false", pure = true)
	boolean equals(@Nullable Object object);

	/**
	 * The hash code of an uri is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this uri.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	int hashCode();

	/**
	 * A string representation of this Uri. Invoke to get the text representing this in a
	 * request.
	 * <br>
	 * Typically:
	 * <pre>
	 *     Scheme://Authority/Path?Query#Fragment
	 * </pre>
	 * Example:
	 * <pre>
	 *     https://john.doe@www.example.com:123/forum/questions/?tag=networking&order=newest#top
	 * </pre>
	 *
	 * @return a string representation of this Uri.
	 * @since 0.0.1 ~2021.03.20
	 */
	@NotNull
	@Pattern(UriRegExp.URI)
	@Contract(pure = true)
	@Override
	String toString();

	/**
	 * Return the authority defined for this.
	 *
	 * @return the authority of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Authority getAuthority();

	/**
	 * Return the fragment defined for this.
	 *
	 * @return the fragment of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Fragment getFragment();

	/**
	 * Return the path defined for this.
	 *
	 * @return the path of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Path getPath();

	/**
	 * Return the query defined for this.
	 *
	 * @return the query of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Query getQuery();

	/**
	 * Return the scheme defined for this.
	 *
	 * @return the scheme of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	Scheme getScheme();
}
