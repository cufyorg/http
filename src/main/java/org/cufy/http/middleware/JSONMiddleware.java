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

import org.cufy.http.body.JsonBody;
import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.request.Headers;
import org.cufy.http.response.Response;

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
public class JSONMiddleware implements Middleware {
	/**
	 * A global instance for {@link ParseCallback}.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Callback<Response> CALLBACK_PARSE = new ParseCallback();

	/**
	 * A global json middleware with all injection options enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	public static final Middleware MIDDLEWARE = new JSONMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json middleware with all the options enabled.
	 * @since 0.0.1 ~2021.03.24
	 */
	public static Middleware jsonMiddleware() {
		return JSONMiddleware.MIDDLEWARE;
	}

	@Override
	public void inject(Client client) {
		client.on(Client.RECEIVED, JSONMiddleware.CALLBACK_PARSE);
	}

	/**
	 * A callback that parses the body into a json-body.
	 *
	 * @author LSafer
	 * @version 0.0.1
	 * @since 0.0.1 ~2021.03.24
	 */
	public static class ParseCallback implements Callback<Response> {
		@Override
		public void call(Client client, Response response) {
			Objects.requireNonNull(client, "client");
			Objects.requireNonNull(response, "response");
			try {
				String contentType = response.getHeaders().get(Headers.CONTENT_TYPE);

				//noinspection DynamicRegexReplaceableByCompiledPattern
				if (contentType != null &&
					contentType.matches("^(?:application|text)\\/(x-)?json.*$"))
					response.body(JsonBody::json);
			} catch (IllegalArgumentException e) {
				client.perform(Client.NOT_PARSED, new IOException(e.getMessage(), e));
			}
		}
	}
}
