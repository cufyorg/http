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

import org.cufy.http.internal.syntax.UriParse;
import org.cufy.http.internal.syntax.UriPattern;
import org.cufy.http.internal.syntax.UriRegExp;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;

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
public class Uri implements Cloneable, Serializable {
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
	@Pattern(UriRegExp.FRAGMENT)
	protected String fragment;
	/**
	 * The authority of this.
	 *
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.PATH)
	protected String path;
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
	@Pattern(UriRegExp.SCHEME)
	protected String scheme;

	/**
	 * Construct a new uri.
	 *
	 * @since 0.3.0 ~2021.11.26
	 */
	public Uri() {
		this.scheme = Scheme.HTTP;
		this.authority = new Authority();
		this.path = Path.UNSPECIFIED;
		this.query = new Query();
		this.fragment = Fragment.UNSPECIFIED;
	}

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
	public Uri(
			@NotNull @Pattern(UriRegExp.SCHEME) String scheme,
			@NotNull Authority authority,
			@NotNull @Pattern(UriRegExp.PATH) String path,
			@NotNull Query query,
			@NotNull @Pattern(UriRegExp.FRAGMENT) String fragment
	) {
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

	/**
	 * Construct a new uri with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new uri.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	public Uri(@NotNull Consumer<@NotNull Uri> builder) {
		Objects.requireNonNull(builder, "builder");
		this.scheme = Scheme.HTTP;
		this.authority = new Authority();
		this.path = Path.UNSPECIFIED;
		this.query = new Query();
		this.fragment = Fragment.UNSPECIFIED;
		//noinspection ThisEscapedInObjectConstruction
		builder.accept(this);
	}

	/**
	 * Construct new uri from a java native {@link URI}.
	 *
	 * @param uri the java native uri to construct a new uri (of this kind) from.
	 * @return a new uri from the given native {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Uri from(@NotNull URI uri) {
		Objects.requireNonNull(uri, "uri");

		String schemeSrc = uri.getScheme();
		String authoritySrc = uri.getRawAuthority();
		String pathSrc = uri.getRawPath();
		String querySrc = uri.getRawQuery();
		String fragmentSrc = uri.getRawFragment();

		String scheme =
				schemeSrc == null || schemeSrc.isEmpty() ?
				Scheme.HTTP : Scheme.parse(schemeSrc);
		Authority authority =
				authoritySrc == null || authoritySrc.isEmpty() ?
				new Authority() : Authority.parse(authoritySrc);
		String path =
				pathSrc == null || pathSrc.isEmpty() ?
				Path.UNSPECIFIED : Path.parse(pathSrc);
		Query query =
				querySrc == null || querySrc.isEmpty() ?
				new Query() : Query.parse(querySrc);
		String fragment =
				fragmentSrc == null || fragmentSrc.isEmpty() ?
				Fragment.UNSPECIFIED : Fragment.parse(fragmentSrc);

		return new Uri(
				scheme,
				authority,
				path,
				query,
				fragment
		);
	}

	/**
	 * Construct a new uri from the given {@link File}.
	 *
	 * @param file the file to construct a new uri from.
	 * @return a new uri from the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Uri from(@NotNull File file) {
		Objects.requireNonNull(file, "file");
		return Uri.from(file.toURI());
	}

	/**
	 * Construct a new uri from the given {@link URL}.
	 *
	 * @param url the url to construct a new uri from.
	 * @return a new uri from the given {@code url}.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a Uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Uri from(@NotNull URL url) {
		Objects.requireNonNull(url, "url");

		try {
			return Uri.from(url.toURI());
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("rejected java.net.URL: " + url, e);
		}
	}

	/**
	 * Construct a new uri from parsing the given {@code source}.
	 *
	 * @param source the source of the constructed uri.
	 * @return a new uri from parsing the given {@code source}.
	 * @throws NullPointerException     if the given {@code source} is null.
	 * @throws IllegalArgumentException if the given {@code source} does not match {@link
	 *                                  UriRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	public static Uri parse(@NotNull @Pattern(UriRegExp.URI_REFERENCE) String source) {
		Objects.requireNonNull(source, "source");
		if (!UriPattern.URI_REFERENCE.matcher(source).matches())
			throw new IllegalArgumentException("invalid uri: " + source);

		Matcher matcher = UriParse.URI.matcher(source);

		if (!matcher.find())
			throw new InternalError("invalid uri " + source);

		String schemeSrc = matcher.group("Scheme");
		String authoritySrc = matcher.group("Authority");
		String pathSrc = matcher.group("Path");
		String querySrc = matcher.group("Query");
		String fragmentSrc = matcher.group("Fragment");

		String scheme =
				schemeSrc == null || schemeSrc.isEmpty() ?
				Scheme.HTTP : Scheme.parse(schemeSrc);
		Authority authority =
				authoritySrc == null || authoritySrc.isEmpty() ?
				new Authority() : Authority.parse(authoritySrc);
		String path =
				pathSrc == null || pathSrc.isEmpty() ?
				Path.UNSPECIFIED : Path.parse(pathSrc);
		Query query =
				querySrc == null || querySrc.isEmpty() ?
				new Query() : Query.parse(querySrc);
		String fragment =
				fragmentSrc == null || fragmentSrc.isEmpty() ?
				Fragment.UNSPECIFIED : Fragment.parse(fragmentSrc);

		return new Uri(
				scheme,
				authority,
				path,
				query,
				fragment
		);
	}

	/**
	 * Capture this uri into a new object.
	 *
	 * @return a clone of this uri.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	public Uri clone() {
		try {
			Uri clone = (Uri) super.clone();
			clone.authority = this.authority.clone();
			clone.query = this.query.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

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

	/**
	 * The hash code of an uri is the {@code xor} of the hash codes of its components.
	 * (optional)
	 *
	 * @return the hash code of this uri.
	 * @since 0.0.1 ~2021.03.23
	 */
	@Override
	@Contract(pure = true)
	public int hashCode() {
		return this.scheme.hashCode() ^
			   this.authority.hashCode() ^
			   this.path.hashCode() ^
			   this.query.hashCode() ^
			   this.fragment.hashCode();
	}

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
	public String toString() {
		String scheme = this.scheme;
		String authority = this.authority.toString();
		String path = this.path;
		String query = this.query.toString();
		String fragment = this.fragment;

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

	/**
	 * Return the authority defined for this.
	 *
	 * @return the authority of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	public Authority getAuthority() {
		return this.authority;
	}

	/**
	 * Return the fragment defined for this.
	 *
	 * @return the fragment of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.FRAGMENT)
	@Contract(pure = true)
	public String getFragment() {
		return this.fragment;
	}

	/**
	 * Return the path defined for this.
	 *
	 * @return the path of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.PATH)
	@Contract(pure = true)
	public String getPath() {
		return this.path;
	}

	/**
	 * Return the query defined for this.
	 *
	 * @return the query of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Contract(pure = true)
	public Query getQuery() {
		return this.query;
	}

	/**
	 * Return the scheme defined for this.
	 *
	 * @return the scheme of this.
	 * @since 0.0.1 ~2021.03.21
	 */
	@NotNull
	@Pattern(UriRegExp.SCHEME)
	@Contract(pure = true)
	public String getScheme() {
		return this.scheme;
	}

	/**
	 * Set the authority of this from the given {@code authority}.
	 *
	 * @param authority the authority to be set.
	 * @throws NullPointerException          if the given {@code authority} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       authority.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setAuthority(@NotNull Authority authority) {
		Objects.requireNonNull(authority, "authority");
		this.authority = authority;
	}

	/**
	 * Set the fragment of this from the given {@code fragment}.
	 *
	 * @param fragment the fragment to be set.
	 * @throws NullPointerException          if the given {@code fragment} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       fragment.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setFragment(@NotNull @Pattern(UriRegExp.FRAGMENT) String fragment) {
		Objects.requireNonNull(fragment, "fragment");
		this.fragment = fragment;
	}

	/**
	 * Set the path of this from the given {@code path}.
	 *
	 * @param path the path to be set.
	 * @throws NullPointerException          if the given {@code path} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       path.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setPath(@NotNull @Pattern(UriRegExp.PATH) String path) {
		Objects.requireNonNull(path, "path");
		this.path = path;
	}

	/**
	 * Set the query of this from the given {@code query}.
	 *
	 * @param query the query to be set.
	 * @throws NullPointerException          if the given {@code query} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       query.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setQuery(@NotNull Query query) {
		Objects.requireNonNull(query, "query");
		this.query = query;
	}

	/**
	 * Set the scheme of this from the given {@code scheme}.
	 *
	 * @param scheme the scheme to be set.
	 * @throws NullPointerException          if the given {@code scheme} is null.
	 * @throws UnsupportedOperationException if this uri does not support changing its
	 *                                       scheme.
	 * @since 0.0.1 ~2021.03.21
	 */
	@Contract(mutates = "this")
	public void setScheme(@NotNull @Pattern(UriRegExp.SCHEME) String scheme) {
		Objects.requireNonNull(scheme, "scheme");
		this.scheme = scheme;
	}
}
