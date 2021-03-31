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
import org.cufy.http.middleware.SocketMiddleware;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.syntax.HTTPRegExp;
import org.cufy.http.syntax.URIRegExp;
import org.cufy.http.uri.URI;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;

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
 *
 * @param <B> the type of the body of the request of this client.
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Client<B extends Body> extends Caller<Client<B>>, Cloneable {
	/**
	 * <b>Mandatory</b>
	 * <br>
	 * <b>User -> CMW</b>
	 * <br>
	 * <b>Triggered by:</b> the user to perform the connection.
	 * <br>
	 * <b>Triggers:</b> the connection middleware to perform the connection.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Request<?>> CONNECT = Action.of(Request.class, "connect", "connect");
	/**
	 * <b>Mandatory</b>
	 * <br>
	 * <b>CMW -> USER</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the connection was
	 * successfully finished and the response is ready to be used.
	 * <br>
	 * <b>Triggers:</b> the user to use the response.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Response<?>> CONNECTED = Action.of(Response.class, "connected", "connected");
	/**
	 * <b>Mandatory</b>
	 * <br>
	 * <b>CMW -> IOEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that teh connection has
	 * failed and the 'connected' action will not be triggered due to that.
	 * <br>
	 * <b>Triggers:</b> the connection exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<IOException> DISCONNECTED = Action.of(IOException.class, "disconnected|not-sent|not-received|malformed|not-parsed", "disconnected");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>CMW -> IOEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the server has sent
	 * the client a malformed response (illegal response - malformed status-line and/or
	 * headers).
	 * <br>
	 * <b>Triggers:</b> the connection exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<IOException> MALFORMED = Action.of(IOException.class, "malformed", "malformed", "disconnected");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>Parser -> IOEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> ta parser middle-ware to notify that the body was not parsed
	 * successfully.
	 * <br>
	 * <b>Triggers:</b> the parsing exceptions handlers.
	 * <br>
	 * <b>Note:</b> this action will not stop triggering the `connected` action. It will
	 * just notify the IOEHandle hoping the problem get fixed before the user uses the
	 * response expecting nothing happened. (btw, this action get triggered before the
	 * `connected` action)
	 *
	 * @since 0.0.6 ~2021.03.31
	 */
	Action<IOException> NOT_PARSED = Action.of(IOException.class, "not-parsed", "not-parsed", "disconnected");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>CMW -> IOEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the response was
	 * received because of an IOException.
	 * <br>
	 * <b>Triggers:</b> the connection exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<IOException> NOT_RECEIVED = Action.of(IOException.class, "not-received", "not-received", "disconnected");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>CMW -> IOEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the message was not
	 * sent because of an IOException.
	 * <br>
	 * <b>Triggers:</b> the connection exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<IOException> NOT_SENT = Action.of(IOException.class, "not-sent", "not-sent", "disconnected");
	/**
	 * <b>Feature</b>
	 * <br>
	 * <b>CMW -> ImplicitH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the response has
	 * been built successfully and ready to be used.
	 * <br>
	 * <b>Triggers:</b> the implicit helpers to fill-out implicit data.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Response<?>> RECEIVED = Action.of(Response.class, "received", "received");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>CMW -> AnalyticH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection to notify that the server has sent the client
	 * the data.
	 * <br>
	 * <b>Triggers:</b> the analytic listeners that want to know if the response was
	 * received or not and what exactly was received.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<String> RECEIVING = Action.of(String.class, "receiving", "receiving");
	/**
	 * <b>Feature</b>
	 * <br>
	 * <b>USER -> ImplicitH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the connection will
	 * be performed soon.
	 * <br>
	 * <b>Triggers:</b> the helper listeners to fill-out implicit data.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Request<?>> SENDING = Action.of(Request.class, "sending", "sending");
	/**
	 * <b>Optional</b>
	 * <br>
	 * <b>CMW -> AnalyticH</b>
	 * <br>
	 * <b>Triggered by:</b> the connection middleware to notify that the request was sent
	 * successfully.
	 * <br>
	 * <b>Triggers:</b> the analytic listeners that want to know if the request was sent
	 * or not and what exactly was sent.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<String> SENT = Action.of(String.class, "sent", "sent");

	/**
	 * <b>Copy</b>
	 * <br>
	 * Construct a new copy of the given {@code client}.
	 *
	 * @param client the client to copy.
	 * @return a copy of the given {@code client}.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.0.6 ~2021.03.31
	 */
	static Client<Body> copy(Client<?> client) {
		return new AbstractClient<>(client);
	}

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new client instance to be a placeholder if a the user has not specified a
	 * client.
	 *
	 * @return a new default client.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> defaultClient() {
		return new AbstractClient<>();
	}

	/**
	 * <b>Shortcut</b>
	 * <br>
	 * Construct a new client with its uri set from the given {@code file}.
	 *
	 * @param file file to set the uri of the constructed client from.
	 * @return a new uri from the given {@code file}.
	 * @throws NullPointerException if the given {@code file} is null.
	 * @throws SecurityException    If a required system property value cannot be
	 *                              accessed.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> to(@NotNull java.io.File file) {
		return new AbstractClient<>()
				.request(r -> r.uri(URI.from(file)));
	}

	/**
	 * <b>Shortcut</b>
	 * <br>
	 * Construct a new client with its uri set from the given java-native {@code url}.
	 *
	 * @param url java-native url to set the uri of the constructed client from.
	 * @return a new uri from the given {@code url}.
	 * @throws NullPointerException     if the given {@code url} is null.
	 * @throws IllegalArgumentException if the URL is not formatted strictly according to
	 *                                  RFC2396 and cannot be converted to a URI.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> to(@NotNull java.net.URL url) {
		return new AbstractClient<>()
				.request(r -> r.uri(URI.from(url)));
	}

	/**
	 * <b>Shortcut</b>
	 * <br>
	 * Construct a new client with its uri set from the given java-native {@code uri}.
	 *
	 * @param uri java-native uri to set the uri of the constructed client from.
	 * @return a new uri from the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> to(@NotNull java.net.URI uri) {
		return new AbstractClient<>()
				.request(r -> r.uri(URI.from(uri)));
	}

	/**
	 * <b>Shortcut</b>
	 * <br>
	 * Construct a new client with its uri begin the given {@code uri}.
	 *
	 * @param uri the uri of the constructed client.
	 * @return a new client from the given {@code uri}.
	 * @throws NullPointerException if the given {@code uri} is null.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> to(@NotNull URI uri) {
		return new AbstractClient<>()
				.request(r -> r.uri(uri));
	}

	/**
	 * <b>Shortcut</b>
	 * <br>
	 * Construct a new client with its uri set from the given {@code uri} literal.
	 *
	 * @param uri the uri literal to set the uri of this client.
	 * @return a new client from the given {@code uri} literal.
	 * @throws NullPointerException     if the given {@code uri} is null.
	 * @throws IllegalArgumentException if the given {@code uri} does not match {@link
	 *                                  URIRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	static Client<Body> to(@NotNull @NonNls @Pattern(URIRegExp.URI_REFERENCE) String uri) {
		return new AbstractClient<>()
				.request(r -> r.uri(uri));
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new client with its request begin the given {@code request}.
	 *
	 * @param request the request of this client.
	 * @param <B>     the type of the body of the given {@code request}.
	 * @return a new client from the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.23
	 */
	static <B extends Body> Client<B> with(@NotNull Request<B> request) {
		return new AbstractClient<>(request);
	}

	/**
	 * Send the request of this client:
	 * <br>
	 * Shortcut for:
	 * <pre>
	 *     client.{@link #trigger(Action, Object) trigger}({@link #CONNECT Client.REQUEST}, client.{@link #request()}.{@link Request#clone() clone()})
	 * </pre>
	 *
	 * @return this.
	 * @since 0.0.1 ~2021.03.23
	 */
	default Client<B> connect() {
		return this.trigger(Client.CONNECT, this.request().clone());
	}

	/**
	 * Set the request of this from the given {@code request}.
	 *
	 * @param request the request to be set.
	 * @param <BB>    the type of the new body.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws UnsupportedOperationException if the request of this client cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default <BB extends Body> Client<BB> request(@NotNull Request<BB> request) {
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
	 * @param <BB>     the new body type.
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
	default <BB extends Body> Client<BB> request(@NotNull Function<Request<B>, Request<BB>> operator) {
		Objects.requireNonNull(operator, "operator");
		Request<B> r = this.request();
		Request<BB> request = operator.apply(r);

		if (request != null && request != r)
			this.request(request);

		//noinspection unchecked
		return (Client<BB>) this;
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
	default Client<Body> request(@NotNull @NonNls @Pattern(HTTPRegExp.REQUEST) String request) {
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
	@NotNull
	@Contract(value = "->new", pure = true)
	Client<B> clone();

	/**
	 * Return the request defined for this.
	 *
	 * @return the request of this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(pure = true)
	Request<B> request();
}
