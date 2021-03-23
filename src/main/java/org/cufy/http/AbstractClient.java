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

import org.cufy.http.request.Request;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.URI;
import org.cufy.http.util.AbstractCaller;
import org.cufy.http.util.Callback;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Client}.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class AbstractClient extends AbstractCaller<Client> implements Client {
	/**
	 * The current request of this client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	protected Request request = Request.defaultRequest();

	/**
	 * Construct a new empty client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient() {

	}

	/**
	 * Construct a new client with its uri set from the given {@code uri} literal.
	 *
	 * @param uri the uri literal to set the uri of this client.
	 * @throws NullPointerException     if the given {@code uri} is null.
	 * @throws IllegalArgumentException if the given {@code uri} does not match {@link
	 *                                  URIRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) @Subst("example.com") String uri) {
		Objects.requireNonNull(uri, "uri");
		this.request.requestLine().uri(uri);
	}

	/**
	 * Construct a new client with its uri set from the given {@code file}.
	 *
	 * @param file file to set the uri of the constructed client from.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull java.io.File file) {
		Objects.requireNonNull(file, "file");
		this.request.requestLine().uri(file);
	}

	/**
	 * Construct a new client with its uri set from the given java-native {@code url}.
	 *
	 * @param url java-native url to set the uri of the constructed client from.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a URI.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull java.net.URL url) {
		Objects.requireNonNull(url, "url");
		this.request.requestLine().uri(url);
	}

	/**
	 * Construct a new client with its uri begin the given {@code uri}.
	 *
	 * @param uri the uri of the constructed client.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull java.net.URI uri) {
		Objects.requireNonNull(uri, "uri");
		this.request.requestLine().uri(uri);
	}

	/**
	 * Construct a new client with its uri begin the given {@code uri}.
	 *
	 * @param uri the uri of the constructed client.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull URI uri) {
		Objects.requireNonNull(uri, "uri");
		this.request.requestLine().uri(uri);
	}

	/**
	 * Construct a new client with its request begin the given {@code request}.
	 *
	 * @param request the request of this client.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
	}

	@Override
	public AbstractClient clone() {
		try {
			AbstractClient clone = (AbstractClient) super.clone();
			clone.request = this.request.clone();
			//noinspection AccessingNonPublicFieldOfAnotherObject
			clone.callbacks = (HashMap<String, HashSet<Callback<Client, ?>>>) this.callbacks.clone();
			//noinspection AccessingNonPublicFieldOfAnotherObject
			clone.callbacks.replaceAll((action, callbacks) -> (HashSet<Callback<Client, ?>>) callbacks.clone());
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public Client request(@NotNull Request request) {
		Objects.requireNonNull(request, "request");
		this.request = request;
		return this;
	}

	@NotNull
	@Override
	public Request request() {
		return this.request;
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return "Client " + System.identityHashCode(this);
	}
}
