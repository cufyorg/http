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

import org.cufy.http.Client;
import org.cufy.http.component.Headers;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.cufy.http.util.Callback;
import org.cufy.http.util.Caller;
import org.cufy.http.util.Middleware;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

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
@SuppressWarnings("ClassHasNoToStringMethod")
public class JSONMiddleware implements Middleware<Client> {
	/**
	 * A global instance for {@link ParseCallback}.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Callback<Client, Response> CALLBACK_PARSE = new ParseCallback();
	/**
	 * A global instance for {@link ReformatCallback}.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Callback<Client, Request> CALLBACK_REFORMAT = new ReformatCallback();

	/**
	 * A global json middleware with all injection options enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE = new JSONMiddleware(true, true);
	/**
	 * A global json middleware with only request-body injection option enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE_REQUEST = new JSONMiddleware(true, false);
	/**
	 * A global json middleware with only response-body injection option enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware<Client> MIDDLEWARE_RESPONSE = new JSONMiddleware(false, true);

	/**
	 * The "on-json-parsed" action name.
	 * <br>
	 * Parameter type:
	 * <pre>
	 *     {@link JSONObject} not-null: the parsed json-object.
	 * </pre>
	 * Trigger On:
	 * <ul>
	 *     <li>A response's body got parsed into a json-object</li>
	 * </ul>
	 *
	 * @since 0.0.1 ~2021.03.23
	 */
	public static final String PARSED = "on-json-parsed";

	/**
	 * True, inject request-body related callbacks.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	protected final boolean jsonRequestBody;
	/**
	 * True, inject response-body related callbacks.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	protected final boolean jsonResponseBody;

	/**
	 * Construct a new json-middleware with the given injection options.
	 *
	 * @param requestBody  inject request-body related callbacks.
	 * @param responseBody inject response-body related callbacks.
	 * @since 0.0.1 ~2021.03.24
	 */
	public JSONMiddleware(boolean requestBody, boolean responseBody) {
		this.jsonRequestBody = requestBody;
		this.jsonResponseBody = responseBody;
	}

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

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json middleware with the request related options enabled.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middlewareRequest() {
		return JSONMiddleware.MIDDLEWARE_REQUEST;
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json middleware with the response related options enabled.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware<Client> middlewareResponse() {
		return JSONMiddleware.MIDDLEWARE_RESPONSE;
	}

	@Override
	public void inject(Caller<Client> caller) {
		if (caller instanceof Client) {
			if (this.jsonRequestBody)
				caller.on(Client.REFORMAT, JSONMiddleware.CALLBACK_REFORMAT);
			if (this.jsonResponseBody)
				caller.on(Client.S2XX, JSONMiddleware.CALLBACK_PARSE);

			return;
		}

		throw new IllegalArgumentException("JSONMiddleware is made for Clients");
	}

	/**
	 * A callback for the action {@link Client#S2XX} and the header {@link
	 * Headers#CONTENT_TYPE} contains {@code application/json} that triggers the action
	 * {@link #PARSED} with a json-object if the response's body was successfully parsed
	 * and triggers the action {@link Client#UNEXPECTED} when the parsing fails.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.24
	 */
	public static class ParseCallback implements Callback<Client, Response> {
		@Override
		public void call(@NotNull Client caller, Response response) {
			Objects.requireNonNull(caller, "caller");
			Objects.requireNonNull(response, "response");
			String contentType = response.headers().get(Headers.CONTENT_TYPE);

			if (contentType != null && contentType.contains("application/json"))
				try {
					String body = response.body().toString();

					JSONObject object = new JSONObject(body);

					caller.trigger(object, JSONMiddleware.PARSED);
				} catch (JSONException e) {
					caller.trigger(response, Client.UNEXPECTED);
				}
		}
	}

	/**
	 * The callback for the action {@link Client#REFORMAT}. Makes sure that the request
	 * has a content type. If not then it sets the suitable content type for json.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.23
	 */
	public static class ReformatCallback implements Callback<Client, Request> {
		@Override
		public void call(@NotNull Client caller, Request request) {
			Objects.requireNonNull(caller, "caller");
			Objects.requireNonNull(request, "request");
			request.headers()
					.computeIfAbsent(
							Headers.CONTENT_TYPE,
							() -> "application/json; charset=utf-8"
					);
		}
	}
}
