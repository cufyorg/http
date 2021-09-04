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
package org.cufy.http.middleware.cache;

import org.cufy.http.connect.Callback;
import org.cufy.http.connect.Client;
import org.cufy.http.middleware.Middleware;
import org.cufy.http.request.Request;
import org.cufy.http.response.Response;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static org.cufy.http.middleware.cache.CacheConnectionCallback.cacheConnectionCallback;
import static org.cufy.http.middleware.cache.CacheResponseCacheCallback.cacheResponseCacheCallback;

/**
 * A middleware that manages cache.
 *
 * @author LSafer
 * @version 0.2.11
 * @since 0.2.11
 */
public class CacheMiddleware implements Middleware {
	/**
	 * A global instance of the cache-middleware with a global cache repository.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	private static final Middleware INSTANCE = new CacheMiddleware();

	/**
	 * The connection callback to be injected by this middleware.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final Callback<Request> connectionCallback;
	/**
	 * The response cache callback to be injected by this middleware.
	 *
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	protected final Callback<Response> responseCacheCallback;

	/**
	 * Construct a new cache middleware.
	 *
	 * @since 0.2.1 ~2021.09.04
	 */
	public CacheMiddleware() {
		CacheRepository repository = new MemoryCacheRepository(CacheValidator.validator());
		this.connectionCallback = cacheConnectionCallback(repository);
		this.responseCacheCallback = cacheResponseCacheCallback(repository);
	}

	/**
	 * Construct a new cache middleware.
	 *
	 * @param repository the repository to be used by the middleware.
	 * @throws NullPointerException if the given {@code repository} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	public CacheMiddleware(@NotNull CacheRepository repository) {
		Objects.requireNonNull(repository, "repository");
		this.connectionCallback = cacheConnectionCallback(repository);
		this.responseCacheCallback = cacheResponseCacheCallback(repository);
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @return a cache middleware.
	 * @since 0.0.1 ~2021.03.24
	 */
	@NotNull
	@Contract(pure = true)
	public static Middleware cacheMiddleware() {
		return CacheMiddleware.INSTANCE;
	}

	/**
	 * Return a usable middleware for the caller. The caller might not store the returned
	 * instance on multiple targets. Instead, calling this method to get an instance
	 * everytime.
	 *
	 * @param repository the cache repository to be used by the middleware.
	 * @return a cache middleware.
	 * @throws NullPointerException if the given {@code client} is null.
	 * @since 0.2.11 ~2021.09.04
	 */
	@NotNull
	@Contract(pure = true)
	public static Middleware cacheMiddleware(@NotNull CacheRepository repository) {
		Objects.requireNonNull(repository, "repository");
		return new CacheMiddleware(repository);
	}

	@Override
	public void inject(@NotNull Client client) {
		client.on(Client.CACHED, this.connectionCallback);
		client.on(Client.CONNECTED, this.responseCacheCallback);
	}
}
