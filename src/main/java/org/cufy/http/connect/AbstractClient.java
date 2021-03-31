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
package org.cufy.http.connect;

import org.cufy.http.body.Body;
import org.cufy.http.request.Request;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * A basic implementation of the interface {@link Client}.
 *
 * @param <B> the type of the body.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class AbstractClient<B extends Body> extends AbstractCaller<Client<B>> implements Client<B> {
	/**
	 * The current request of this client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	protected Request<B> request;

	/**
	 * <b>Default</b>
	 * <br>
	 * Construct a new default client.
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient() {
		//noinspection unchecked
		this.request = (Request<B>) Request.defaultRequest();
	}

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new copy of the given {@code client}.
	 *
	 * @param client the client to copy.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	public AbstractClient(Client<?> client) {
		Objects.requireNonNull(client, "client");
		//noinspection unchecked
		this.request = (Request<B>) Request.copy(client.request());
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new client with its request begin the given {@code request}.
	 *
	 * @param request the request of this client.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	public AbstractClient(@NotNull Request<B> request) {
		Objects.requireNonNull(request, "request");
		//noinspection unchecked
		this.request = (Request<B>) Request.copy(request);
	}

	@NotNull
	@Override
	public AbstractClient<B> clone() {
		try {
			//noinspection unchecked
			AbstractClient<B> clone = (AbstractClient<B>) super.clone();
			clone.request = this.request.clone();
			//noinspection AccessingNonPublicFieldOfAnotherObject
			clone.callbacks = new LinkedHashMap<>(this.callbacks);
			//noinspection AccessingNonPublicFieldOfAnotherObject
			clone.callbacks.replaceAll((action, callbacks) -> new LinkedHashSet<>(callbacks));
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@NotNull
	@Override
	public <BB extends Body> Client<BB> request(@NotNull Request<BB> request) {
		Objects.requireNonNull(request, "request");
		//noinspection unchecked
		this.request = (Request<B>) request;
		//noinspection unchecked
		return (Client<BB>) this;
	}

	@NotNull
	@Override
	public Request<B> request() {
		return this.request;
	}

	@NotNull
	@NonNls
	@Override
	public String toString() {
		return "Client " + System.identityHashCode(this);
	}
}
//
//	/**
//	 * Construct a new client with its uri set from the given {@code file}.
//	 *
//	 * @param file file to set the uri of the constructed client from.
//	 * @throws NullPointerException if the given {@code file} is null.
//	 * @throws SecurityException    If a required system property value cannot be
//	 *                              accessed.
//	 * @since 0.0.1 ~2021.03.23
//	 */
//	public AbstractClient(@NotNull java.io.File file) {
//		Objects.requireNonNull(file, "file");
//		this.request.requestLine().uri(file);
//	}
//
//	/**
//	 * Construct a new client with its uri set from the given java-native {@code url}.
//	 *
//	 * @param url java-native url to set the uri of the constructed client from.
//	 * @throws NullPointerException     if the given {@code url} is null.
//	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
//	 *                                  RFC2396 and cannot be converted to a URI.
//	 * @since 0.0.1 ~2021.03.23
//	 */
//	public AbstractClient(@NotNull java.net.URL url) {
//		Objects.requireNonNull(url, "url");
//		this.request.requestLine().uri(url);
//	}
//
//	/**
//	 * Construct a new client with its uri begin the given {@code uri}.
//	 *
//	 * @param uri the uri of the constructed client.
//	 * @throws NullPointerException if the given {@code uri} is null.
//	 * @since 0.0.1 ~2021.03.23
//	 */
//	public AbstractClient(@NotNull java.net.URI uri) {
//		Objects.requireNonNull(uri, "uri");
//		this.request.requestLine().uri(uri);
//	}
//
//	/**
//	 *
//	 * Construct a new client with its uri set from the given {@code uri} literal.
//	 *
//	 * @param uri the uri literal to set the uri of this client.
//	 * @throws NullPointerException     if the given {@code uri} is null.
//	 * @throws IllegalArgumentException if the given {@code uri} does not match {@link
//	 *                                  URIRegExp#URI_REFERENCE}.
//	 * @since 0.0.1 ~2021.03.23
//	 */
//	public AbstractClient(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) String uri) {
//		Objects.requireNonNull(uri, "uri");
//		//noinspection unchecked
//		this.request = (Request<B>) Request.defaultRequest().uri(uri);
//	}
//
//	/**
//	 * Construct a new client with its uri begin the given {@code uri}.
//	 *
//	 * @param uri the uri of the constructed client.
//	 * @throws NullPointerException if the given {@code uri} is null.
//	 * @since 0.0.1 ~2021.03.23
//	 */
//	public AbstractClient(@NotNull URI uri) {
//		Objects.requireNonNull(uri, "uri");
//		this.request.requestLine().uri(uri);
//	}
