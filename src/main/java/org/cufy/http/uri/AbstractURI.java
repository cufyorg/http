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

import org.cufy.http.syntax.URIParse;
import org.cufy.http.syntax.URIPattern;
import org.cufy.http.syntax.URIRegExp;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * A basic implementation of the interface {@link URI}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.21
 */
public class AbstractURI implements URI {
	@SuppressWarnings("JavaDoc")
	private static final long serialVersionUID = 4830745156508565988L;

	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Authority authority = Authority.defaultAuthority();
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Fragment fragment = Fragment.defaultFragment();
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Path path = Path.defaultPath();
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Query query = Query.defaultQuery();
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	protected Scheme scheme = Scheme.defaultScheme();

	/**
	 * Construct a new empty uri.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractURI() {

	}

	/**
	 * Construct a new uri from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed uri.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  URIRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractURI(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) String source) {
		Objects.requireNonNull(source, "source");
		if (!URIPattern.URI_REFERENCE.matcher(source).matches())
			throw new IllegalArgumentException("invalid uri: " + source);

		Matcher matcher = URIParse.URI.matcher(source);

		if (matcher.find()) {
			@Subst("http") String scheme = matcher.group("Scheme");
			@Subst("admin:admin@example.com:400") String authority = matcher.group("Authority");
			@Subst("/search") String path = matcher.group("Path");
			@Subst("q=query&v=variable") String query = matcher.group("Query");
			@Subst("top") String fragment = matcher.group("Fragment");

			if (scheme != null && !scheme.isEmpty())
				this.scheme = Scheme.parse(scheme);
			if (authority != null && !authority.isEmpty())
				this.authority = Authority.parse(authority);
			if (path != null && !path.isEmpty())
				this.path = Path.parse(path);
			if (query != null && !query.isEmpty())
				this.query = Query.parse(query);
			if (fragment != null && !fragment.isEmpty())
				this.fragment = Fragment.parse(fragment);
		}
	}

	/**
	 * Construct a new uri from the given {@link java.io.File}.
	 *
	 * @param file the file to construct a new uri from.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractURI(@NotNull java.io.File file) {
		Objects.requireNonNull(file, "file");

		java.net.URI uri = file.toURI();

		@Subst("http") String scheme = uri.getScheme();
		@Subst("admin:admin@example.com:4000") String authority = uri.getRawAuthority();
		@Subst("/search") String path = uri.getRawPath();
		@Subst("q=query&v=variable") String query = uri.getRawQuery();
		@Subst("top") String fragment = uri.getRawFragment();

		if (scheme != null && !scheme.isEmpty())
			this.scheme = Scheme.parse(scheme);
		if (authority != null && !authority.isEmpty())
			this.authority = Authority.parse(authority);
		if (path != null && !path.isEmpty())
			this.path = Path.parse(path);
		if (query != null && !query.isEmpty())
			this.query = Query.parse(query);
		if (fragment != null && !fragment.isEmpty())
			this.fragment = Fragment.parse(fragment);
	}

	/**
	 * Construct a new uri from the given {@link java.net.URL}.
	 *
	 * @param url the url to construct a new uri from.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a URI.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractURI(@NotNull java.net.URL url) {
		try {
			Objects.requireNonNull(url, "url");

			java.net.URI uri = url.toURI();

			@Subst("http") String scheme = uri.getScheme();
			@Subst("admin:admin@example.com:4000") String authority = uri.getRawAuthority();
			@Subst("/search") String path = uri.getRawPath();
			@Subst("q=query&v=variable") String query = uri.getRawQuery();
			@Subst("top") String fragment = uri.getRawFragment();

			if (scheme != null && !scheme.isEmpty())
				this.scheme = Scheme.parse(scheme);
			if (authority != null && !authority.isEmpty())
				this.authority = Authority.parse(authority);
			if (path != null && !path.isEmpty())
				this.path = Path.parse(path);
			if (query != null && !query.isEmpty())
				this.query = Query.parse(query);
			if (fragment != null && !fragment.isEmpty())
				this.fragment = Fragment.parse(fragment);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("rejected java.net.URL: " + url, e);
		}
	}

	/**
	 * Construct new uri from a java native {@link java.net.URI}.
	 *
	 * @param uri the java native uri to construct a new uri (of this kind) from.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	public AbstractURI(@NotNull java.net.URI uri) {
		Objects.requireNonNull(uri, "uri");
		@Subst("http") String scheme = uri.getScheme();
		@Subst("admin:admin@example.com:4000") String authority = uri.getRawAuthority();
		@Subst("/search") String path = uri.getRawPath();
		@Subst("q=query&v=variable") String query = uri.getRawQuery();
		@Subst("top") String fragment = uri.getRawFragment();

		if (scheme != null && !scheme.isEmpty())
			this.scheme = Scheme.parse(scheme);
		if (authority != null && !authority.isEmpty())
			this.authority = Authority.parse(authority);
		if (path != null && !path.isEmpty())
			this.path = Path.parse(path);
		if (query != null && !query.isEmpty())
			this.query = Query.parse(query);
		if (fragment != null && !fragment.isEmpty())
			this.fragment = Fragment.parse(fragment);
	}

	@NotNull
	@Override
	public Authority authority() {
		return this.authority;
	}

	@NotNull
	@Override
	public URI authority(@NotNull Authority authority) {
		Objects.requireNonNull(authority, "authority");
		this.authority = authority;
		return this;
	}

	@NotNull
	@Override
	public AbstractURI clone() {
		try {
			AbstractURI clone = (AbstractURI) super.clone();
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
		if (object instanceof URI) {
			URI uri = (URI) object;

			//noinspection NonFinalFieldReferenceInEquals
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

	@NotNull
	@Override
	public URI fragment(@NotNull Fragment fragment) {
		Objects.requireNonNull(fragment, "fragment");
		this.fragment = fragment;
		return this;
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
	public Path path() {
		return this.path;
	}

	@NotNull
	@Override
	public URI path(@NotNull Path path) {
		Objects.requireNonNull(path, "path");
		this.path = path;
		return this;
	}

	@NotNull
	@Override
	public Query query() {
		return this.query;
	}

	@NotNull
	@Override
	public URI query(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.query = query;
		return this;
	}

	@NotNull
	@Override
	public Scheme scheme() {
		return this.scheme;
	}

	@NotNull
	@Override
	public URI scheme(@NotNull Scheme scheme) {
		Objects.requireNonNull(scheme, "scheme");
		this.scheme = scheme;
		return this;
	}

	@NotNull
	@NonNls
	@Pattern(URIRegExp.URI)
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

		@Subst("http://admin:admin@exmaple.com:4000/search?q=query#top") String s = builder.toString();
		return s;
	}
}
