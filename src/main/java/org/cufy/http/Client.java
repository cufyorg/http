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

import org.cufy.http.middleware.SocketMiddleware;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.URI;
import org.cufy.http.util.Caller;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * A client is a stateful object containing the necessary data to perform an http request.
 * Also, the client provides the ability to set callbacks to be invoked when a specific
 * task/event occurs. (e.g. the response of a request received)
 * <br>
 * Note: the client has no callbacks as default. So, in order for it to work, you need to
 * add inject middlewares that perform the standard actions for it. (e.g. {@link
 * SocketMiddleware})
 * <br>
 * Also Note: a client is intended to be used as the central unit for a single request.
 * So, if a common configuration is needed. You might declare a global client and {@link
 * #clone()} it on the need of using it.
 * <br>
 * Actions:
 * <ul>
 *     <li>{@link #CONNECT}</li>
 *     <li>{@link #CONNECTED}</li>
 *     <li>{@link #MALFORMED}</li>
 *     <li>{@link #NOT_RECEIVED}</li>
 *     <li>{@link #NOT_SENT}</li>
 *     <li>{@link #REFORMAT}</li>
 *     <li>{@link #S1XX}</li>
 *     <li>{@link #S2XX}</li>
 *     <li>{@link #S3XX}</li>
 *     <li>{@link #S4XX}</li>
 *     <li>{@link #S5XX}</li>
 *     <li>{@link #UNEXPECTED}</li>
 * </ul>
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Client extends Caller<Client>, Cloneable {
	/**
	 * The "connect" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Request} not-null: the request to be sent. (use this instead of getting it from the client object)
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The user wants to connect (send the request) to the server.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONNECT = "connect";
	/**
	 * The "connected" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>A response was received and parsed successfully from the server after connecting to it.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String CONNECTED = "connected";
	/**
	 * The "malformed-response" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Throwable} not-null: the throwable that got caught.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>
	 *         Parse related throwable got caught when attempting to parse the response returned from the server.
	 *     </li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23l
	 */
	String MALFORMED = "malformed-response";
	/**
	 * The "not-received" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link IOException} not-null: the exception that got thrown when receiving.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>An IO-exception got caught when trying to receive from the server.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String NOT_RECEIVED = "not-received";
	/**
	 * The "not-sent" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link IOException} not-null: the exception that got thrown when connecting (sending).
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>An IO-exception got caught when trying to connect (send the request) to the server.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String NOT_SENT = "not-sent";
	/**
	 * The "reformat" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Request} not-null: the request to be reformatted.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>
	 *         A request is ready to be used but unsure that all the attributes are properly included
	 *     </li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String REFORMAT = "reformat";
	/**
	 * The "1xx" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The response got parsed and the status-code was in the 100-199 range.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String S1XX = "1xx";
	/**
	 * The "2xx" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The response got parsed and the status-code was in the 200-299 range.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String S2XX = "2xx";
	/**
	 * The "3xx" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The response got parsed and the status-code was in the 300-399 range.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String S3XX = "3xx";
	/**
	 * The "4xx" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The response got parsed and the status-code was in the 400-499 range.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String S4XX = "4xx";
	/**
	 * The "5xx" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>The response got parsed and the status-code was in the 500-599 range.</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	String S5XX = "5xx";
	/**
	 * The "unexpected-response" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link Response} not-null: the response that got received.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>
	 *         The response that got received was successfully parsed but its content was unexpected.
	 *     </li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23l
	 */
	String UNEXPECTED = "unexpected-response";

	/**
	 * Return a new client instance to be a placeholder if a the user has not specified a
	 * client.
	 *
	 * @return a new empty client.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client defaultClient() {
		return new AbstractClient();
	}

	/**
	 * Construct a new client with its request begin the given {@code request}.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param request the request of this client.
	 * @return a new client from the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client from(@NotNull Request request) {
		return new AbstractClient(request)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Construct a new client with its uri set from the given {@code file}.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param file file to set the uri of the constructed client from.
	 * @return a new uri from the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client to(@NotNull java.io.File file) {
		return new AbstractClient(file)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Construct a new client with its uri set from the given java-native {@code url}.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param url java-native url to set the uri of the constructed client from.
	 * @return a new uri from the given {@code url}.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a URI.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client to(@NotNull java.net.URL url) {
		return new AbstractClient(url)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Construct a new client with its uri set from the given java-native {@code uri}.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param uri java-native uri to set the uri of the constructed client from.
	 * @return a new uri from the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client to(@NotNull java.net.URI uri) {
		return new AbstractClient(uri)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Construct a new client with its uri begin the given {@code uri}.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param uri the uri of the constructed client.
	 * @return a new client from the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client to(@NotNull URI uri) {
		return new AbstractClient(uri)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Construct a new client with its uri set from the given {@code uri} literal.
	 * <br>
	 * The returned client will have {@link SocketMiddleware} pre-injected.
	 *
	 * @param uri the uri literal to set the uri of this client.
	 * @return a new client from the given {@code uri} literal.
	 * @throws NullPointerException     if the given {@code uri} is null.
	 * @throws IllegalArgumentException if the given {@code uri} does not match {@link
	 *                                  URIRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client to(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) @Subst("example.com") String uri) {
		return new AbstractClient(uri)
				.middleware(SocketMiddleware.middleware());
	}

	/**
	 * Send the request of this client:
	 * <br>
	 * Shortcut for:
	 * <pre>
	 *     client.{@link #trigger(Object, String) trigger}(client.{@link #request()}.{@link Request#clone() clone()}, {@link #CONNECT Client.CONNECT})
	 * </pre>
	 *
	 * @return this.
	 * @since 0.0.1 ~2021.03.23
	 */
	default Client connect() {
		return this.trigger(this.request().clone(), Client.CONNECT);
	}

	/**
	 * Set the request of this from the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if the request of this client cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client request(@NotNull Request request) {
		throw new UnsupportedOperationException("request");
	}

	/**
	 * Replace the request of this client to be the result of invoking the given {@code
	 * operator} with the current request of this client. If the {@code operator} returned
	 * null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the request of this client cannot be
	 *                                       changed and the returned request from the
	 *                                       given {@code operator} is different from the
	 *                                       current request.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client request(@NotNull UnaryOperator<Request> operator) {
		Objects.requireNonNull(operator, "operator");
		Request r = this.request();
		Request request = operator.apply(r);

		if (request != null && request != r)
			this.request(request);

		return this;
	}

	/**
	 * Set the request of this from the given {@code request} literal.
	 *
	 * @param request the request literal to set the request of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws IllegalArgumentException      if the given {@code request} does not match
	 *                                       {@link HTTPRegExp#REQUEST}.
	 * @throws UnsupportedOperationException if the request of this client cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client request(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) @Subst("GET / HTTP/1.1\n") String request) {
		return this.request(Request.parse(request));
	}

	/**
	 * Clone this caller. The clone must have a cloned request of this and a shallow copy
	 * of the listeners of this (but not the mappings).
	 * <br>
	 * Note: the cloned client will not be {@link #equals(Object) equal} to this client.
	 *
	 * @return a clone of this client.
	 * @since 0.0.1 ~2021.03.24
	 */
	Client clone();

	/**
	 * Return the request defined for this.
	 *
	 * @return the request of this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(pure = true)
	Request request();
}
