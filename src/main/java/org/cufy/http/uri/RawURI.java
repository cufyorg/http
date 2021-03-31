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
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Objects;

/**
 * A basic implementation of the interface {@link URI}.
 *
 * @author LSafer
 * @version 0.0.6
 * @since 0.0.6 ~2021.03.30
 */
public class RawURI implements URI {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4830745156508565988L;

	/**
	 * The authority to be returned by {@link #authority()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Authority authority;
	/**
	 * The fragment to be returned by {@link #fragment()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Fragment fragment;
	/**
	 * The path to be returned by {@link #path()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	protected final Path path;
	/**
	 * The query to be returned by {@link #query()}.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	@NotNull
	@UnmodifiableView
	protected final Query query;
	/**
	 * The scheme to be returned by {@link #scheme()}.
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
	@NonNls
	protected final String value;

	/**
	 * <b>Empty</b>
	 * <br>
	 * Construct a new empty raw uri.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public RawURI() {
		this.value = "";
		this.scheme = Scheme.empty();
		this.authority = Authority.empty();
		this.path = Path.empty();
		this.query = Query.empty();
		this.fragment = Fragment.empty();
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
	public RawURI(@NotNull URI uri) {
		Objects.requireNonNull(uri, "uri");
		this.value = uri.toString();
		this.scheme = uri.scheme();
		this.authority = Authority.unmodifiable(uri.authority());
		this.path = uri.path();
		this.query = Query.unmodifiable(uri.query());
		this.fragment = uri.fragment();
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
	public RawURI(@NotNull @NonNls String value) {
		Objects.requireNonNull(value, "value");
		this.value = value;
		this.scheme = Scheme.empty();
		this.authority = Authority.empty();
		this.path = Path.empty();
		this.query = Query.empty();
		this.fragment = Fragment.empty();
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
	public RawURI(@NotNull @NonNls String value, @NotNull Scheme scheme, @NotNull Authority authority, @NotNull Path path, @NotNull Query query, @NotNull Fragment fragment) {
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(scheme, "scheme");
		Objects.requireNonNull(authority, "authority");
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(query, "query");
		Objects.requireNonNull(fragment, "fragment");
		this.value = value;
		this.scheme = scheme;
		this.authority = Authority.unmodifiable(authority);
		this.path = path;
		this.query = Query.unmodifiable(query);
		this.fragment = fragment;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Authority authority() {
		return this.authority;
	}

	@NotNull
	@Override
	public RawURI clone() {
		try {
			return (RawURI) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public boolean equals(@Nullable Object object) {
		if (object == this)
			return true;
		if (object instanceof URI) {
			URI uri = (URI) object;

			return Objects.equals(this.scheme, uri.scheme()) &&
				   Objects.equals(this.authority, uri.authority()) &&
				   Objects.equals(this.path, uri.path()) &&
				   Objects.equals(this.query, uri.query()) &&
				   Objects.equals(this.fragment, uri.fragment());
		}

		return false;
	}

	@NotNull
	@Override
	public Fragment fragment() {
		return this.fragment;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@NotNull
	@Override
	public Path path() {
		return this.path;
	}

	@NotNull
	@UnmodifiableView
	@Override
	public Query query() {
		return this.query;
	}

	@NotNull
	@Override
	public Scheme scheme() {
		return this.scheme;
	}

	@NotNull
	@NonNls
	@Pattern(".*")
	@Override
	public String toString() {
		return this.value;
	}
}
