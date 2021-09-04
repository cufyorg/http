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
package org.cufy.http.middleware.json;

import org.cufy.http.connect.Client;
import org.cufy.http.middleware.Middleware;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static org.cufy.http.middleware.json.JsonResponseBodyCallback.jsonResponseBodyCallback;

/**
 * A middleware that adds middlewares that optimize the client for supporting JSON.
 * <br>
 * To use it you need to include <a href="https://github.com/stleary/JSON-java">org.json</a>
 * library.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.0.1 ~2021.03.23
 */
public class JsonMiddleware implements Middleware {
	/**
	 * A global json middleware with all injection options enabled.
	 *
	 * @since 0.0.1 ~2021.03.24
	 */
	private static final Middleware INSTANCE = new JsonMiddleware();

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a json middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Middleware jsonMiddleware() {
		return JsonMiddleware.INSTANCE;
	}

	@Override
	public void inject(@NotNull Client client) {
		client.on(Client.RECEIVED, jsonResponseBodyCallback());
	}
}
