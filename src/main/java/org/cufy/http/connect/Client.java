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

import org.cufy.http.middleware.Middleware;
import org.cufy.http.middleware.SocketMiddleware;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.syntax.HttpRegExp;
import org.cufy.http.syntax.UriRegExp;
import org.cufy.http.uri.Uri;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.Pattern;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * A client is a stateful object containing the necessary data to perform a http request.
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
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public interface Client extends Cloneable {
	/**
	 * <b>Dynamic</b>
	 * <br>
	 * <b>Any -> NosyUser</b>
	 * <br>
	 * <b>Triggered by:</b> anything except "exception".
	 * <br>
	 * <b>Triggers:</b> nothing.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Object> ALL = (trigger, parameter) -> !"exception".equals(trigger);
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
	Action<Request> CONNECT = Action.action(Request.class, "connect", "connect");
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
	Action<Response> CONNECTED = Action.action(Response.class, "connected", "connected");
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
	Action<IOException> DISCONNECTED = Action.action(IOException.class, "disconnected|not-sent|not-received|malformed|not-parsed", "disconnected");
	/**
	 * <b>Mandatory</b>
	 * <br>
	 * <b>Client -> UEHandle</b>
	 * <br>
	 * <b>Triggered by:</b> the client internally to notify that a callback has thrown an
	 * uncaught exception. (and got caught by the caller itself)
	 * <br>
	 * <b>Triggers:</b> the uncaught exceptions handlers.
	 *
	 * @since 0.0.6 ~2021.03.29
	 */
	Action<Throwable> EXCEPTION = Action.action(Throwable.class, "exception", "exception");
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
	Action<IOException> MALFORMED = Action.action(IOException.class, "malformed", "malformed", "disconnected");
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
	Action<IOException> NOT_PARSED = Action.action(IOException.class, "not-parsed", "not-parsed", "disconnected");
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
	Action<IOException> NOT_RECEIVED = Action.action(IOException.class, "not-received", "not-received", "disconnected");
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
	Action<IOException> NOT_SENT = Action.action(IOException.class, "not-sent", "not-sent", "disconnected");
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
	Action<Response> RECEIVED = Action.action(Response.class, "received", "received");
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
	Action<String> RECEIVING = Action.action(String.class, "receiving", "receiving");
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
	Action<Request> SENDING = Action.action(Request.class, "sending", "sending");
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
	Action<String> SENT = Action.action(String.class, "sent", "sent");

	/**
	 * <b>Default</b>
	 * <br>
	 * Return a new client instance to be a placeholder if a the user has not specified a
	 * client.
	 *
	 * @return a new default client.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "->new", pure = true)
	static Client client() {
		return new AbstractClient();
	}

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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(Client client) {
		return new AbstractClient(client);
	}

	/**
	 * <b>Components</b>
	 * <br>
	 * Construct a new client with its request begin the given {@code request}.
	 *
	 * @param request the request of this client.
	 * @return a new client from the given {@code request}.
	 * @throws NullPointerException if the given {@code request} is null.
	 * @since 0.0.6 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull Request request) {
		return new AbstractClient(request);
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
	 *                                  RFC2396 and cannot be converted to a Uri.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull URL url) {
		return new AbstractClient().request(r -> r.setUri(Uri.uri(url)));
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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull URI uri) {
		return new AbstractClient().request(r -> r.setUri(Uri.uri(uri)));
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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull Uri uri) {
		return new AbstractClient().request(r -> r.setUri(uri));
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
	 *                                  UriRegExp#URI_REFERENCE}.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull @Pattern(UriRegExp.URI_REFERENCE) String uri) {
		return new AbstractClient().request(r -> r.setUri(uri));
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
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull File file) {
		return new AbstractClient().request(r -> r.setUri(Uri.uri(file)));
	}

	/**
	 * <b>Builder</b>
	 * <br>
	 * Construct a new client with the given {@code builder}.
	 *
	 * @param builder the builder to apply to the new client.
	 * @return the client constructed from the given {@code builder}.
	 * @throws NullPointerException if the given {@code builder} is null.
	 * @since 0.2.3 ~2021.08.27
	 */
	@NotNull
	@Contract(value = "_->new", pure = true)
	static Client client(@NotNull Consumer<Client> builder) {
		Objects.requireNonNull(builder, "builder");
		Client client = new AbstractClient();
		builder.accept(client);
		return client;
	}

	/**
	 * Send the request of this client:
	 * <br>
	 * Shortcut for:
	 * <pre>
	 *     client.{@link #perform(Action, Object) trigger}({@link #CONNECT Client.REQUEST}, client.{@link #getRequest()}.{@link Request#clone() clone()})
	 * </pre>
	 *
	 * @return this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("->this")
	default Client connect() {
		return this.perform(Client.CONNECT, this.getRequest().clone());
	}

	/**
	 * Replace the extras map of this client to be the result of invoking the given {@code
	 * operator} with the current extras map of this client. If the {@code operator}
	 * returned null then nothing happens.
	 * <br>
	 * Throwable thrown by the {@code operator} will fall throw this method unhandled.
	 *
	 * @param operator the computing operator.
	 * @return this.
	 * @throws NullPointerException          if the given {@code operator} is null.
	 * @throws UnsupportedOperationException if the extras map of this client cannot be
	 *                                       changed and the returned extras map from the
	 *                                       given {@code operator} is different from the
	 *                                       current extras map.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client extras(@NotNull UnaryOperator<Map<@Nullable String, @Nullable Object>> operator) {
		Objects.requireNonNull(operator, "operator");
		Map<String, Object> e = this.getExtras();
		Map<String, Object> extras = operator.apply(e);

		if (extras != null && extras != e)
			this.setExtras(extras);

		return this;
	}

	/**
	 * Add the given {@code callback} to be performed when the given action occurs. The
	 * thread executing the given {@code callback} is not specific so the given {@code
	 * callback} must not assume it will be executed in a specific thread (e.g. the
	 * application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param regex    the regex matching the targeted action names.
	 * @param callback the callback to be set.
	 * @return this.
	 * @throws NullPointerException if the given {@code regex} or {@code callback} is
	 *                              null.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	default Client on(@NotNull @Language("RegExp") String regex, @NotNull Callback<Object> callback) {
		return this.on(Action.action(regex), callback);
	}

	/**
	 * Add the given {@code callback} to be performed when the given action occurs. The
	 * thread executing the given {@code callback} is not specific so the given {@code
	 * callback} must not assume it will be executed in a specific thread (e.g. the
	 * application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param type     the type of the targeted parameters.
	 * @param regex    the regex matching the targeted action names.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code type} or {@code regex} or {@code
	 *                              callback} is null.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_,_,_->this", mutates = "this")
	default <T> Client on(@NotNull Class<? super T> type, @NotNull @Language("RegExp") String regex, @NotNull Callback<T> callback) {
		return this.on(Action.action(type, regex), callback);
	}

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with
	 * {@code null} as the parameter that was registered to be called when the given
	 * {@code action} occurs.
	 *
	 * @param action the action to be triggered.
	 * @param <T>    the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract("_->this")
	default <T> Client perform(@NotNull Action<T> action) {
		return this.perform(action, null);
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
		Request r = this.getRequest();
		Request request = operator.apply(r);

		if (request != null && request != r)
			this.setRequest(request);

		return this;
	}

	/**
	 * Set the extras map to be the given map.
	 *
	 * @param extras the new extras map.
	 * @return this.
	 * @throws NullPointerException          if the given {@code extras} is null.
	 * @throws UnsupportedOperationException if the extras map of this client cannot be
	 *                                       changed.
	 * @since 0.2.0 ~2021.08.26
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client setExtras(@NotNull Map<@Nullable String, @Nullable Object> extras) {
		throw new UnsupportedOperationException("setExtras");
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
	default Client setRequest(@NotNull Request request) {
		throw new UnsupportedOperationException("request");
	}

	/**
	 * Set the request of this from the given {@code request} literal.
	 *
	 * @param request the request literal to set the request of this from.
	 * @return this.
	 * @throws NullPointerException          if the given {@code request} is null.
	 * @throws IllegalArgumentException      if the given {@code request} does not match
	 *                                       {@link HttpRegExp#REQUEST}.
	 * @throws UnsupportedOperationException if the request of this client cannot be
	 *                                       changed.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(value = "_->this", mutates = "this")
	default Client setRequest(@NotNull @Pattern(HttpRegExp.REQUEST) String request) {
		return this.setRequest(Request.request(request));
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
	Client clone();

	/**
	 * Return the extras map.
	 *
	 * @return the extras map.
	 * @since 0.2.0
	 */
	@NotNull
	@Contract(pure = true)
	Map<@Nullable String, @Nullable Object> getExtras();

	/**
	 * Return the request defined for this.
	 *
	 * @return the request of this.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract(pure = true)
	Request getRequest();

	/**
	 * Inject the listeners of the given {@code middleware} to this client (using {@link
	 * Middleware#inject(Client)}). Duplicate injection is the middleware responsibility
	 * to solve. It is recommended for the middleware to inject the same callback instance
	 * to solve the problem.
	 *
	 * @param middleware the middleware to be injected.
	 * @return this.
	 * @throws NullPointerException     if the given {@code middleware} is null.
	 * @throws IllegalArgumentException if the given {@code middleware} refused to inject
	 *                                  its callbacks to this client for some aspect in
	 *                                  this client.
	 * @since 0.0.1 ~2021.03.23
	 */
	@NotNull
	@Contract("_->this")
	Client use(@NotNull Middleware middleware);

	/**
	 * Add the given {@code callback} to be performed when the given {@code action}
	 * occurs. The thread executing the given {@code callback} is not specific so the
	 * given {@code callback} must not assume it will be executed in a specific thread
	 * (e.g. the application thread).
	 * <br>
	 * Exceptions thrown by the given {@code callback} will be caught safely. But,
	 * exception by a thread created by the callback is left for the callback to handle.
	 *
	 * @param action   the action to listen to.
	 * @param callback the callback to be set.
	 * @param <T>      the type of the expected parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} or {@code callback} is
	 *                              null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract(value = "_,_->this", mutates = "this")
	<T> Client on(@NotNull Action<T> action, @NotNull Callback<T> callback);

	/**
	 * Trigger the given {@code action} in this caller. Invoke all the callbacks with the
	 * given {@code parameter} that was registered to be called when the given {@code
	 * action} occurs.
	 *
	 * @param parameter the parameter to invoke the callbacks with.
	 * @param action    the action to be triggered.
	 * @param <T>       the type of the parameter.
	 * @return this.
	 * @throws NullPointerException if the given {@code action} is null.
	 * @since 0.0.6 ~2021.03.28
	 */
	@NotNull
	@Contract("_,_->this")
	<T> Client perform(@NotNull Action<T> action, @Nullable T parameter);
}
