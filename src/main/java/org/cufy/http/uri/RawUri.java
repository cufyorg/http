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

import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link Uri}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawUri implements Uri {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4830745156508565988L;

	/**
	 * The authority to be returned by {@link #getAuthority()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Authority authority;
	/**
	 * The fragment to be returned by {@link #getFragment()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Fragment fragment;
	/**
	 * The path to be returned by {@link #getPath()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Path path;
	/**
	 * The query to be returned by {@link #getQuery()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Query query;
	/**
	 * The scheme to be returned by {@link #getScheme()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Scheme scheme;
	/**
	 * The raw source of this.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw uri.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUri() {
		this.value = "";
		this.scheme = Scheme.EMPTY;
		this.authority = Authority.EMPTY;
		this.path = Path.EMPTY;
		this.query = Query.EMPTY;
		this.fragment = Fragment.EMPTY;
	}

	/**
	 * <b>Unmodifiable</b>
	 * <br>
	 * Construct an unmodifiable copy of the given {@code uri}.
	 *
	 * @param uri the uri to be copied.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUri(@NotNull Uri uri) {
		Objects.requireNonNull(uri, "uri");
		this.value = uri.toString();
		this.scheme = uri.getScheme();
		this.authority = Authority.raw(uri.getAuthority());
		this.path = uri.getPath();
		this.query = Query.raw(uri.getQuery());
		this.fragment = uri.getFragment();
	}

	/**
	 * <b>Raw</b>
	 * <br>
	 * Construct a new raw uri with the given {@code value}.
	 *
	 * @param value the value of the constructed uri.
	 * @throws NullPointerException if the given {@code value} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUri(@NotNull String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.scheme = Scheme.EMPTY;
		this.authority = Authority.EMPTY;
		this.path = Path.EMPTY;
		this.query = Query.EMPTY;
		this.fragment = Fragment.EMPTY;
	}

	/**
	 * <b>Raw + Components</b>
	 * <br>
	 * Construct a new raw uri with the given {@code value}.
	 *
	 * @param value     the value of the constructed uri.
	 * @param scheme    the scheme of the constructed uri.
	 * @param authority the authority of the constructed uri.
	 * @param path      the path of the constructed uri.
	 * @param query     the query of the constructed uri.
	 * @param fragment  the fragment of the constructed uri.
	 * @throws NullPointerException if the given {@code value} or {@code scheme} or {@code
	 *                              authority} or {@code path} or {@code query} or {@code
	 *                              fragment} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawUri(@NotNull String value, @NotNull Scheme scheme, @NotNull Authority authority, @NotNull Path path, @NotNull Query query, @NotNull Fragment fragment) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(scheme, "scheme");
		Objects.requireNonNull(authority, "authority");
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(query, "query");
		Objects.requireNonNull(fragment, "fragment");
		this.value = value;
		this.scheme = scheme;
		this.authority = Authority.raw(authority);
		this.path = path;
		this.query = Query.raw(query);
		this.fragment = fragment;
	}

	@NotNull
	@Override
	public RawUri clone() {
		try {
			return (RawUri) super.clone();
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
	@UnmodifiableView
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
	@UnmodifiableView
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
		return this.value.hashCode();
	}

	@NotNull
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}
}
