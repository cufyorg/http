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
package org.cufy.http.middleware;

import org.cufy.http.body.JSONBody;
import org.cufy.http.connect.Action;
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Caller;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

/**
 * A middleware that adds middlewares that optimize the client for supporting JSON.
 * <br>
 * To use it you need to include <a href="https://github.com/stleary/JSON-java">org.json</a>
 * library.
 *
 * @author LSafer
 * @version 0.0.1
 * @since 0.0.1 ~2021.03.23
 */
public class JSONMiddleware implements Middleware<Client> {
	/**
	 * A global instance for {@link ParseCallback}.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Callback<Client<?>, Response<?>> CALLBACK_PARSE = new ParseCallback();

	/**
	 * A modified version of the action {@link Client#CONNECTED} for when the body was
	 * parsed into a {@link JSONBody json body} due to this middleware.
	 *
	 * @since 0.0.1 ~2021.03.31
	 */
	public static final Action<Response<JSONBody>> CONNECTED = Action.of(Response.class, "connected", "connected");

	/**
	 * A global json middleware with all injection options enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE = new JSONMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json middleware with all the options enabled.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middleware() {
		return JSONMiddleware.MIDDLEWARE;
	}

	@Override
	public void inject(Caller<? extends Client> caller) {
		if (caller instanceof Client) {
			Client client = (Client) caller;

			//noinspection unchecked
			client.on(Client.RECEIVED, JSONMiddleware.CALLBACK_PARSE);

			return;
		}

		throw new IllegalArgumentException("JSONMiddleware is made for Clients");
	}

	/**
	 * A callback that parses the body into a json-body.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.24
	 */
	public static class ParseCallback implements Callback<Client<?>, Response<?>> {
		@Override
		public void call(@NotNull Client<?> client, Response<?> response) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(response, "response");
			try {
				String contentType = response.getHeaders().get(Headers.CONTENT_TYPE);

				//noinspection DynamicRegexReplaceableByCompiledPattern
				if (contentType != null &&
					contentType.matches("^(?:application|text)\\/(x-)?json.*$"))
					response.body(JSONBody::copy);
			} catch (IllegalArgumentException e) {
				client.trigger(Client.NOT_PARSED, new IOException(e.getMessage(), e));
			}
		}
	}
}
//
//	/**
//	 * Return a usable middleware for the caller. The caller might not store the returned
//	 * instance on multiple targets. Instead, calling this method to get an instance
//	 * everytime.
//	 *
//	 * @return a json middleware with the request related options enabled.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	public static Middleware<Client> middlewareRequest() {
//		return JSONMiddleware.MIDDLEWARE_REQUEST;
//	}
//
//	/**
//	 * Return a usable middleware for the caller. The caller might not store the returned
//	 * instance on multiple targets. Instead, calling this method to get an instance
//	 * everytime.
//	 *
//	 * @return a json middleware with the response related options enabled.
//	 * @since 0.0.1 ~2021.03.24
//	 */
//	public static Middleware<Client> middlewareResponse() {
//		return JSONMiddleware.MIDDLEWARE_RESPONSE;
//	}
//	/**
//	 * The action to be called when the json middleware finished parsing the json body.
//	 * <br>
//	 * Parameter type:
//	 * <pre>
//	 *     {@link JSONObject} not-null: the parsed json-object.
//	 * </pre>
//	 * Trigger On:
//	 * <ul>
//	 *     <li>A response's body got parsed into a json-object</li>
//	 * </ul>
//	 *
//	 * @since 0.0.1 ~2021.03.28
//	 */
//	public static final Action<JSONObject> PARSED = Action.of(JSONObject.class, "on-json-parsed", "parsed", "on-json-parsed");
