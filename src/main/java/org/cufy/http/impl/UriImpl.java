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

import org.cufy.http.model.*;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Uri}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class UriImpl implements Uri {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4830745156508565988L;

	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Authority authority;
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Fragment fragment;
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Path path;
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Query query;
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Scheme scheme;

	/**
	 * Construct a new uri from the given components.
	 *
	 * @param scheme    the scheme of the constructed uri.
	 * @param authority the authority of the constructed uri.
	 * @param path      the path of the constructed uri.
	 * @param query     the query of the constructed uri.
	 * @param fragment  the fragment of the constructed uri.
	 * @throws NullPointerException if the given {@code scheme} or {@code authority} or
	 *                              {@code path} or {@code query} or {@code fragment} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.30
	 */
	@ApiStatus.Internal
	public UriImpl(@NotNull Scheme scheme, @NotNull Authority authority, @NotNull Path path, @NotNull Query query, @NotNull Fragment fragment) {
		Objects.requireNonNull(scheme, "scheme");
		Objects.requireNonNull(authority, "authority");
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(query, "query");
		Objects.requireNonNull(fragment, "fragment");
		this.scheme = scheme;
		this.authority = authority;
		this.path = path;
		this.query = query;
		this.fragment = fragment;
	}

	@NotNull
	@Override
	public UriImpl clone() {
		try {
			UriImpl clone = (UriImpl) super.clone();
			clone.authority = this.authority.clone();
			clone.query = this.query.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof Uri) {
			Uri uri = (Uri) object;

			return Objects.equals(this.scheme, uri.getScheme()) &&
				   Objects.equals(this.authority, uri.getAuthority()) &&
				   Objects.equals(this.path, uri.getPath()) &&
				   Objects.equals(this.query, uri.getQuery()) &&
				   Objects.equals(this.fragment, uri.getFragment());
		}

		return false;
	}

	@NotNull
	@Override
	public Authority getAuthority() {
		return this.authority;
	}

	@NotNull
	@Override
	public Fragment getFragment() {
		return this.fragment;
	}

	@NotNull
	@Override
	public Path getPath() {
		return this.path;
	}

	@NotNull
	@Override
	public Query getQuery() {
		return this.query;
	}

	@NotNull
	@Override
	public Scheme getScheme() {
		return this.scheme;
	}

	@Override
	public int hashCode() {
		return this.scheme.hashCode() ^
			   this.authority.hashCode() ^
			   this.path.hashCode() ^
			   this.query.hashCode() ^
			   this.fragment.hashCode();
	}

	@NotNull
	@Override
	public Uri setAuthority(@NotNull Authority authority) {
		Objects.requireNonNull(authority, "authority");
		this.authority = authority;
		return this;
	}

	@NotNull
	@Override
	public Uri setFragment(@NotNull Fragment fragment) {
		Objects.requireNonNull(fragment, "fragment");
		this.fragment = fragment;
		return this;
	}

	@NotNull
	@Override
	public Uri setPath(@NotNull Path path) {
		Objects.requireNonNull(path, "path");
		this.path = path;
		return this;
	}

	@NotNull
	@Override
	public Uri setQuery(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.query = query;
		return this;
	}

	@NotNull
	@Override
	public Uri setScheme(@NotNull Scheme scheme) {
		Objects.requireNonNull(scheme, "scheme");
		this.scheme = scheme;
		return this;
	}

	@NotNull
	@Pattern(UriRegExp.URI)
	@Override
	public String toString() {
		String scheme = this.scheme.toString();
		String authority = this.authority.toString();
		String path = this.path.toString();
		String query = this.query.toString();
		String fragment = this.fragment.toString();

		StringBuilder builder = new StringBuilder();

		builder.append(scheme)
			   .append(":");

		if (!authority.isEmpty())
			builder.append("//")
				   .append(authority);

		if (!path.startsWith("/"))
			builder.append("/");

		builder.append(path);

		if (!query.isEmpty())
			builder.append("?")
				   .append(query);

		if (!fragment.isEmpty())
			builder.append("#")
				   .append(fragment);

		return builder.toString();
	}
}
