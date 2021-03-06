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

import org.cufy.http.syntax.UriParse;
import org.cufy.http.syntax.UriPattern;
import org.cufy.http.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link Uri}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractUri implements Uri {
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
	 * <b>Default</b>
	 * <br>
	 * Construct a new default uri.
	 *
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUri() {
		this.scheme = Scheme.scheme();
		this.authority = Authority.authority();
		this.path = Path.path();
		this.query = Query.query();
		this.fragment = Fragment.fragment();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new uri from copying the given {@code uri}.
	 *
	 * @param uri the uri to copy.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.6 ~2021.03.30
	 */
	public AbstractUri(@NotNull Uri uri) {
		Objects.requireNonNull(uri, "uri");
		this.scheme = uri.getScheme();
		this.authority = Authority.authority(uri.getAuthority());
		this.path = uri.getPath();
		this.query = Query.query(uri.getQuery());
		this.fragment = uri.getFragment();
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new uri from the given {@link File}.
	 *
	 * @param file the file to construct a new uri from.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUri(@NotNull File file) {
		Objects.requireNonNull(file, "file");

		URI uri = file.toURI();

		String scheme = uri.getScheme();
		String authority = uri.getRawAuthority();
		String path = uri.getRawPath();
		String query = uri.getRawQuery();
		String fragment = uri.getRawFragment();

		this.scheme = scheme == null || scheme.isEmpty() ?
					  Scheme.scheme() :
					  Scheme.scheme(scheme);
		this.authority = authority == null || authority.isEmpty() ?
						 Authority.authority() :
						 Authority.authority(authority);
		this.path = path == null || path.isEmpty() ?
					Path.path() :
					Path.path(path);
		this.query = query == null || query.isEmpty() ?
					 Query.query() :
					 Query.query(query);
		this.fragment = fragment == null || fragment.isEmpty() ?
						Fragment.fragment() :
						Fragment.fragment(fragment);
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct a new uri from the given {@link URL}.
	 *
	 * @param url the url to construct a new uri from.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a Uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUri(@NotNull URL url) {
		try {
			Objects.requireNonNull(url, "url");

			URI uri = url.toURI();

			String scheme = uri.getScheme();
			String authority = uri.getRawAuthority();
			String path = uri.getRawPath();
			String query = uri.getRawQuery();
			String fragment = uri.getRawFragment();

			this.scheme = scheme == null || scheme.isEmpty() ?
						  Scheme.scheme() :
						  Scheme.scheme(scheme);
			this.authority = authority == null || authority.isEmpty() ?
							 Authority.authority() :
							 Authority.authority(authority);
			this.path = path == null || path.isEmpty() ?
						Path.path() :
						Path.path(path);
			this.query = query == null || query.isEmpty() ?
						 Query.query() :
						 Query.query(query);
			this.fragment = fragment == null || fragment.isEmpty() ?
							Fragment.fragment() :
							Fragment.fragment(fragment);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("rejected java.net.URL: " + url, e);
		}
	}

	/**
	 * <b>Integration</b>
	 * <br>
	 * Construct new uri from a java native {@link URI}.
	 *
	 * @param uri the java native uri to construct a new uri (of this kind) from.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUri(@NotNull URI uri) {
		Objects.requireNonNull(uri, "uri");
		String scheme = uri.getScheme();
		String authority = uri.getRawAuthority();
		String path = uri.getRawPath();
		String query = uri.getRawQuery();
		String fragment = uri.getRawFragment();

		this.scheme = scheme == null || scheme.isEmpty() ?
					  Scheme.scheme() :
					  Scheme.scheme(scheme);
		this.authority = authority == null || authority.isEmpty() ?
						 Authority.authority() :
						 Authority.authority(authority);
		this.path = path == null || path.isEmpty() ?
					Path.path() :
					Path.path(path);
		this.query = query == null || query.isEmpty() ?
					 Query.query() :
					 Query.query(query);
		this.fragment = fragment == null || fragment.isEmpty() ?
						Fragment.fragment() :
						Fragment.fragment(fragment);
	}

	/**
	 * <b>Parse</b>
	 * <br>
	 * Construct a new uri from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed uri.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractUri(@NotNull @Pattern(UriRegExp.URI_REFERENCE) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.URI_REFERENCE.matcher(source).matches())
			throw new IllegalArgumentException("invalid uri: " + source);

		Matcher matcher = UriParse.URI.matcher(source);

		if (matcher.find()) {
			String scheme = matcher.group("Scheme");
			String authority = matcher.group("Authority");
			String path = matcher.group("Path");
			String query = matcher.group("Query");
			String fragment = matcher.group("Fragment");

			this.scheme = scheme == null || scheme.isEmpty() ?
						  Scheme.scheme() :
						  Scheme.scheme(scheme);
			this.authority = authority == null || authority.isEmpty() ?
							 Authority.authority() :
							 Authority.authority(authority);
			this.path = path == null || path.isEmpty() ?
						Path.path() :
						Path.path(path);
			this.query = query == null || query.isEmpty() ?
						 Query.query() :
						 Query.query(query);
			this.fragment = fragment == null || fragment.isEmpty() ?
							Fragment.fragment() :
							Fragment.fragment(fragment);
		} else {
			this.scheme = Scheme.scheme();
			this.authority = Authority.authority();
			this.path = Path.path();
			this.query = Query.query();
			this.fragment = Fragment.fragment();
		}
	}

	/**
	 * <b>Components</b>
	 * <br>
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
	public AbstractUri(@NotNull Scheme scheme, @NotNull Authority authority, @NotNull Path path, @NotNull Query query, @NotNull Fragment fragment) {
		Objects.requireNonNull(scheme, "scheme");
		Objects.requireNonNull(authority, "authority");
		Objects.requireNonNull(path, "path");
		Objects.requireNonNull(query, "query");
		Objects.requireNonNull(fragment, "fragment");
		this.scheme = scheme;
		this.authority = Authority.authority(authority);
		this.path = path;
		this.query = Query.query(query);
		this.fragment = fragment;
	}

	@NotNull
	@Override
	public AbstractUri clone() {
		try {
			AbstractUri clone = (AbstractUri) super.clone();
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

			//noinspection NonFinalFieldReferenceInEquals
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
		//noinspection NonFinalFieldReferencedInHashCode
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
